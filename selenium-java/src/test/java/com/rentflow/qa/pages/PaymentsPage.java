package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PaymentsPage {

    private WebDriver driver;

    private By makePaymentButton = By.cssSelector("[data-testid='payment-make']");
    private By amountField = By.cssSelector("[data-testid='payment-amount-input']");
    private By dueDateField = By.cssSelector("#pay-due");
    private By methodDropdown = By.cssSelector("[data-testid='payment-method-select']");
    private By submitButton = By.cssSelector("[data-testid='payment-submit']");
    private By toast = By.cssSelector("[data-testid='toast']");

    public PaymentsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickMakePayment() {
        driver.findElement(makePaymentButton).click();
    }

    public void setAmount(String amount) {
        driver.findElement(amountField).clear();
        driver.findElement(amountField).sendKeys(amount);
    }

    public void setDueDate(String date) {
        // HTML <input type="date"> elements are notoriously unreliable with sendKeys() —
        // Chrome's native date picker doesn't always accept typed keystrokes the way a
        // plain text field does, and behavior can vary depending on locale/OS settings.
        // Setting the value directly via JavaScript sidesteps that flakiness entirely.
        // Expected format here is yyyy-MM-dd (e.g. "2026-12-01"), matching the input's
        // native value format regardless of how the date is visually displayed on screen.
        WebElement field = driver.findElement(dueDateField);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", field, date);
    }

    public void selectMethod(String method) {
        new Select(driver.findElement(methodDropdown)).selectByValue(method);
    }

    public By getSubmitButtonLocator() {
        return submitButton;
    }

    public String getToastMessage() {
        return driver.findElement(toast).getText();
    }
}