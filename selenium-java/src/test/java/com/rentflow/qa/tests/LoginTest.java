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
        loginPage.login("landlord@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    void invalidLoginShowsError() {
        driver.get(BASE_URL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("landlord@rentflow.dev", "wrongpassword");

        String error = loginPage.getErrorMessage();
        assertFalse(error.isEmpty());
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}