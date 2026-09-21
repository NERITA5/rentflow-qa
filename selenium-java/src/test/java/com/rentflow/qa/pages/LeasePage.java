package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class LeasePage {

    private WebDriver driver;

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
    }

    public void clickNewLease() {
        driver.findElement(newLeaseButton).click();
    }

    public void searchAndSelectTenant(String searchText) {
        driver.findElement(tenantSearchField).sendKeys(searchText);
        List<WebElement> options = driver.findElements(tenantOption);
        options.get(0).click();
    }

    /**
     * Selects the first real unit in the dropdown (index 0 is always the
     * "Select a unit..." placeholder). We deliberately don't hardcode a specific
     * unit name here — which unit is currently VACANT depends on the app's live
     * state from all prior testing, not something this test can predict in advance.
     */
    public String selectFirstAvailableUnit() {
        Select dropdown = new Select(driver.findElement(unitDropdown));
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
     * Sets a date input's value in a way React actually notices.
     *
     * Just doing element.value = "..." via JavaScript changes what's visually shown
     * in the browser, but React's controlled-component state never finds out — React
     * only reacts to a genuine 'input' event, and a plain value assignment doesn't
     * fire one. The fix: call the BROWSER'S OWN original value setter (bypassing
     * React's override of it) via its prototype, THEN manually dispatch a real
     * 'input' event. This is a well-known, standard workaround whenever you need to
     * set a React-controlled input's value from outside React itself — exactly what
     * Selenium is doing here.
     */
    public void setDates(String startDate, String endDate) {
        setReactControlledDateValue(startDateField, startDate);
        setReactControlledDateValue(endDateField, endDate);
    }

    private void setReactControlledDateValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        String script =
            "var nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeSetter.call(arguments[0], arguments[1]);" +
            "var event = new Event('input', { bubbles: true });" +
            "arguments[0].dispatchEvent(event);";
        ((JavascriptExecutor) driver).executeScript(script, element, value);
    }

    public By getReviewButtonLocator() {
        return reviewButton;
    }

    public By getConfirmSubmitLocator() {
        return confirmSubmitButton;
    }

    public String getToastMessage() {
        return driver.findElement(toast).getText();
    }

    public By getAcceptButtonLocator() {
        return acceptButton;
    }
}