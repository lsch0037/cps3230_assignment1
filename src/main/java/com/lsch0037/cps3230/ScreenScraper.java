package com.lsch0037.cps3230;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.j2objc.annotations.RetainedLocalRef;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;
import java.security.Key;
import java.util.LinkedList;

public class ScreenScraper 
{

    public static void main( String[] args ) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        visitAmazon(driver);
        searchAmazon(driver, "bluetooth Speaker");
        getTopResults(driver, 5);

        driver.quit();
    }

    //nagivates to the amazon website
    public static void visitAmazon(WebDriver driver){
        //visit amazon.com website
        driver.get("https://www.amazon.com");
    }

    //nagivates to the marketalertum website
    public static void visitMarketAlert(WebDriver driver){
        //visit marketalertum.com website
        driver.get("https://www.marketalertum.com/");
    }

    public static void searchAmazon(WebDriver driver, String searchTerm){
        //get the web elements
        WebElement searchBar = driver.findElement(By.id("twotabsearchtextbox"));
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));

        //type in search term and press enter
        searchBar.sendKeys(searchTerm);
        searchButton.sendKeys(Keys.ENTER);

    }

    //Returns the web element of the top X search results
    //where x is the number of results specified
    public static List<WebElement> getTopResults(WebDriver driver, int numOfResults){
        List<WebElement> resultsItem = driver.findElements(By.className("s-result-item"));

        /*List<WebElement> titlesElements = driver.findElements(By.tagName("h2"));
        List<WebElement> imageUrlElements = driver.findElements(By.className("s-image"));

        List<AlertItem> items = new LinkedList<AlertItem>();

        for (WebElement result : searchResults) {
            AlertItem item = new AlertItem();

            //item.setType(alertType);
            item.setTitle(titleElement.getText());
            item.setDescription("test");
            item.setUrl("test");
            item.setImageUrl("test");
            item.setPostedBy("test");
            item.setPriceInCents(1);

            items.add(item);
        }

        return items;
        */

        return resultsItem;
    }

    //Given the results, returns a corresponding AlertItem object with all the matching details
    public static AlertItem parseResult(WebDriver driver, WebElement result){
        //click the result element
        result.sendKeys(Keys.ENTER);

        String title = driver.findElement(By.id("productTitle")).getText();
        String description = driver.findElement(By.id("productDescription")).getText();
        String url = driver.getCurrentUrl();
        String imageUrl = driver.findElement(By.id("landignImage")).getAttribute("src");
        String priceText = driver.findElement(By.id("corePriceDisplay_desktop_feature_div")).getText();
        String priceTextCleaned = priceText.replace("$", "").replace(".", "");
        int priceInCents = Integer.parseInt(priceTextCleaned);

        AlertItem alert = new AlertItem();
        alert.setTitle(title);
        alert.setDescription(description);
        alert.setUrl(url);
        alert.setImageUrl(imageUrl);
        alert.setPriceInCents(priceInCents);

        //navigate back to the results page
        driver.navigate().back();

        return alert;
    }

    public static void logInMarketAlert(WebDriver driver){

    }

    public static void viewAlerts(WebDriver driver){

    }

    public static void logOutMarketAlert(WebDriver driver){

    }

    public static void postAlert(WebDriver driver){

    }

    public static void deleteAlerts(WebDriver driver){

    }
}
