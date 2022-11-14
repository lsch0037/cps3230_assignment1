package com.lsch0037.cps3230.Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MarketAlertList extends PageObject{

    public MarketAlertList(WebDriver driver){
        super(driver);
    }

    public boolean isOnAlertsPage(){
        if(driver.getCurrentUrl().equals("https://www.marketalertum.com/Alerts/List"))
            return true;

        return false;
    }

    /*
     * Returns a list of alert elements in the list
     */
    public List<WebElement> getAlerts(){
        //refresh to ensure the information is up to date
        driver.navigate().refresh();

        //return alert element
        return driver.findElements(By.tagName("tbody"));
    }
}