package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MarketAlertHome extends PageObject{

    @FindBy(className = "navbar-nav")
    WebElement navBar;

    public MarketAlertHome(WebDriver driver){
        super(driver);
    }

    public void clickLogInButton(){
        //get the third button in the navBar
        WebElement logInButton = navBar.findElements(By.tagName("li")).get(2);
        logInButton.click();    //click logIn button
    }

    /*
     * Returns whether the current instance of WebDriver is on the MarketAlertUm Home page
     * Compares the title of the tab to that which is expected when on the Hope page
     * returns true if is is on the home page, false otherwise
     */
    public boolean isOnMarketAlertHome(){
        if(driver.getTitle().equals("Home Page - MarketAlertUM"))
            return true;

        return false;
    }
}
