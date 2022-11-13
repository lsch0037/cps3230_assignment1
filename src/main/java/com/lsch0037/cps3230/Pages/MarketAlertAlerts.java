package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.WebDriver;

public class MarketAlertAlerts extends PageObject{

    public MarketAlertAlerts(WebDriver driver){
        super(driver);
    }

    public boolean isOnAlertsPage(){
        if(driver.getCurrentUrl().equals("https://www.marketalertum.com/Alerts/List"))
            return true;

        return false;
    }
    
}