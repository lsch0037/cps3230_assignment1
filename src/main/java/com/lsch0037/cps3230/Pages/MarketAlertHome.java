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
}
