package com.lsch0037.cps3230;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class AppTest 
{
    WebDriver driver;

    //TODO: FIGURE OUT WHY THIS DOESNT WORK
    @BeforeEach
    public void setup(){
        //FOR LINUX SYSTEM
        //System.setProperty("webdriver.chrome.driver", "/home/schimpf/webtesting/chromedriver");

        //FOR WINDOWS
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }

    @Test
    public void testSimpleGoogleSearch() throws Exception{

        //TODO:REMOVE
        setup();



        //open amazon.de
        driver.get("https://www.amazon.de");

        //get the accept cookies button
        WebElement acceptCookiesButton = driver.findElement(By.id("sp-cc-accept"));
        WebElement searchBar = driver.findElement(By.id("twotabsearchtextbox"));
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));

        if(acceptCookiesButton != null)
            acceptCookiesButton.sendKeys(Keys.ENTER);

        searchBar.sendKeys("bluetooth speaker");

        searchButton.sendKeys(Keys.ENTER);

        Thread.sleep(1000);

        //TODO:REMOVE
        teardown();
    }
}
