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

    /**
     * Sets the date input's value in a way React actually notices. A plain
     * element.value = "..." assignment via JavaScript changes what's visually shown
     * but never fires the 'input' event React listens for, so React's own state stays
     * out of sync with what's on screen. The fix: call the native browser value setter
     * directly (bypassing React's override), then manually dispatch a real 'input'
     * event so React picks up the change.
     */
    public void setDueDate(String date) {
        WebElement field = driver.findElement(dueDateField);
        String script =
            "var nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeSetter.call(arguments[0], arguments[1]);" +
            "var event = new Event('input', { bubbles: true });" +
            "arguments[0].dispatchEvent(event);";
        ((JavascriptExecutor) driver).executeScript(script, field, date);
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