package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginTest extends BaseTest {

    @Test
    void validLoginRedirectsToDashboard() {
        driver.get(BASE_URL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("sarah.landlord@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    void invalidLoginShowsError() {
        driver.get(BASE_URL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        // Deliberately using an email that doesn't exist at all, rather than a real
        // account's wrong password — this avoids ever contributing failed attempts
        // toward the rate limiter on an account another test (or you, manually)
        // relies on for a VALID login. The app still correctly rejects this with
        // the same "invalid credentials" error either way, so the test still proves
        // the same thing, just without any risk of poisoning another test.
        loginPage.login("nonexistent.qa.test@rentflow.dev", "wrongpassword");

        String error = loginPage.getErrorMessage();
        assertFalse(error.isEmpty());
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}