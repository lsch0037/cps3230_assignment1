package com.lsch0037.cps3230.Pages;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ScanHome extends PageObject {
    @FindBy(id = "search")
    private WebElement searchBar;
        
    public ScanHome(WebDriver driver){
        super(driver);
    }

    public void search(String searchTerm){
        this.searchBar.sendKeys(searchTerm);
        this.searchBar.sendKeys(Keys.ENTER);
    }

    public boolean isOnScanHomePage(){
        if(driver.getTitle().equals("Computers Store Malta | SCAN"))
            return true;

        return false;
    }
}
