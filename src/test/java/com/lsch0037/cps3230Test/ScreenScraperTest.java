package com.lsch0037.cps3230Test;

import com.lsch0037.cps3230.ScreenScraper;
import com.lsch0037.cps3230.AlertItem;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;
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
    public void testVisitScan(){
        //Visit amazon.com website
        ScreenScraper.visitScan(driver);

        //Verify that the title is the same title
        String title = driver.getTitle();
        assertEquals(title, "Computers Store Malta | SCAN");
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
        String searchTerm = "football";
        ScreenScraper.visitAmazon(driver);  //go to amazon
        ScreenScraper.searchAmazon(driver, searchTerm);   //search for "basketball"

        //Verify that the title is as expected when searching for a term
        String title = driver.getTitle();
        assertEquals(title, "Amazon.com : " + searchTerm);
    }

    @Test
    public void testSearchScan(){
        String searchTerm = "football";
        ScreenScraper.visitScan(driver);  //go to amazon
        ScreenScraper.searchScan(driver, searchTerm);   //search for "basketball"

        //Verify that the title is as expected when searching for a term
        String resultText = driver.findElement(By.className("page-title")).getText();
        assertEquals(resultText, "Search results for: '"+ searchTerm +"'");

    }
    

    /*
    @Test
    public void testGetTopResults(){
        ScreenScraper.visitAmazon(driver);  //go to amazon
        ScreenScraper.searchAmazon(driver, "basketball");   //search for "basketball"

        //get the top 10 search results
        List<AlertItem> results = ScreenScraper.getTopResults(driver, 10);


        //verify
        //list of results is not empty
        assertFalse(results.isEmpty());

        //for each result
        for (AlertItem result : results) {
            //assertTrue(result.getType() > 0 && result.getType() < 7);
            assertFalse(result.getTitle().isEmpty());
            assertFalse(result.getDescription().isEmpty());
            assertFalse(result.getUrl().isEmpty());
            assertFalse(result.getImageUrl().isEmpty());
            assertFalse(result.getPostedBy().isEmpty());
            assertTrue(result.getPriceInCents() > 0 );
        }
    }
    */
    @Test
    public void testGetResults(){
        String searchTerm = "basketball";
        int numOfResults = 10;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        List<WebElement> results = ScreenScraper.getResults(driver, numOfResults);
        
        assertFalse(results.isEmpty());
        // assertFalse(results.size() == numOfResults);
    }

    @Test
    public void testVisitResult(){
        String searchTerm = "razors";
        int numOfResults = 10;
        ScreenScraper.visitAmazon(driver);
        ScreenScraper.searchAmazon(driver, searchTerm);
        WebElement result = ScreenScraper.getResults(driver, numOfResults).get(0);
        
        ScreenScraper.visitResult(driver, result);

    }

    @Test
    public void testParseResult(){
        String searchTerm = "bike";
        int numOfResults = 3;
        ScreenScraper.visitAmazon(driver);
        ScreenScraper.searchAmazon(driver, searchTerm);
        List<WebElement> results = ScreenScraper.getResults(driver, numOfResults);

        AlertItem alert;
        for (WebElement result : results) {
            ScreenScraper.visitResult(driver, result);

            alert = ScreenScraper.parseResult(driver);
            assertFalse(alert.getTitle().isEmpty());
            assertFalse(alert.getDescription().isEmpty());
            assertFalse(alert.getUrl().isEmpty());
            assertFalse(alert.getImageUrl().isEmpty());
            assertTrue(alert.getPriceInCents() > 0);
        }

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
