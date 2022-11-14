package com.lsch0037.cps3230Test;

import com.lsch0037.cps3230.ScreenScraper;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;
import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.ScanHome;
import com.lsch0037.cps3230.Pages.ScanProduct;
import com.lsch0037.cps3230.Pages.ScanResults;

import com.lsch0037.cps3230.Constants;

import java.net.http.HttpResponse;
import java.util.List;

import org.json.JSONObject;
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

public class ScreenScraperTest 
{
    WebDriver driver;
    ScreenScraper screenScraper;
    ScanHome scanHome;
    ScanResults scanResults;
    ScanProduct scanProduct;
    MarketAlertHome marketAlertHome;
    MarketAlertLogin marketAlertLogin;
    MarketAlertList marketAlertList;

    @BeforeEach
    //runs before each test 
    public void setup(){

        //set path to chromedriver executable
        System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);

        driver = new ChromeDriver();                        //initialise ChromeDriver
        screenScraper = new ScreenScraper(driver);          //intiialise ScreenScraper
        //initialise all the page objects
        scanHome = new ScanHome(driver);
        scanResults = new ScanResults(driver);
        scanProduct = new ScanProduct(driver);
        marketAlertHome = new MarketAlertHome(driver);
        marketAlertLogin = new MarketAlertLogin(driver);
        marketAlertList = new MarketAlertList(driver);
    }

    @AfterEach
    //Teardown method which runs after each test
    public void teardown(){
        //quit WebDriver
        driver.quit();
    }

    @Test
    public void testVisitScan(){
        //Visit amazon.com website
        screenScraper.visitScan(driver);

        assertTrue(scanHome.isOnScanHomePage());
    }

    @Test
    public void testVisitMarketAlert(){
        //Visit marketalertum.com website
        screenScraper.visitMarketAlert(driver);

        //Verify that the driver has navigated to the MarketAlertUm home page
        assertTrue(marketAlertHome.isOnMarketAlertHome());
    }

    //returns search term randomly selected from among products that are offered by scanmalta
    //TODO: CONSIDER WHETHER RANDOM STRINGS ARE REQUIRED
    public String randomSearchTerm(){
        String searchTerms[] = {
            "Laptop",
            "Monitor",
            "Graphics Card",
            "Keyboard",
            "Mouse",
            "Headphones",
            "Speaker",
            "Tablet",
            "Router"
        };

        return searchTerms[randomInt(0, searchTerms.length)];
    }

    @Test
    public void testSearchScan(){
        String searchTerm = randomSearchTerm();     //generate random searchterm
        screenScraper.visitScan(driver);     //go to scanmalta
        screenScraper.searchScan(driver, scanHome, searchTerm);   //search for term

        //Verify that the driver is on the scan search results page and has found results
        assertTrue(scanResults.isOnResultsPage());
        assertTrue(scanResults.foundResults());
    }

    @Test 
    //TODO: CONSIDER WHETHER THIS TEST IS NECCESSARY
    public void testSearchScanForInvalidTerm(){
        screenScraper.visitScan(driver);
        
        //Search for empty
        screenScraper.searchScan(driver, scanHome, "");

        //verify that
        //we are not on the results page
        assertFalse(scanResults.isOnResultsPage());
    }

    @Test
    public void testGetResultLinks(){
        String searchTerm = randomSearchTerm();
        int numOfResults = randomInt(1, 10);

        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, searchTerm);
        List<String> links = screenScraper.getResultLinks(driver, scanResults, numOfResults);


        //Verify
        assertFalse(links.isEmpty());   //Links returned are not empty
        assertEquals(links.size(), numOfResults);   //The desired number of results is returned
    }

    @Test
    //Get result links of a product which has not results on scan
    public void testGetEmptyResults(){
        screenScraper.visitScan(driver);
        
        //Search scan for something that they do not offer
        screenScraper.searchScan(driver, scanHome, "pumpkin");

        //verify that
        assertFalse(scanResults.foundResults());    //no results were found
        assertTrue(scanResults.getResultLinks(10).isEmpty());   //result links are empty
    }

    @Test
    //Get result links of more results than are displayed
    public void testGetTooManyResults(){
        screenScraper.visitScan(driver);
        
        screenScraper.searchScan(driver, scanHome, "playstation");

        List<String> links = screenScraper.getResultLinks(driver, scanResults, 100);

        assertTrue(links.size() < 100); 
    }

    @Test
    public void testVisitProduct(){
        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, randomSearchTerm());
        String link = screenScraper.getResultLinks(driver, scanResults, 1).get(0);

        screenScraper.visitResult(driver, link);

        assertTrue(scanProduct.isOnProductPage());
    }

    @Test
    public void testParseResult(){
        String searchTerm = randomSearchTerm();
        // String searchTerm = "Laptop";
        int alertType = randomInt(1, 6);

        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, searchTerm);

        String link = screenScraper.getResultLinks(driver, scanResults, 1).get(0);
        screenScraper.visitResult(driver, link);

        JSONObject product = screenScraper.parseResult(driver, scanProduct, Constants.USERID, alertType);

        assertNotNull(product.get("heading"));
        assertFalse(((String)product.get("heading")).isBlank());
        
        assertNotNull(product.get("description"));
        assertFalse(((String)product.get("description")).isBlank());

        assertNotNull(product.get("url"));
        assertFalse(((String)product.get("url")).isBlank());

        assertNotNull(product.get("imageUrl"));
        assertFalse(((String)product.get("imageUrl")).isBlank());

        assertEquals(((String)product.get("postedBy")), Constants.USERID);

        assertTrue((int)product.get("priceInCents") > 0);
    }

    @Test
    public void testGoToLogIn(){
        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);

        assertTrue(marketAlertLogin.isOnLogInPage());
    }

    //logs into marketAlertUm with the given username
    public void logIn(){
        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);
        screenScraper.logIn(driver, marketAlertLogin, Constants.USERID);
    }

    @Test
    public void testLogIn(){
        //log in to marketAlertUm
        logIn();

        assertTrue(marketAlertList.isOnAlertsPage());
    }

    @Test
    public void testLogInInvalidId(){
        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);

        screenScraper.logIn(driver, marketAlertLogin, "invalidId");

        assertFalse(marketAlertList.isOnAlertsPage());
    }

    //returns a random number between max and min
    public int randomInt(int min, int max){
        return (int) (Math.random()* (max - min) + min);
    }

    //generates a new AlertItem with each attribute randomly selected
    //TODO: MAKE THE DATA IN THE OBJECT TRULY RANDOM
    public JSONObject randomProduct(){
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

        JSONObject product = new JSONObject();
        product.put("alertType", randomInt(0, 6));
        product.put("heading", possibleHeadings[randomInt(0, 3)]);
        product.put("description", possibleDescriptions[randomInt(0, 3)]);
        product.put("url", possibleUrls[randomInt(0, 3)]);
        product.put("imageUrl", possibleImageUrls[randomInt(0, 3)]);
        product.put("postedBy", Constants.USERID);
        product.put("priceInCents", randomInt(0, 1000000));
        
        return product;
    }

    @Test
    public void testPostProduct(){
        JSONObject product = randomProduct();

        //post the alert
        HttpResponse<String> response = screenScraper.postAlert(driver, product);

        //verify that
        assertNotNull(response);    //send was successful
        assertEquals(response.statusCode(), 201);   //statusCode is 201
    }

    @Test 
    public void testGetAlerts(){
        screenScraper.deleteAlerts(driver, Constants.USERID);
        screenScraper.postAlert(driver, randomProduct());
        logIn();

        List<WebElement> alerts = screenScraper.getAlerts(driver);

        assertFalse(alerts.isEmpty());
    }

    @Test 
    public void testGetEmptyAlerts(){
        screenScraper.deleteAlerts(driver, Constants.USERID);
        logIn();

        List<WebElement> alerts = screenScraper.getAlerts(driver);

        assertTrue(alerts.isEmpty());
    }

    @Test
    public void testDeleteAlerts(){
        
        //post alert to marketAlertUm
        testPostProduct();

        //delete all alerts
        screenScraper.deleteAlerts(driver, Constants.USERID);

        //verify that there are no alerts on marketAlertUm
        logIn();
        List<WebElement> alerts = screenScraper.getAlerts(driver);
        assertTrue(alerts.isEmpty());    //verify there are no alerts
    }

    @Test
    //TODO: CONSIDER REMOVING
    public void testDeleteEmptyAlerts(){

        //delete all alerts
        screenScraper.deleteAlerts(driver, Constants.USERID);

        //verify that there are no alerts on marketAlertUm
        logIn();
        List<WebElement> alerts = screenScraper.getAlerts(driver);
        assertTrue(alerts.isEmpty());    //verify there are no alerts
    }
    
    @Test
    public void testLogOutMarketAlert(){
        assertTrue(false);
    }
}
