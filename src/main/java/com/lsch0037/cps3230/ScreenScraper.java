package com.lsch0037.cps3230;

import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;
import com.lsch0037.cps3230.Pages.PageObject;
import com.lsch0037.cps3230.Pages.ScanHome;
import com.lsch0037.cps3230.Pages.ScanProduct;
import com.lsch0037.cps3230.Pages.ScanResults;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import java.util.List;

public class ScreenScraper 
{
    public static void main( String[] args ) throws Exception {
        // System.out.println("Hello World");

        System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);

        WebDriver driver = new ChromeDriver();
        ScanHome scanHome = new ScanHome(driver);
        ScanResults scanResults = new ScanResults(driver);
        ScanProduct scanProduct = new ScanProduct(driver);
        MarketAlertHome marketAlertHome = new MarketAlertHome(driver);
        MarketAlertLogin marketAlertLogin = new MarketAlertLogin(driver);
        MarketAlertList marketAlertList = new MarketAlertList(driver);

        deleteAlerts(driver, Constants.USERID);

        visitScan(driver); 
        searchScan(driver, scanHome, "macbook");
        List<String> links = getResultLinks(driver, scanResults, 5);

        JSONObject product;
        for (String link : links) {
            visitResult(driver, link);
            product = parseResult(driver, scanProduct, Constants.USERID, 1);
            postAlert(driver, product);
        }

        driver.quit();
    }

    //nagivates to the amazon website
    public static void visitScan(WebDriver driver){
        //visit amazon.com website
        driver.get("https://www.scanmalta.com");
    }

    //nagivates to the marketalertum website
    public static void visitMarketAlert(WebDriver driver){
        //visit marketalertum.com website
        driver.get("https://www.marketalertum.com/");
    }

    public static void searchScan(WebDriver driver, ScanHome scanHome, String searchTerm){
        scanHome.search(searchTerm);
    }

    public static List<String> getResultLinks(WebDriver driver, ScanResults scanResults, int numOfResults){
        return scanResults.getResultLinks(numOfResults);
    }

    //navigates to the page of the given product
    public static void visitResult(WebDriver driver, String link){
        driver.get(link);
    }

    public static JSONObject parseResult(WebDriver driver, ScanProduct scanProduct, String userId, int alertType){
        JSONObject object = new JSONObject();
        object.put("alertType", alertType);
        object.put("heading", scanProduct.getHeading());
        object.put("description", scanProduct.getDescription());
        object.put("url", scanProduct.getUrl());
        object.put("imageUrl", scanProduct.getImageUrl());
        object.put("postedBy", userId);
        object.put("priceInCents", scanProduct.getPriceInCents());

        return object;
    }

    public static void goToLogIn(WebDriver driver, MarketAlertHome marketAlertHome){
        marketAlertHome.clickLogInButton();
    }

    public static void logIn(WebDriver driver, MarketAlertLogin marketAlertLogin, String userId){
        marketAlertLogin.inputUserId(userId);
        marketAlertLogin.submit();
    }

    //attempts to post the json object to the api
    //returns the statusCode of the response or -1 upon failure to send
    public static HttpResponse postAlert(WebDriver driver, JSONObject json){

        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json.toString()))
        .build();
        
        try{
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            return res;
        }catch(Exception e){
            return null;
        }
    }

    public static int deleteAlerts(WebDriver driver, String username){
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert?userId=" + username))
        .DELETE()
        .build();
        
        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            return -1;
        }

        return response.statusCode();
    }

    public static List<WebElement> getAlerts(WebDriver driver){
        //refresh to ensure the information is up to date
        driver.navigate().refresh();

        return driver.findElements(By.tagName("table"));
    }

    public static void logOutMarketAlert(WebDriver driver){

    }
}