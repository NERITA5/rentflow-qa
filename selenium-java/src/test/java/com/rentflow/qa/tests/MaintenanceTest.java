package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import com.rentflow.qa.pages.MaintenancePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaintenanceTest extends BaseTest {

    @Test
    void tenantCanSubmitMaintenanceRequest() {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("tenant@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/maintenance");

        MaintenancePage maintenancePage = new MaintenancePage(driver);
        maintenancePage.clickNewRequest();

        // Unique title per run, same reasoning as every other creation test —
        // makes it easy to independently verify this exact request now exists.
        String uniqueTitle = "QA Test Issue " + System.currentTimeMillis();
        maintenancePage.fillRequestForm(uniqueTitle, "Automated test — leaking faucet in unit.", "MEDIUM");

        scrollAndClick(maintenancePage.getSubmitButtonLocator());

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("[data-testid='toast']")));
        assertFalse(maintenancePage.getToastMessage().isEmpty());

        // Confirm the new request genuinely appears in the list afterward —
        // proof it was actually created, not just that the form closed cleanly.
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            org.openqa.selenium.By.tagName("body"), uniqueTitle));
        assertTrue(driver.getPageSource().contains(uniqueTitle));
    }
}