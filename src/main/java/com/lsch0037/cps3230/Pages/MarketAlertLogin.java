package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MarketAlertLogin extends PageObject {

    @FindBy(xpath = "//input[@name='UserId']")
    private WebElement textInput;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement submitButton;

    public MarketAlertLogin(WebDriver driver){
        super(driver);
    }

    public void inputUserId(String userId){
        textInput.sendKeys(userId);
    }

    public void submit(){
        submitButton.click();
    }

    public boolean isOnLogInPage(){
        if(driver.getCurrentUrl().equals("https://www.marketalertum.com/Alerts/Login"))
            return true;

        return false;
    }
    
}
