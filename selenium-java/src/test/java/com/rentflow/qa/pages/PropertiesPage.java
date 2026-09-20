package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PropertiesPage {

    private WebDriver driver;

    private By newPropertyButton = By.cssSelector("[data-testid='property-create']");
    private By nameField = By.cssSelector("[data-testid='property-name-input']");
    // These fields have no data-testid in the app (a real, minor gap worth noting) —
    // falling back to their HTML id attributes, which is a workable but less stable
    // locator strategy than data-testid, since ids can change for styling reasons.
    private By addressField = By.cssSelector("#p-address");
    private By cityField = By.cssSelector("#p-city");
    private By provinceField = By.cssSelector("#p-province");
    private By postalField = By.cssSelector("#p-postal");
    private By submitButton = By.cssSelector("[data-testid='property-form-submit']");
    private By dialog = By.cssSelector("[data-testid='property-form-dialog']");

    public PropertiesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickNewProperty() {
        driver.findElement(newPropertyButton).click();
    }

    public void fillPropertyForm(String name, String address, String city, String province, String postalCode) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(provinceField).sendKeys(province);
        driver.findElement(postalField).sendKeys(postalCode);
    }

    public boolean isDialogOpen() {
        return driver.findElements(dialog).size() > 0;
    }

    public By getSubmitButtonLocator() {
        return submitButton;
    }
}