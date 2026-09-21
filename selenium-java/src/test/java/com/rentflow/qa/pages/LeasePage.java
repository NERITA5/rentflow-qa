package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class LeasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By newLeaseButton = By.cssSelector("[data-testid='lease-create']");
    private By tenantSearchField = By.cssSelector("[data-testid='lease-tenant-search']");
    private By tenantOption = By.cssSelector("[data-testid='lease-tenant-option']");
    private By unitDropdown = By.cssSelector("[data-testid='lease-unit-select']");
    private By startDateField = By.cssSelector("#l-start");
    private By endDateField = By.cssSelector("#l-end");
    private By reviewButton = By.cssSelector("[data-testid='lease-create-submit']");
    private By confirmSubmitButton = By.cssSelector("[data-testid='confirm-submit']");
    private By toast = By.cssSelector("[data-testid='toast']");
    private By acceptButton = By.cssSelector("[data-testid='lease-accept']");

    public LeasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickNewLease() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(newLeaseButton));
        scrollAndClick(btn);
    }

    /**
     * Types into the tenant search box, then explicitly waits for at least one
     * matching option to actually appear before clicking it. The dropdown only
     * re-renders AFTER React processes the typed input, so grabbing the option
     * list immediately after sendKeys() risks a race condition where the list
     * hasn't updated yet.
     */
    public void searchAndSelectTenant(String searchText) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(tenantSearchField));
        searchInput.clear();
        searchInput.sendKeys(searchText);
        
        wait.until(ExpectedConditions.presenceOfElementLocated(tenantOption));
        List<WebElement> options = driver.findElements(tenantOption);
        scrollAndClick(options.get(0));
    }

    /**
     * Selects the first real unit in the dropdown (index 0 is always the
     * "Select a unit..." placeholder). We deliberately don't hardcode a specific
     * unit name here — which unit is currently VACANT depends on the app's live
     * state from all prior testing, not something this test can predict in advance.
     */
    public String selectFirstAvailableUnit() {
        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(unitDropdown));
        Select dropdown = new Select(dropdownElement);
        List<WebElement> options = dropdown.getOptions();
        if (options.size() <= 1) {
            throw new IllegalStateException(
                "No vacant units are available for this landlord right now — " +
                "this test needs at least one VACANT unit to exist to proceed."
            );
        }
        WebElement firstRealOption = options.get(1);
        String label = firstRealOption.getText();
        dropdown.selectByIndex(1);
        return label;
    }

    /**
     * Sets a date input's value in a way React actually notices. A plain
     * element.value = "..." assignment via JavaScript never fires the 'input'
     * event React listens for, so React's own state stays out of sync with what's
     * visually on screen. The fix: call the native browser value setter directly
     * (bypassing React's override), then manually dispatch a real 'input' event.
     */
    public void setDates(String startDate, String endDate) {
        setReactControlledDateValue(startDateField, startDate);
        setReactControlledDateValue(endDateField, endDate);
    }

    private void setReactControlledDateValue(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        String script =
            "var nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeSetter.call(arguments[0], arguments[1]);" +
            "var event = new Event('input', { bubbles: true });" +
            "arguments[0].dispatchEvent(event);";
        ((JavascriptExecutor) driver).executeScript(script, element, value);
    }

    public void clickAcceptLease() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(acceptButton));
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

    public By getReviewButtonLocator() {
        return reviewButton;
    }

    public By getConfirmSubmitLocator() {
        return confirmSubmitButton;
    }

    public String getToastMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(toast)).getText();
    }

    public By getAcceptButtonLocator() {
        return acceptButton;
    }
}