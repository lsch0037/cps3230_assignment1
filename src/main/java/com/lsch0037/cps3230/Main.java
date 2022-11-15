package com.lsch0037.cps3230;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
    public static void main(String [] args) {
        //set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVERPATH);

        WebDriver driver = new ChromeDriver();

        ScreenScraper screenScraper = new ScreenScraper(driver);
        screenScraper.run(Constants.USERID, "laptop", 5, 1);
        
    }
    
}
