package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import com.rentflow.qa.pages.PropertiesPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PropertyCreationTest extends BaseTest {

    @Test
    void landlordCanCreateProperty() {
        // Log in first — property creation requires authentication as a landlord.
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("sarah.landlord@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/properties");

        PropertiesPage propertiesPage = new PropertiesPage(driver);
        propertiesPage.clickNewProperty();

        // Unique name per run — same reasoning as the unique email in RegisterTest:
        // the app rejects a duplicate name+address for the same landlord, so a fixed
        // name would make this test fail on every run after the first.
        String uniquePropertyName = "QA Test Property " + System.currentTimeMillis();
        propertiesPage.fillPropertyForm(uniquePropertyName, "123 Test Street", "Testville", "AB", "T1T 1T1");

        scrollAndClick(propertiesPage.getSubmitButtonLocator());

        // Wait for the dialog to actually close, then confirm the new property
        // shows up in the list — proof it was really created, not just that the
        // form submitted without a client-side error.
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("[data-testid='property-form-dialog']")));

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            org.openqa.selenium.By.tagName("body"), uniquePropertyName));

        assertTrue(driver.getPageSource().contains(uniquePropertyName));
    }
}