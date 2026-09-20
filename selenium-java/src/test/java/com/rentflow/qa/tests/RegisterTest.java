package com.rentflow.qa.tests;

import com.rentflow.qa.pages.RegisterPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegisterTest extends BaseTest {

    @Test
    void validRegistrationRedirectsToDashboard() {
        driver.get(BASE_URL + "/register");

        // A unique email per run, so this test can be re-executed repeatedly without
        // colliding with an account it created on a previous run (the app correctly
        // rejects duplicate emails, which is exactly what our second test checks —
        // but that would break THIS test if we reused a fixed email).
        String uniqueEmail = "qa.automation." + System.currentTimeMillis() + "@rentflow.dev";

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("QA Automation Test", uniqueEmail, "5551234567", "Password123", "TENANT");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    void duplicateEmailShowsError() {
        driver.get(BASE_URL + "/register");

        RegisterPage registerPage = new RegisterPage(driver);
        // landlord@rentflow.dev is a real seeded account — guaranteed to already exist,
        // so this is a reliable way to trigger the duplicate-email rejection every time.
        registerPage.register("Duplicate Test", "landlord@rentflow.dev", "5551234567", "Password123", "TENANT");

        String error = registerPage.getErrorMessage();
        assertFalse(error.isEmpty());
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }
}