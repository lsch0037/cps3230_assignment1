package com.lsch0037.cps3230Test;

import com.lsch0037.cps3230.ScreenScraper;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;
import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.ScanHome;
import com.lsch0037.cps3230.Pages.ScanProduct;
import com.lsch0037.cps3230.Pages.ScanResults;

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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class ScreenScraperTest 
{
    WebDriver driver;
    ScanHome scanHome;
    ScanResults scanResults;
    ScanProduct scanProduct;
    MarketAlertHome marketAlertHome;
    MarketAlertLogin marketAlertLogin;
    MarketAlertList marketAlertList;

    //TODO: REMOVE THIS BEFORE RELEASE
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
        scanHome = new ScanHome(driver);
        scanResults = new ScanResults(driver);
        scanProduct = new ScanProduct(driver);
        marketAlertHome = new MarketAlertHome(driver);
        marketAlertLogin = new MarketAlertLogin(driver);
        marketAlertList = new MarketAlertList(driver);
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }

    @Test
    public void testVisitScan(){
        //Visit amazon.com website
        ScreenScraper.visitScan(driver);

        //TODO: REPLACE WITH scanHome.isOnScanHomePage()
        assertTrue(scanHome.isOnScanHomePage());
    }

    @Test
    public void testVisitMarketAlert(){
        //Visit marketalertum.com website
        ScreenScraper.visitMarketAlert(driver);

        //Verify that the title is the same title
        String title = driver.getTitle();
        //TODO: REPLACE WITH marketAlertHome.isOnMarketAlertHome()
        assertEquals(title, "Home Page - MarketAlertUM");
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
        // ScanHome scanHome = new ScanHome(driver);
        String searchTerm = randomSearchTerm();
        ScreenScraper.visitScan(driver);  //go to scanmalta
        ScreenScraper.searchScan(driver, scanHome, searchTerm);   //search for random search term

        //TODO: REPLACE WITH scanResutls.isOnResultsPage()
        ScanResults scanResults = new ScanResults(driver);
        assertTrue(scanResults.foundResults());
        
    }

    @Test 
    public void testSearchScanForInvalidTerm(){
        ScreenScraper.visitScan(driver);
        
        //Search for empty
        ScreenScraper.searchScan(driver, scanHome, "");

        //verify that
        //we are not on the results page
        assertFalse(scanResults.isOnResultsPage());
    }

    @Test
    public void testGetResultLinks(){
        String searchTerm = randomSearchTerm();
        int numOfResults = randomInt(1, 10);

        // ScanResults scanResults = new ScanResults(driver);
        // ScanHome scanHome = new ScanHome(driver);

        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, scanHome, searchTerm);
        List<String> links = ScreenScraper.getResultLinks(driver, scanResults, numOfResults);


        //Verify
        assertFalse(links.isEmpty());   //Links returned are not empty
        assertEquals(links.size(), numOfResults);   //The desired number of results is returned
    }

    @Test
    //Get result links of a product which has not results on scan
    public void testGetEmptyResults(){
        ScreenScraper.visitScan(driver);
        
        //Search scan for something that they do not offer
        ScreenScraper.searchScan(driver, scanHome, "pumpkin");

        //verify that
        assertFalse(scanResults.foundResults());    //no results were found
        assertTrue(scanResults.getResultLinks(10).isEmpty());   //result links are empty

    }

    @Test
    //Get result links of more results than are displayed
    public void testGetTooManyResults(){
        ScreenScraper.visitScan(driver);
        
        ScreenScraper.searchScan(driver, scanHome, "playstation");

        List<String> links = ScreenScraper.getResultLinks(driver, scanResults, 100);

        assertTrue(links.size() < 100); 
    }

    @Test
    public void testVisitProduct(){
        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, scanHome, randomSearchTerm());
        String link = ScreenScraper.getResultLinks(driver, scanResults, 1).get(0);

        ScreenScraper.visitResult(driver, link);

        assertTrue(scanProduct.isOnProductPage());
    }

    @Test
    public void testParseResult(){
        String searchTerm = randomSearchTerm();
        int alertType = randomInt(1, 6);

        ScreenScraper.visitScan(driver);
        ScreenScraper.searchScan(driver, scanHome, searchTerm);
        String link = ScreenScraper.getResultLinks(driver, scanResults, 1).get(0);
        ScreenScraper.visitResult(driver, link);

        //TODO: FIGURE OUT WHY THIS FAILS SOMETIMES
        JSONObject product = ScreenScraper.parseResult(driver, scanProduct, username, alertType);

        assertNotNull(product.get("heading"));
        assertFalse(((String)product.get("heading")).isBlank());
        
        assertNotNull(product.get("description"));
        assertFalse(((String)product.get("description")).isBlank());

        assertNotNull(product.get("url"));
        assertFalse(((String)product.get("url")).isBlank());

        assertNotNull(product.get("imageUrl"));
        assertFalse(((String)product.get("imageUrl")).isBlank());

        assertEquals(((String)product.get("postedBy")), username);

        assertTrue((int)product.get("priceInCents") > 0);
    }

    @Test
    //TODO: CONSIDER WHETHER THIS IS NECCESSARY
    public void testParseResultInvalidPage(){
        assertTrue(false);
    }

    @Test
    public void testGoToLogIn(){
        ScreenScraper.visitMarketAlert(driver);
        ScreenScraper.goToLogIn(driver, marketAlertHome);

        // assertEquals(driver.getCurrentUrl(), "https://www.marketalertum.com/Alerts/Login");
        assertTrue(marketAlertLogin.isOnLogInPage());
    }

    //logs into marketAlertUm with the given username
    public void logIn(){
        ScreenScraper.visitMarketAlert(driver);
        ScreenScraper.goToLogIn(driver, marketAlertHome);
        ScreenScraper.logIn(driver, marketAlertLogin, username);
    }

    @Test
    public void testLogIn(){
        //log in to marketAlertUm
        logIn();

        assertTrue(marketAlertList.isOnAlertsPage());
    }

    @Test
    public void testLogInInvalidId(){
        ScreenScraper.visitMarketAlert(driver);
        ScreenScraper.goToLogIn(driver, marketAlertHome);

        ScreenScraper.logIn(driver, marketAlertLogin, "invalidId");

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
        product.put("postedBy", username);
        product.put("priceInCents", randomInt(0, 1000000));
        
        return product;
    }

    @Test
    public void testPostProduct(){
        JSONObject product = randomProduct();

        //post the alert
        HttpResponse<String> response = ScreenScraper.postAlert(driver, product);

        //verify that
        assertNotNull(response);    //send was successful
        assertEquals(response.statusCode(), 201);   //statusCode is 201
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

        JSONObject product = randomProduct();
        ScreenScraper.postAlert(driver, product);
        logIn();

        
        WebElement alert = ScreenScraper.getAlerts(driver).get(0);
        List<WebElement> trs = alert.findElements(By.tagName("tr"));
        
        //heading
        String heading = trs.get(0).getText();

        //imageUrl
        String imageUrl = trs.get(1)
            .findElement(By.tagName("img"))
            .getAttribute("src");

        //description
        String description = trs.get(2).getText();

        //price
        String priceInText = trs.get(3).getText();
        String price = priceInText.split("€")[1];
        String priceCleaned = price.replace(".", "")
        .replace("\"", "");

        //url
        String url = trs.get(4)
        .findElement(By.tagName("a"))
        .getAttribute("href");

        
        //TODO: SOMEHOW TEST ALERTTYPE
        // assertTrue(heading.startsWith(product.getHeading(), 0));
        // assertEquals(product.getDescription(), description);
        // assertEquals(product.getUrl(), url);
        // assertEquals(product.getImageUrl(), imageUrl);
        // assertEquals(Integer.toString(product.getPriceInCents()), priceCleaned);

        // assertEquals(, null);
    }

    @Test 
    public void testGetEmptyAlerts(){
        assertTrue(false);
    }

    @Test
    public void testDeleteAlerts(){
        
        //post alert to marketAlertUm
        testPostProduct();

        //delete all alerts
        ScreenScraper.deleteAlerts(driver, username);

        //verify that there are no alerts on marketAlertUm
        List<WebElement> alertElements = getAlerts();   //get alerts
        assertTrue(alertElements.isEmpty());    //verify there are no alerts
    }

    @Test
    public void testDeleteEmptyAlerts(){
        assertTrue(false);
    }
    
    @Test
    public void testLogOutMarketAlert(){
        assertTrue(false);
    }
}
