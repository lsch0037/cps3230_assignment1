package com.lsch0037.cps3230.Pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ScanHome extends PageObject {
    @FindBy(id = "search")
    private WebElement searchBar;
        
    @FindBy(tagName = "button")
    private WebElement searchButton;

    public ScanHome(WebDriver driver){
        super(driver);
    }

    public void enterSearchTerm(String searchTerm){
        this.searchBar.sendKeys(searchTerm);
    }

    public void pressSearchButton(){
        this.searchButton.click();
    }
}
