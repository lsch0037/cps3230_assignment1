package com.lsch0037.cps3230.Pages;

import java.util.LinkedList;
import java.util.List;

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

    public List<WebElement> getAlerts(){

        return new LinkedList<WebElement>();
    }
    
}