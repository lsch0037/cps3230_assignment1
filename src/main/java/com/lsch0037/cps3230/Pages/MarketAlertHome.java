package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page object class for the marketAlertUm home page
public class MarketAlertHome extends PageObject{

    @FindBy(className = "navbar-nav")
    WebElement navBar;

    public MarketAlertHome(WebDriver driver){
        super(driver);
    }


    //Clicks the login button to navigate to the login page
    public void clickLogInButton(){
        //get the third button element in the navBar
        WebElement logInButton = navBar.findElements(By.tagName("li")).get(2);
        logInButton.click();    //click the button
    }

    /*
     * Verifies that the user is in fact on the Home page by comparing the url to that expected on the Home page
     * Returns true if the url matches, false otherwise
     */
    public boolean isOnMarketAlertHome(){
        if(driver.getTitle().equals("Home Page - MarketAlertUM"))
            return true;

        return false;
    }
}
