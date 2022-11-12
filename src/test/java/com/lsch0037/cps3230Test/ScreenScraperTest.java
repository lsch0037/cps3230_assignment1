package com.lsch0037.cps3230Test;

import com.lsch0037.cps3230.ScreenScraper;

import io.netty.handler.codec.http.HttpResponse;

import com.lsch0037.cps3230.AlertItem;

import java.nio.channels.AlreadyConnectedException;
import java.security.DrbgParameters.Reseed;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginContext;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class ScreenScraperTest 
{
    WebDriver driver;
    String username = "21ed7a53-ff36-4daf-8da0-c8b66b11c0de";

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
    public void testSearchScan(){
        String searchTerm = "football";
        ScreenScraper.visitScan(driver);  //go to amazon
        boolean success = ScreenScraper.searchScan(driver, searchTerm);   //search for "basketball"

        assertTrue(success);

        //Verify that the title is as expected when searching for a term
        String resultText = driver.findElement(By.className("page-title")).getText();
        assertEquals(resultText, "Search results for: '"+ searchTerm +"'");

    }

    @Test
    public void testGetResults(){
        String searchTerm = "headphones";
        int numOfResults = 10;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        List<WebElement> results = ScreenScraper.getResults(driver, numOfResults);
        
        assertFalse(results.isEmpty());
        assertFalse(results.size() == numOfResults);
    }

    @Test
    public void testGetEmptyResults(){
        String searchTerm = "basketball";   //empty search term
        int numOfResults = 10;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        List<WebElement> results = ScreenScraper.getResults(driver, numOfResults);
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetTooManyResults(){
        String searchTerm = "usb stick";   //empty search term
        int numOfResults = 30;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        List<WebElement> results = ScreenScraper.getResults(driver, numOfResults);
        
        //will return only as many products as there are
        assertNotEquals(results.size(), numOfResults);
    }

    @Test
    public void testVisitResult(){
        String searchTerm = "case";
        int numOfResults = 2;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        WebElement result = ScreenScraper.getResults(driver, numOfResults).get(0);
        ScreenScraper.visitResult(driver, result);

        //verify this element exists, it only exists on a product page
        assertNotNull(driver.findElement(By.className("product-info-main")));
    }

    @Test
    public void testParseResult(){
        String searchTerm = "phone";
        int numOfResults = 3;
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, searchTerm);
        WebElement result = ScreenScraper.getResults(driver, numOfResults).get(0);

        AlertItem alert;
        ScreenScraper.visitResult(driver, result);

        alert = ScreenScraper.parseResult(driver);
        assertFalse(alert.getTitle().isEmpty());
        assertFalse(alert.getDescription().isEmpty());
        assertFalse(alert.getUrl().isEmpty());
        assertFalse(alert.getImageUrl().isEmpty());
        assertTrue(alert.getPriceInCents() > 0);
    }

    @Test
    public void testGoToLogIn(){
        ScreenScraper.visitMarketAlert(driver);
        ScreenScraper.goToLogIn(driver);

        WebElement form = driver.findElement(By.tagName("form"));
        
        assertNotNull(form);

        assertEquals(form.getText(), "User ID:");
    }

    //logs into marketAlertUm with the given username
    public void logIn(){
        ScreenScraper.visitMarketAlert(driver);
        ScreenScraper.goToLogIn(driver);
        ScreenScraper.logIn(driver, username);
    }

    @Test
    public void testLogIn(){
        //log in to marketAlertUm
        logIn();

        //test that the "Latest Alerts for <User Name>" message appears
        String heading = driver.findElement(By.tagName("main")).getText();
        assertTrue(heading.contains("Latest alerts for "));
    }

    //returns a random number between max and min
    public int randomInt(int min, int max){
        return (int) (Math.random()* (max - min) + min);
    }

    //generates a new AlertItem with each attribute randomly selected
    //TODO: MAKE THE DATA IN THE OBJECT TRULY RANDOM
    public AlertItem randomAlert(){
        String possibleHeadings[] = {
            "Apple MacBook Air 13\" Retina M1 Chip 256GB SSD 8GB RAM Gold (Ex Display)",
            "Apple iPhone SE (2022) 256GB (PRODUCT)RED",
            "JBL Flip Essential 2 Waterproof Wireless Bluetooth Speaker",
            "50\" Xiaomi Mi TV P1 4K Android Smart TV"
        };

        String possibleDescriptions[] = {
            "Apple-designed M1 chip for a giant leap in CPU, GPU, and machine learning performance",
            "4.7-inch Retina HD display",
            "Take your tunes on the go with the powerful JBL Flip Essential 2. Our lightweight Bluetooth speaker goes anywhere. Bad weather? Not to worry.",
            "Sharp details with 4K UHD resolution, Dolby Vision and HDR10+"
        };

        String possibleUrls[] = {
            "https://www.scanmalta.com/shop/apple-macbook-air-13-retina-m1-chip-256gb-ssd-8gb-ram-gold-2021-ex-display.html",
            "https://www.scanmalta.com/shop/apple-iphone-se-2022-256gb-product-red.html",
            "https://www.scanmalta.com/shop/jbl-flip-essential-2-waterproof-wireless-bluetooth-speaker.html",
            "https://www.scanmalta.com/shop/50-xiaomi-mi-tv-p1-4k-android-smart-tv.html"
        };

        String possibleImageUrls[] = {
            "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/a/p/apple-macbook-air-gold_2.png",
            "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/a/p/apple-iphone-se-2022-64gb-product-red_2.jpg",
            "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/3/_/3_jbl_flip_essential_front_36365_x1.jpg",
            "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/7/c/7c274328-be64-4f90-95cb-72be5e22177e_1_1.jpg"
        };

        AlertItem alert = new AlertItem();
        alert.setTitle(possibleHeadings[randomInt(0, 3)]);
        alert.setDescription(possibleDescriptions[randomInt(0, 3)]);
        alert.setUrl(possibleUrls[randomInt(0, 3)]);
        alert.setImageUrl(possibleImageUrls[randomInt(0, 3)]);
        alert.setPriceInCents(randomInt(0, 1000000));
        
        return alert;
    }

    @Test
    public void testAlertToJson(){

        AlertItem alert = randomAlert();
        JSONObject json = ScreenScraper.alertToJson(alert, 0, username);

        assertEquals(0, json.get("alertType"));
        assertEquals(alert.getTitle(), json.get("heading"));
        assertEquals(alert.getDescription(), json.get("description"));
        assertEquals(alert.getUrl(), json.get("url"));
        assertEquals(alert.getImageUrl(), json.get("imageUrl"));
        assertEquals(username, json.get("postedBy"));
        assertEquals(alert.getPriceInCents(), json.get("priceInCents"));
    }

    @Test
    public void testPostAlert(){
        AlertItem randomAlert = randomAlert();        
        JSONObject jsonAlert = ScreenScraper.alertToJson(randomAlert, randomInt(1, 6), username);

        int statusCode = ScreenScraper.postAlert(driver, jsonAlert);
        assertEquals(statusCode, 201);

        // TODO: CHECK THE ACTUAL VALUES OF THE RESPONSE OBJECT
    }

    //returns all the alerts currently on the marketAlertUm website
    //returns empty list if there are no alerts
    public List<WebElement> getAlerts(){
        //log into marketAlertUm
        logIn();

        //refresh to ensure the information is up to date
        driver.navigate().refresh();

        return driver.findElements(By.tagName("table"));
    }

    @Test
    public void testGetAlerts(){
        ScreenScraper.deleteAlerts(driver, username);

        AlertItem item = randomAlert();
        JSONObject jsonAlert = ScreenScraper.alertToJson(item, randomInt(1, 6), username);
    
        ScreenScraper.postAlert(driver, jsonAlert);

        logIn();

        WebElement alert = ScreenScraper.getAlerts(driver).get(0);

        List<WebElement> trs = alert.findElements(By.tagName("tr"));
        String heading = trs.get(0).getText();
        String imageUrl = trs.get(0)
            .findElement(By.tagName("img"))
            .getAttribute("src");
        String description = trs.get(2).getText();
        String priceInText = trs.get(3).getText();
        String url = trs.get(4)
        .findElement(By.tagName("a"))
        .getAttribute("href");

        assertEquals(item.getTitle(), heading);
        assertEquals(item.getDescription(), description);
        assertEquals(item.getUrl(), url);
        assertEquals(item.getImageUrl(), imageUrl);
        // assertTrue(priceInText.contains(item.getPriceInCents().toString());
        assertEquals(item.getTitle(), heading);
    }

    @Test
    public void testDeleteAlerts(){
        
        //post alert to marketAlertUm
        testPostAlert();

        //delete all alerts
        ScreenScraper.deleteAlerts(driver, username);

        //verify that there are no alerts on marketAlertUm
        List<WebElement> alertElements = getAlerts();   //get alerts
        assertTrue(alertElements.isEmpty());    //verify there are no alerts
    }
    
    @Test
    public void testLogOutMarketAlert(){
        assertTrue(false);
    }

}
