package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import com.rentflow.qa.pages.PaymentsPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PaymentTest extends BaseTest {

    @Test
    void tenantCanSubmitPayment() {
        // tenant@rentflow.dev has a real seeded ACTIVE lease — required, since the
        // "Make a Payment" button is disabled entirely for a tenant with no active lease.
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("tenant@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/payments");

        PaymentsPage paymentsPage = new PaymentsPage(driver);

        // Explicitly wait for the button to become enabled — it starts disabled
        // until the tenant's active lease finishes loading asynchronously.
        wait.until(ExpectedConditions.elementToBeClickable(
            org.openqa.selenium.By.cssSelector("[data-testid='payment-make']")));
        paymentsPage.clickMakePayment();

        paymentsPage.setAmount("1500");
        paymentsPage.setDueDate("2026-12-01");
        paymentsPage.selectMethod("CREDIT_CARD");

        scrollAndClick(paymentsPage.getSubmitButtonLocator());

        // IMPORTANT: this app SIMULATES payments with a random ~95% success / 5% failure
        // outcome (see the real app code in /api/payments). That means we genuinely
        // cannot assert one fixed message every time — sometimes it's a success toast,
        // sometimes a failure toast, and BOTH are correct, expected app behavior.
        // So instead of asserting exact text, we assert that SOME confirmation toast
        // appeared at all, proving the submission was actually processed either way.
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("[data-testid='toast']")));

        String message = paymentsPage.getToastMessage();
        assertFalse(message.isEmpty());
    }
}