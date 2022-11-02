package com.lsch0037.cps3230;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AppTest 
{
    WebDriver driver;

    @BeforeEach
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "/home/schimpf/webtesting/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }

    @Test
    public void testSimpleGoogleSearch(){
        driver.get("https://www.google.com");
    }
}
