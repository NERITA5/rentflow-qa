package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By nameField = By.cssSelector("[data-testid='register-name']");
    private By emailField = By.cssSelector("[data-testid='register-email']");
    private By phoneField = By.cssSelector("[data-testid='register-phone']");
    private By passwordField = By.cssSelector("[data-testid='register-password']");
    private By roleDropdown = By.cssSelector("[data-testid='register-role']");
    private By submitButton = By.cssSelector("[data-testid='register-submit']");
    private By errorMessage = By.cssSelector("[data-testid='register-error']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        el.clear();
        el.sendKeys(name);
    }

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        el.clear();
        el.sendKeys(email);
    }

    public void enterPhone(String phone) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField));
        el.clear();
        el.sendKeys(phone);
    }

    public void enterPassword(String password) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        el.clear();
        el.sendKeys(password);
    }

    public void selectRole(String role) {
        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(roleDropdown));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByValue(role);
    }

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollAndClick(btn);
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}