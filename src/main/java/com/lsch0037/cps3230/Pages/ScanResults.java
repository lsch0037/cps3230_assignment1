package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedList;
import java.util.List;

public class ScanResults extends PageObject{
    @FindBy(className = "message")
    private List<WebElement> noResultsMessage;

    @FindBy(tagName = "ol")
    private WebElement resultList;

    public ScanResults(WebDriver driver){
        super(driver);
    }

    public boolean foundResults(){
        if(noResultsMessage.isEmpty())
            return true;

        return false;
    }

    public List<String> getResultLinks(int numOfResults){
        if(!foundResults())
            return new LinkedList<String>();
            
        List<WebElement> linkElements =  resultList.findElements(By.tagName("a"));
        List<String> links = new LinkedList<String>();
        for (WebElement linkElement : linkElements) {
            links.add(linkElement.getAttribute("href"));
        }

        return links.subList(0, numOfResults);
    }

    public boolean isOnResultsPage(){
        if(driver.getCurrentUrl().startsWith("https://www.scanmalta.com/shop/catalogsearch/result"))
            return true;

        return false;
    }
}
