package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    private By emailField = By.cssSelector("[data-testid='login-email']");
    private By passwordField = By.cssSelector("[data-testid='login-password']");
    private By submitButton = By.cssSelector("[data-testid='login-submit']");
    private By errorMessage = By.cssSelector("[data-testid='login-error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSubmit() {
        driver.findElement(submitButton).click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}