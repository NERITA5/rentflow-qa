package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {

    private WebDriver driver;

    private By nameField = By.cssSelector("[data-testid='register-name']");
    private By emailField = By.cssSelector("[data-testid='register-email']");
    private By phoneField = By.cssSelector("[data-testid='register-phone']");
    private By passwordField = By.cssSelector("[data-testid='register-password']");
    private By roleDropdown = By.cssSelector("[data-testid='register-role']");
    private By submitButton = By.cssSelector("[data-testid='register-submit']");
    private By errorMessage = By.cssSelector("[data-testid='register-error']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void selectRole(String role) {
        // register-role is a real <select> dropdown, not a text field — Selenium needs
        // its dedicated Select class to interact with dropdown options correctly.
        // "role" here must exactly match an <option> value: "TENANT" or "LANDLORD".
        Select dropdown = new Select(driver.findElement(roleDropdown));
        dropdown.selectByValue(role);
    }

    public void clickSubmit() {
    WebElement button = driver.findElement(submitButton);
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
    new WebDriverWait(driver, Duration.ofSeconds(5))
        .until(ExpectedConditions.elementToBeClickable(submitButton));
    driver.findElement(submitButton).click();
}

    public void register(String name, String email, String phone, String password, String role) {
        enterName(name);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        selectRole(role);
        clickSubmit();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}