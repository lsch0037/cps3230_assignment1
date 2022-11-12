package com.lsch0037.cps3230;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.json.Json;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;
import java.util.LinkedList;

public class ScreenScraper 
{
    public static void main( String[] args ) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        getResults(driver, 5);

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

    public static boolean searchScan(WebDriver driver, String searchTerm){
        //get the web elements
        WebElement searchBar = driver.findElement(By.id("search"));
        List<WebElement> buttons = driver.findElements(By.tagName("button"));

        //type in search term and press enter
        searchBar.sendKeys(searchTerm);
        // searchBar.sendKeys(Keys.ENTER);

        for (WebElement button : buttons){
            if(button.getAttribute("title").equals("Search")){
                button.click();
                return true;
            }
        }
        //in case no search button is found
        return false;
    }

    public static List<WebElement> getResults(WebDriver driver, int x){
        
        //if message for no results found exists
        if(!driver.findElements(By.className("message")).isEmpty())
            return new LinkedList<WebElement>();    //return empty list

        //find product list element
        WebElement resultList = driver.findElement(By.tagName("ol"));
        //list of all product elements
        List<WebElement> results = resultList.findElements(By.className("item"));

        return results.subList(0, x - 1);
    }

    //navigates to the page of the given product
    public static void visitResult(WebDriver driver, WebElement result){
        String link = result.findElement(By.tagName("a")).getAttribute("href");
        
        driver.get(link);
    }

    //Given the results, returns a corresponding AlertItem object with all the matching details
    public static AlertItem parseResult(WebDriver driver){

        //title
        String title = driver.findElement(By.className("page-title-wrapper")).getText();

        //description
        WebElement quickOverview = driver.findElement(By.className("overview"));
        List<WebElement> descriptionPoints = quickOverview.findElements(By.tagName("li"));

        String description = "";
        for(WebElement point: descriptionPoints)
            description = description + point.getText() + " ";

        //Url
        String url = driver.getCurrentUrl();

        //ImageUrl
        String imageUrl = driver.findElement(By.className("fotorama__active"))
        .findElement(By.tagName("img")).getAttribute("src");

        //Price
        WebElement priceBox = driver.findElement(By.className("price-box"));
        WebElement priceElement = priceBox.findElement(By.className("price"));
        String priceText = priceElement.getText();
        String priceCleaned = priceText.replaceAll("€", "").replace(".", "");
        int priceInCents = Integer.parseInt(priceCleaned);

        //Create new alert item with the acquired info
        AlertItem alert = new AlertItem();
        alert.setTitle(title);
        alert.setDescription(description);
        alert.setUrl(url);
        alert.setImageUrl(imageUrl);
        alert.setPriceInCents(priceInCents);

        //return alert
        return alert;
    }

    public static void goToLogIn(WebDriver driver){
        WebElement logInButton = driver.findElements(By.className("nav-item")).get(2);
        String link = logInButton.findElement(By.tagName("a")).getAttribute("href");

        driver.get(link);
    }

    public static void logIn(WebDriver driver, String username){
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> inputs = form.findElements(By.tagName("input"));

        inputs.get(0).sendKeys(username);
        inputs.get(1).click();
    }

    public static JSONObject alertToJson(AlertItem alert, int alertType, String username){
        JSONObject object = new JSONObject();
        object.put("alertType", alertType);
        object.put("heading", alert.getTitle());
        object.put("description", alert.getDescription());
        object.put("url", alert.getUrl());
        object.put("imageUrl", alert.getImageUrl());
        object.put("postedBy",  username);
        object.put("priceInCents", alert.getPriceInCents());

        return object;
    }

    //attempts to post the json object to the api
    //returns the statusCode of the response or -1 upon failure to send
    //TODO: CHANGE THIS TO RETURN THE RESPONSE OBJECT AS OPPOSED TO THE STATUSCODE
    public static int postAlert(WebDriver driver, JSONObject json){

        //https://api.marketalertum.com/Alert

        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json.toString()))
        .build();
        
        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            return -1;
        }

        return response.statusCode();
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