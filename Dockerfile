# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install Chrome dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Install Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Copy everything at once
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Set Chrome environment with unique user data directory
ENV CHROME_BIN=/usr/bin/google-chrome
ENV CHROME_USER_DATA_DIR=/tmp/chrome-user-data

# Create unique Chrome user data directory
RUN mkdir -p /tmp/chrome-user-data

# Run tests
CMD ["./mvnw", "test"]