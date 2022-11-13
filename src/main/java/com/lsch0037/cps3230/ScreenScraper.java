package com.lsch0037.cps3230;

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
import java.util.LinkedList;

public class ScreenScraper 
{
    // static WebDriver driver = new ChromeDriver();
    // static ScanHome scanHome = new ScanHome(driver);

    //TODO: REMOVE THIS TO ITS OWN Main.java CLASS
    public static void main( String[] args ) throws Exception {
        // String userId = args[1];
        // String searchTerm = args[2];
        // String itemType = args[3];
        // String numOfItems = args[4];
        
        //TODO: REMOVE THIS BEFORE RELEASE
        String userId = "21ed7a53-ff36-4daf-8da0-c8b66b11c0de";
        int numOfItems = 5;
        int alertType = 6;
        //TODO: REMOVE THIS BEFORE RELEASE

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        deleteAlerts(driver, userId);

        visitScan(driver);
        searchScan(driver, "bluetooth speaker");
        List<String> reslutLinks = getResultLinks(driver, numOfItems);

        for (String link : reslutLinks) {
            driver.get(link); 
            Product product = parseResult(driver, userId, alertType);
            // JSONObject productJson = alertToJson(product, alertType, userId);
            JSONObject productJson = product.toJson();
            
            postAlert(driver, productJson);
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
        scanHome.enterSearchTerm(searchTerm);
        scanHome.pressSearchButton();

    }

    /*
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
    */

    /*
    public static List<String> getResultLinks(WebDriver driver, int numOfResults){
        //if message for no results found exists
        if(!driver.findElements(By.className("message")).isEmpty())
            return new LinkedList<String>();    //return empty list

        //find product list element
        WebElement resultList = driver.findElement(By.tagName("ol"));
        //list of all product elements
        List<WebElement> results = resultList.findElements(By.className("item"));

        List<String> links = new LinkedList<String>();

        for(WebElement result : results){
            links.add(result.findElement(By.tagName("a")).getAttribute("href"));
        }

        return links.subList(0, numOfResults);
    }
    */

    public static List<String> getResultLinks(WebDriver driver, ScanResults scanResults, int numOfResults){
        return scanResults.getResultLinks(numOfResults);
    }

    //navigates to the page of the given product
    public static void visitResult(WebDriver driver, String link){
        driver.get(link);
    }

/*
    //Given the results, returns a corresponding AlertItem object with all the matching details
    //TODO: POSSIBLE SPLIT THIS UP INTO parseHeading, parseDescription.....
    public static Product parseResult(WebDriver driver, String userId, int alertType){

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
        String imageUrl = driver.findElement(By.className("gallery-container"))
            .findElement(By.tagName("img"))
            .getAttribute("src");

        //Price
        WebElement priceBox = driver.findElement(By.className("price-box"));
        WebElement priceElement = priceBox.findElement(By.className("price"));
        String priceText = priceElement.getText();
        String priceCleaned = priceText.replaceAll("€", "")
        .replace(".", "")
        .replace(",", "");
        int priceInCents = Integer.parseInt(priceCleaned);

        //Create new alert item with the acquired info
        Product alert = new Product();
        alert.setAlertType(alertType);
        alert.setHeading(title);
        alert.setDescription(description);
        alert.setUrl(url);
        alert.setImageUrl(imageUrl);
        alert.setPostedBy(userId);
        alert.setPriceInCents(priceInCents);

        //return alert
        return alert;
    }
    */

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