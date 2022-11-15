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
    //Get result links of a product which has no results on scan
    public void testSearchScanForInvalidTerm(){
        screenScraper.visitScan(driver);
        
        //Search scan for something that they do not offer
        screenScraper.searchScan(driver, scanHome, "pumpkin");

        //verify that
        assertFalse(scanResults.foundResults());    //no results were found
        assertTrue(scanResults.getResultLinks(10).isEmpty());
    }

    @Test
    //Get results links of a product that has results on scan
    public void testGetResultLinks(){
        String searchTerm = randomSearchTerm();     //generate random search term
        int numOfResults = randomInt(1, 10);    //generate random number of results

        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, searchTerm);     //search for searchterm
        List<String> links = screenScraper.getResultLinks(driver, scanResults, numOfResults);   //get results


        //Verify
        assertFalse(links.isEmpty());   //Links returned are not empty
        assertEquals(links.size(), numOfResults);   //The desired number of results is returned
    }

    @Test
    //Get result links of a product which has no results on scan
    public void testGetEmptyResultsLinks(){
        screenScraper.visitScan(driver);
        
        //Search scan for something that they do not offer
        screenScraper.searchScan(driver, scanHome, "pumpkin");

        //verify that
        assertFalse(scanResults.foundResults());    //no results were found
        assertTrue(scanResults.getResultLinks(10).isEmpty());   //result links are empty
    }

    @Test
    //Get result links of more results than are displayed
    public void testGetTooManyResultLinks(){
        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, "playstation");  //search scan

        //attempt to get more results than fit on a page
        List<String> links = screenScraper.getResultLinks(driver, scanResults, 100);

        //verify that the actual number of results was returned and not the expected
        assertTrue(links.size() < 100); 
        assertFalse(links.isEmpty());   //links are also not empty
    }

    @Test
    public void testVisitProduct(){
        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, randomSearchTerm());     //search scan
        String link = screenScraper.getResultLinks(driver, scanResults, 1).get(0);  //get first result

        screenScraper.visitResult(driver, link);    //go to the first result

        //verify that the driver navigated to the product page
        assertTrue(scanProduct.isOnProductPage());
    }

    @Test
    public void testParseResult(){
        String searchTerm = randomSearchTerm();     //generate random search term
        int alertType = randomInt(1, 6);    //generate random alert type

        
        screenScraper.visitScan(driver);
        screenScraper.searchScan(driver, scanHome, searchTerm);
        String link = screenScraper.getResultLinks(driver, scanResults, 1).get(0);
        screenScraper.visitResult(driver, link);    //go to a result product page

        //parse into JSONObject
        JSONObject product = screenScraper.parseResult(driver, scanProduct, Constants.USERID, alertType);

        //verify that:

        //JSONObject exists and has non-blank heading
        assertNotNull(product.get("heading"));
        assertFalse(((String)product.get("heading")).isBlank());
        
        //JSONObject exists and has non-blank description
        assertNotNull(product.get("description"));
        assertFalse(((String)product.get("description")).isBlank());

        //JSONObject exists and has non-blank url
        assertNotNull(product.get("url"));
        assertFalse(((String)product.get("url")).isBlank());

        //JSONObject exists and has non-blank imageUrl
        assertNotNull(product.get("imageUrl"));
        assertFalse(((String)product.get("imageUrl")).isBlank());

        //JSONObject exists and has non-blank postedby
        assertNotNull(product.get("postedBy"));
        assertEquals(((String)product.get("postedBy")), Constants.USERID);

        //JSONObject exists and has non-blank price
        assertNotNull(product.get("priceInCents"));
        assertTrue((int)product.get("priceInCents") > 0);
    }

    @Test
    //Navigate to the login page from the marketAlertUm home page
    public void testGoToLogIn(){
        //Go to marketAlertUm home
        screenScraper.visitMarketAlert(driver);

        //Go to login page
        screenScraper.goToLogIn(driver, marketAlertHome);

        //verify that it is on login page
        assertTrue(marketAlertLogin.isOnLogInPage());
    }

    /*
     * This function exists to avoid the repetition of all the steps needed to log in to marketAlertUm
     * It visits the home page, navigates to the login page, and attempts to log in using the user id
     */
    public void logIn(){
        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);
        screenScraper.logIn(driver, marketAlertLogin, Constants.USERID);
    }

    @Test
    //Log in to marketAlertUm given a correct userId
    public void testLogIn(){
        //log in to marketAlertUm
        logIn();

        //verify that the driver has successfully logged in and is on the alerts page
        assertTrue(marketAlertList.isOnAlertsPage());
    }

    @Test
    //Attempt to login to marketAlertUm given an incorrect userId
    public void testLogInInvalidId(){
        //navigate to login page
        screenScraper.visitMarketAlert(driver);
        screenScraper.goToLogIn(driver, marketAlertHome);

        //attempt to log in using an invalid user Id
        screenScraper.logIn(driver, marketAlertLogin, "invalidId");

        //verify that driver is not on the alerts page but remains on the login page
        assertFalse(marketAlertList.isOnAlertsPage());
        assertTrue(marketAlertLogin.isOnLogInPage());
    }

    /*
     * This method exists to simplify the generation of random integers
     * returns a random integer between and including min and max
     */
    public int randomInt(int min, int max){
        return (int) (Math.random()* (max - min) + min);
    }

    /*
     * This method exists to generate a JSONObject with the necessary attributes to post as an alert to the api
     * The attributes that are strings are randomly selected from a list of real examples of that attribute
     * The posted by attribute is set to the UserId specified in Constants.java
     * The price in cents is a random integer between 0 and 1000000
     * The alert type is a random integer between 1 and 6 (including both)
     * Returns the JSONObject with all the attributes set
     */
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
        //select the attriubtes randomly
        product.put("alertType", randomInt(0, 6));
        product.put("heading", possibleHeadings[randomInt(0, 3)]);
        product.put("description", possibleDescriptions[randomInt(0, 3)]);
        product.put("url", possibleUrls[randomInt(0, 3)]);
        product.put("imageUrl", possibleImageUrls[randomInt(0, 3)]);
        product.put("postedBy", Constants.USERID);                       //set to real user id
        product.put("priceInCents", randomInt(0, 1000000));     //price is a random number between €0 to €10,000
        
        return product;     //return product with random attriubtes
    }

    @Test
    //post an alert to the marketAlertUm api
    public void testPostProduct(){
        //generate random product
        JSONObject product = randomProduct();

        //post the alert
        HttpResponse<String> response = screenScraper.postAlert(driver, product);

        //verify that
        assertNotNull(response);    //send was successful
        assertEquals(response.statusCode(), 201);   //statusCode is 201
    }

    @Test 
    //retrieve a product from the marketAlertUm web page
    public void testGetAlerts(){
        //first delete all alerts and then post one random product
        screenScraper.deleteAlerts(driver, Constants.USERID);
        screenScraper.postAlert(driver, randomProduct());
        logIn();    //log in to marketAlertUm

        //get the list of alerts displayed
        List<WebElement> alerts = screenScraper.getAlerts(driver);

        //verify that an item was retrieved
        assertFalse(alerts.isEmpty());
    }

    @Test 
    //attempt to retrive a product from the marketAlertUm web page when there are no items
    public void testGetEmptyAlerts(){
        //delete all alerts to ensure page is empty
        screenScraper.deleteAlerts(driver, Constants.USERID);
        logIn();    //log in to marketAlertUm

        //get the list of alerts displayed
        List<WebElement> alerts = screenScraper.getAlerts(driver);

        //verify that no items were retrieved
        assertTrue(alerts.isEmpty());
    }

    @Test
    //delete an alert from marketAlertUm
    public void testDeleteAlerts(){
        
        //post alert to marketAlertUm
        testPostProduct();

        //delete all alerts
        int statusCode = screenScraper.deleteAlerts(driver, Constants.USERID);
        
        //verify statusCode indicates successful delete
        assertEquals(statusCode, 201);
    }

    @Test
    //delete and alert from marketAlertUm when the page is already empty
    public void testDeleteEmptyAlerts(){
        //ensure that alerts are empty
        screenScraper.deleteAlerts(driver, Constants.USERID);

        //delete all alerts
        int statusCode = screenScraper.deleteAlerts(driver, Constants.USERID);

        //verify statusCode indicates successful delete
        assertEquals(statusCode, 201);
    }
}
