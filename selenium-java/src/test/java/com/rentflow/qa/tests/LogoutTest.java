package com.rentflow.qa.tests;

import com.rentflow.qa.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest extends BaseTest {

    @Test
    void userCanLogOut() {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("sarah.landlord@rentflow.dev", "Password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        // Logout lives inside a dropdown menu — open it first, then click the
        // actual logout option inside it.
        driver.findElement(By.cssSelector("[data-testid='user-menu-trigger']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-testid='logout-button']")));
        driver.findElement(By.cssSelector("[data-testid='logout-button']")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));

        // Extra check worth doing: confirm the session is REALLY gone server-side,
        // not just that the page navigated. If we could still reach a protected
        // page afterward, that would be a real security bug (session not fully
        // invalidated on logout).
        driver.get(BASE_URL + "/dashboard");
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}