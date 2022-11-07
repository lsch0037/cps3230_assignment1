package com.lsch0037.cps3230Test;

import com.lsch0037.cps3230.ScreenScraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class ScreenScraperTest 
{
    WebDriver driver;

    @BeforeEach
    public void setup(){

        //TODO: AUTOMATICALLY FIGURE OUT THE SYSTEM AND SET THE CORRECT PROPERTY
        /*String osname = System.getProperty("os.name");
        if(osname.toLowerCase().contains("windows")){
            //FOR LINUX SYSTEM
            //System.setProperty("webdriver.chrome.driver", "/home/schimpf/webtesting/chromedriver");
        }else if(osname.toLowerCase().contains("linux")){
            //FOR WINDOWS
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
            driver = new ChromeDriver();
        }
        */

        //FOR LINUX SYSTEM
        //System.setProperty("webdriver.chrome.driver", "/home/schimpf/webtesting/chromedriver");

        //FOR WINDOWS
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }


    @Test
    public void testVisitAmazon(){
        //Visit amazon.com website
        ScreenScraper.visitAmazon(driver);

        //Verify that the title is the same title
        String title = driver.getTitle();
        assertEquals(title, "Amazon.com. Spend less. Smile more.");
    }

    @Test
    public void testVisitMarketAlert(){
        //Visit marketalertum.com website
        ScreenScraper.visitMarketAlert(driver);

        //Verify that the title is the same title
        String title = driver.getTitle();
        assertEquals(title, "Home Page - MarketAlertUM");
    }

    @Test
    public void testSearchAmazon(){
        ScreenScraper.visitAmazon(driver);
        ScreenScraper.searchAmazon(driver, "basketball");

        WebElement element = driver.findElement(By.name());
        String text = element.getText();
        assertEquals(text, "basketball");
    }

    @Test
    public void testScrapeAmazon(){
        assertTrue(false);
    }

    @Test
    public void testLogInMarketAlert(){
        assertTrue(false);
    }

    @Test
    public void testViewAlerts(){
        assertTrue(false);
    }

    @Test
    public void testLogOutMarketAlert(){
        assertTrue(false);
    }

    @Test
    public void testCreateAlert(){
        assertTrue(false);
    }

    public void testDeleteAlerts(){
        assertTrue(false);
    }
}
