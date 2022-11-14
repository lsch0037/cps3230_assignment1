package com.lsch0037.cps3230;

import com.lsch0037.cps3230.Pages.MarketAlertList;
import com.lsch0037.cps3230.Pages.MarketAlertHome;
import com.lsch0037.cps3230.Pages.MarketAlertLogin;
import com.lsch0037.cps3230.Pages.ScanHome;
import com.lsch0037.cps3230.Pages.ScanProduct;
import com.lsch0037.cps3230.Pages.ScanResults;


import org.openqa.selenium.WebDriver;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;

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
        
        this.driver = driver;

        scanHome = new ScanHome(driver);
        scanResults = new ScanResults(driver);
        scanProduct = new ScanProduct(driver);
        marketAlertHome = new MarketAlertHome(driver);
        marketAlertLogin = new MarketAlertLogin(driver);
        marketAlertList = new MarketAlertList(driver);
    }

    //run the screen scraper program
    public void run(String userId, String searchTerm, int numOfResults, int alertType){
        deleteAlerts(driver, userId);

        visitScan(driver); 
        searchScan(driver, scanHome, searchTerm);
        List<String> links = getResultLinks(driver, scanResults, numOfResults);

        JSONObject product;
        for (String link : links) {
            visitResult(driver, link);
            product = parseResult(driver, scanProduct, userId, alertType);
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

    //search scanmalta.com for the specified searchterm
    public void searchScan(WebDriver driver, ScanHome scanHome, String searchTerm){
        scanHome.search(searchTerm);
    }

    //get the links for the top results on scanmalta
    public List<String> getResultLinks(WebDriver driver, ScanResults scanResults, int numOfResults){
        return scanResults.getResultLinks(numOfResults);
    }

    //navigates to the page of the given product
    public void visitResult(WebDriver driver, String link){
        driver.get(link);
    }

    //find the neccessary details on the productpage and create a JSONObject with the neccessary items as products
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

    //go the the marketAlertUm login page
    public void goToLogIn(WebDriver driver, MarketAlertHome marketAlertHome){
        marketAlertHome.clickLogInButton();
    }

    //log in to marketAlertUm by inputting the user Id
    public void logIn(WebDriver driver, MarketAlertLogin marketAlertLogin, String userId){
        marketAlertLogin.inputUserId(userId);
        marketAlertLogin.submit();
    }

    /*
     * Attepts to post the json object to the api
     * Returns the response object
     * In case send fails return null
     */
    public HttpResponse postAlert(WebDriver driver, JSONObject json){

        HttpClient client = HttpClient.newHttpClient();
        
        //create post request out of json object
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json.toString()))
        .build();
        
        try{
            //attempt to send 
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            return res;     //return response 
        }catch(Exception e){
            return null;    //in case of fail return a null object
        }
    }

    /*
     * Sends a delete request with the given user Id to the marketAlertUm api
     */
    public int deleteAlerts(WebDriver driver, String userId){
        HttpClient client = HttpClient.newHttpClient();
        
        //create delete request
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert?userId=" + userId))
        .DELETE()
        .build();
        
        HttpResponse<String> response;
        try{
            //attempt to make send request
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            return -1;  //in case of exception return -1
        }

        //return the statuscode of the request
        return response.statusCode();
    }

    //return a list of alerts that are displayed on marketAlertUm
    public List<WebElement> getAlerts(WebDriver driver){
        return marketAlertList.getAlerts();
    }
}