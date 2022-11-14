package com.lsch0037.cps3230;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*  This class provides extra functionality required by the PageObject calles
 *  Inspired by the LibraryUtils.java class seen here "https://www.youtube.com/watch?v=V6_VfcriQAE"
 */
public class ScreenScraperUtils {
    /* Waits for the element in question to be visible and then returns it
     *  Returns element or null if the element does not become visible as the timer expires 
     */ 
    public static WebElement awaitElementVisible(WebDriver driver, WebElement awaitElement, int seconds){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOf(awaitElement));
    }
}
