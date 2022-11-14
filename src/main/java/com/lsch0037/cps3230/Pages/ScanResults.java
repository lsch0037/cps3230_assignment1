package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;

//Page object class for the scan search results page
public class ScanResults extends PageObject{

    public ScanResults(WebDriver driver){
        super(driver);
    }

    /*
     * Checks whether the message that is displayed when no results are found exists
     * If the message does exist then return false
     * Otherwise return true
     */
    public boolean foundResults(){
        //search for no results message
        List<WebElement> noResultsMessage = driver.findElements(By.className("message"));

        //if no such element is found
        if(noResultsMessage.isEmpty())
            return true;

        return false;
    }

    /*
     * Returns a list of links to the top results, the length of the list is specified in numOfResults
     * If numOfResults is greater than the actual results found then only as many links are returns as results exist
     */
    public List<String> getResultLinks(int numOfResults){
        //search for results elements
        List<WebElement> resultItems = driver.findElements(By.className("product-item-top"));

        List<String> links = new LinkedList<String>();

        //for each result found
        for (WebElement result : resultItems) {
            WebElement a = result.findElement(By.tagName("a"));     //get the sub-element that holds the link
            links.add(a.getAttribute("href"));                          //add links to list
        }


        //if more results are expected than exist
        if(numOfResults > links.size())
            return links;                   //return all results that exist
        else
            return links.subList(0, numOfResults);      //return as many results as are expected
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
