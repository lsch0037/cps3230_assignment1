package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page object class for the marketAlertUm login page
public class MarketAlertLogin extends PageObject {

    @FindBy(xpath = "//input[@name='UserId']")
    private WebElement textInput;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement submitButton;


    public MarketAlertLogin(WebDriver driver){
        super(driver);
    }

    //Enters the given userId in the input field
    public void inputUserId(String userId){
        textInput.sendKeys(userId);
    }

    //Presses the submit button near the input field
    public void submit(){
        submitButton.click();
    }

    /*
     * Verifies that the user is in fact on the Login page by comparing the url to that expected on the Login page
     * Returns true if the url matches, false otherwise
     */
    public boolean isOnLogInPage(){
        if(driver.getCurrentUrl().equals("https://www.marketalertum.com/Alerts/Login"))
            return true;

        return false;
    }
    
}
