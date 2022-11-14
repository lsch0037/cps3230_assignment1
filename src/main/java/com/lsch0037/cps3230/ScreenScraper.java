package com.lsch0037.cps3230;

import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;
import com.lsch0037.cps3230.Pages.ScanHome;
import com.lsch0037.cps3230.Pages.ScanProduct;
import com.lsch0037.cps3230.Pages.ScanResults;

import io.cucumber.java.it.Ma;

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
    private WebDriver driver;
    private ScanHome scanHome;
    private ScanResults scanResults;
    private ScanProduct scanProduct;
    private MarketAlertHome marketAlertHome;
    private MarketAlertLogin marketAlertLogin;
    private MarketAlertList marketAlertList;

    public ScreenScraper(WebDriver driver){
        System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);

        scanHome = new ScanHome(driver);
        scanResults = new ScanResults(driver);
        scanProduct = new ScanProduct(driver);
        marketAlertHome = new MarketAlertHome(driver);
        marketAlertLogin = new MarketAlertLogin(driver);
        marketAlertList = new MarketAlertList(driver);
        
    }

    public void run(){
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
    public void visitScan(WebDriver driver){
        //visit amazon.com website
        driver.get("https://www.scanmalta.com");
    }

    //nagivates to the marketalertum website
    public void visitMarketAlert(WebDriver driver){
        //visit marketalertum.com website
        driver.get("https://www.marketalertum.com/");
    }

    public void searchScan(WebDriver driver, ScanHome scanHome, String searchTerm){
        scanHome.search(searchTerm);
    }

    public List<String> getResultLinks(WebDriver driver, ScanResults scanResults, int numOfResults){
        return scanResults.getResultLinks(numOfResults);
    }

    //navigates to the page of the given product
    public void visitResult(WebDriver driver, String link){
        driver.get(link);
    }

    public JSONObject parseResult(WebDriver driver, ScanProduct scanProduct, String userId, int alertType){
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

    public void goToLogIn(WebDriver driver, MarketAlertHome marketAlertHome){
        marketAlertHome.clickLogInButton();
    }

    public void logIn(WebDriver driver, MarketAlertLogin marketAlertLogin, String userId){
        marketAlertLogin.inputUserId(userId);
        marketAlertLogin.submit();
    }

    //attempts to post the json object to the api
    public HttpResponse postAlert(WebDriver driver, JSONObject json){

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

    //TODO: MAKE THIS RETURN THE RESPONSE OBJECT AND NOT THE STATUS
    public int deleteAlerts(WebDriver driver, String username){
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

    public List<WebElement> getAlerts(WebDriver driver){

        return marketAlertList.getAlerts();
    }

    public void logOutMarketAlert(WebDriver driver){
        //TODO: IMPLEMENT
    }
}