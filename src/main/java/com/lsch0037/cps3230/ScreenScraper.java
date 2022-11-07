package com.lsch0037.cps3230;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class ScreenScraper 
{

    public static void main( String[] args ) throws Exception {
        WebDriver driver = new ChromeDriver();
        visitAmazon(driver);
        searchAmazon(driver, "bluetooth Speaker");
        scrapeAmazon(driver, 5);

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
        //WebElement acceptCookiesButton = driver.findElement(By.id("sp-cc-accept"));
        WebElement searchBar = driver.findElement(By.id("twotabsearchtextbox"));
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));

        /*
        //if cookies popup, click accept cookies
        if(acceptCookiesButton != null)
            acceptCookiesButton.sendKeys(Keys.ENTER);
        */

        //type in search term and press enter
        searchBar.sendKeys(searchTerm);
        searchButton.sendKeys(Keys.ENTER);

    }

    public static void scrapeAmazon(WebDriver driver, int numOfResults){

    }

    public static void logInMarketAlert(WebDriver driver){

    }

    public static void viewAlerts(WebDriver driver){

    }

    public static void logOutMarketAlert(WebDriver driver){

    }

    public static void createAlert(WebDriver driver){

    }

    public static void deleteAlerts(WebDriver driver){

    }
}
