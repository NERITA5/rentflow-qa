package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import com.rentflow.qa.pages.RegisterPage;
import com.rentflow.qa.pages.LeasePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LeaseWorkflowTest extends BaseTest {

    private static String tenantEmail;
    private static final String TENANT_PASSWORD = "Password123";

    /**
     * Logs the current user out and BLOCKS until it's genuinely finished server-side,
     * unlike a plain fire-and-forget fetch() call. executeScript() returns immediately
     * without waiting for a Promise to resolve — so a plain fetch() there can let the
     * test navigate to /login while the logout request is still in flight, meaning the
     * old session cookie may still be valid when the next page loads. executeAsyncScript()
     * instead waits for an explicit callback, so we only proceed once the logout request
     * has actually completed.
     */
    private void logout() {
        ((JavascriptExecutor) driver).executeAsyncScript(
            "var callback = arguments[arguments.length - 1];" +
            "fetch('/api/auth/logout', {method: 'POST'})" +
            ".then(() => callback())" +
            ".catch(() => callback());"
        );
    }

    @Test
    @Order(1)
    void landlordCreatesLeaseForNewTenant() {
        tenantEmail = "qa.lease.tenant." + System.currentTimeMillis() + "@rentflow.dev";

        driver.get(BASE_URL + "/register");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("QA Lease Tenant", tenantEmail, "5559876543", TENANT_PASSWORD, "TENANT");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        logout();
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
       loginPage.login("landlord@rentflow.dev", "Password123");
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/leases");
        LeasePage leasePage = new LeasePage(driver);
        leasePage.clickNewLease();

        leasePage.searchAndSelectTenant(tenantEmail);
        leasePage.selectFirstAvailableUnit();
        leasePage.setDates("2026-11-01", "2027-11-01");

        scrollAndClick(leasePage.getReviewButtonLocator());
        wait.until(ExpectedConditions.elementToBeClickable(leasePage.getConfirmSubmitLocator()));
        scrollAndClick(leasePage.getConfirmSubmitLocator());

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("[data-testid='toast']")));
        String message = leasePage.getToastMessage();
        assertFalse(message.isEmpty());
    }

    @Test
    @Order(2)
    void tenantCanAcceptPendingLease() {
        logout();
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(tenantEmail, TENANT_PASSWORD);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/leases");

        LeasePage leasePage = new LeasePage(driver);
        wait.until(ExpectedConditions.elementToBeClickable(
            org.openqa.selenium.By.cssSelector("[data-testid='lease-accept-inline']")));
        driver.findElement(org.openqa.selenium.By.cssSelector("[data-testid='lease-accept-inline']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(leasePage.getAcceptButtonLocator()));
        driver.findElement(leasePage.getAcceptButtonLocator()).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            org.openqa.selenium.By.cssSelector("[data-testid='confirm-submit']")));
        driver.findElement(org.openqa.selenium.By.cssSelector("[data-testid='confirm-submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("[data-testid='toast']")));

        assertTrue(driver.getPageSource().contains("ACTIVE") || driver.getPageSource().toLowerCase().contains("active"));
    }
}