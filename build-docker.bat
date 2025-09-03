@echo off
setlocal ENABLEEXTENSIONS

REM --- Ensure Docker Desktop is running ---
docker version >nul 2>&1
if errorlevel 1 (
  echo [!] Docker Desktop is not running or not reachable.
  pause
  exit /b 1
)

REM --- Prepare host folders ---
if not exist "logs" mkdir "logs"
if not exist "artifacts" mkdir "artifacts"

REM --- Build tests image ---
echo [*] Building Docker image: tryout-automation
docker build -t tryout-automation .
if errorlevel 1 ( echo [!] Build failed & pause & exit /b 1 )

REM --- Network for cross-container DNS ---
docker network create tryout-net >nul 2>&1

REM --- Pull & start Selenium (noVNC on :7900) ---
docker pull selenium/standalone-chrome:latest
echo [*] Starting selenium/standalone-chrome (noVNC: http://localhost:7900)
docker run -d --rm ^
  --name selenium ^
  --network tryout-net ^
  --shm-size=2g ^
  -p 4444:4444 ^
  -p 7900:7900 ^
  -e SE_VNC_PASSWORD=secret ^
  selenium/standalone-chrome:latest
if errorlevel 1 ( echo [!] Failed to start selenium & pause & exit /b 1 )

REM --- (Optional) wait briefly for Selenium to come up ---
echo [*] Waiting for Selenium at http://localhost:4444/status ...
for /l %%i in (1,1,30) do (
  curl -s http://localhost:4444/status >nul 2>&1 && goto ready
  timeout /t 1 >nul
)
:ready

REM --- Run tests container (REMOTE, non-headless) ---
echo [*] Running tests (connect to Selenium at selenium:4444)...
docker run --rm ^
  --name tryout-tests ^
  --network tryout-net ^
  -e SELENIUM_URL=http://selenium:4444/wd/hub ^
  -e HEADLESS=false ^
  -e LOG_DIR=/logs ^
  -e ARTIFACTS_DIR=/artifacts ^
  -v "%CD%\logs:/logs" ^
  -v "%CD%\artifacts:/artifacts" ^
  tryout-automation
set ERR=%ERRORLEVEL%

echo [*] Stopping selenium...
docker stop selenium >nul 2>&1

if %ERR% NEQ 0 (
  echo [!] Tests exited with code %ERR%
) else (
  echo [+] Tests completed. Open logs\ and artifacts\ on your host.
  echo [+] Watch live at http://localhost:7900/?autoconnect=1&resize=scale while Selenium is running.
)
pause
exit /b %ERR%
