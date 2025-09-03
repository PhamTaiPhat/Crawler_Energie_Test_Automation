package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public record UiContext(WebDriver driver, WebDriverWait waiter) {
}


