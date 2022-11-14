package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;

public class ScanResults extends PageObject{

    public ScanResults(WebDriver driver){
        super(driver);
    }

    public boolean foundResults(){
        List<WebElement> noResultsMessage = driver.findElements(By.className("message"));

        if(noResultsMessage.isEmpty())
            return true;

        return false;
    }

    public List<String> getResultLinks(int numOfResults){
        List<WebElement> resultItems = driver.findElements(By.className("product-item-top"));

        List<String> links = new LinkedList<String>();
        for (WebElement result : resultItems) {
            WebElement a = result.findElement(By.tagName("a"));
            links.add(a.getAttribute("href"));
        }

        return links.subList(0, numOfResults);
    }

    /*
     * Returns whether the current instance of WebDriver is on the scanmalta search results page
     * Compares the title of the tab to that which is expected when on the results page
     * returns true if is is on the results page, false otherwise
     */
    public boolean isOnResultsPage(){
        if(driver.getCurrentUrl().startsWith("https://www.scanmalta.com/shop/catalogsearch/result"))
            return true;

        return false;
    }
}
