package com.rentflow.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class MaintenancePage {

    private WebDriver driver;

    private By newRequestButton = By.cssSelector("[data-testid='maintenance-create']");
    private By titleField = By.cssSelector("[data-testid='maintenance-title-input']");
    private By descriptionField = By.cssSelector("#m-desc");
    private By priorityDropdown = By.cssSelector("#m-priority");
    private By submitButton = By.cssSelector("[data-testid='maintenance-submit']");
    private By toast = By.cssSelector("[data-testid='toast']");

    public MaintenancePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickNewRequest() {
        driver.findElement(newRequestButton).click();
    }

    public void fillRequestForm(String title, String description, String priority) {
        // Plain text/textarea fields via sendKeys() are fine here, unlike the date
        // inputs we hit earlier — sendKeys() simulates real keystrokes, which DOES
        // fire genuine input events React listens for. The React-controlled-value
        // problem only happens when you set .value directly via raw JavaScript,
        // bypassing real browser events entirely — we're not doing that here.
        driver.findElement(titleField).sendKeys(title);
        driver.findElement(descriptionField).sendKeys(description);
        new Select(driver.findElement(priorityDropdown)).selectByValue(priority);
    }

    public By getSubmitButtonLocator() {
        return submitButton;
    }

    public String getToastMessage() {
        return driver.findElement(toast).getText();
    }
}