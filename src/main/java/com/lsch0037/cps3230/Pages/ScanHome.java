package com.lsch0037.cps3230.Pages;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page object class for the scanmalta home page
public class ScanHome extends PageObject {
    @FindBy(id = "search")
    private WebElement searchBar;
        
    public ScanHome(WebDriver driver){
        super(driver);
    }

    //enter the searchterm into the searchbar element and press enter
    public void search(String searchTerm){
        this.searchBar.sendKeys(searchTerm);
        this.searchBar.sendKeys(Keys.ENTER);
    }


    /*
     * Verifies that the user is in fact on the Scan home page by comparing the url to that expected on the home page
     * Returns true if the url matches, false otherwise
     */
    public boolean isOnScanHomePage(){
        if(driver.getTitle().equals("Computers Store Malta | SCAN"))
            return true;

        return false;
    }
}
