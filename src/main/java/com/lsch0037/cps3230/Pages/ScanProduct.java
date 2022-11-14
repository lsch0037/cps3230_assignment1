package com.lsch0037.cps3230.Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.lsch0037.cps3230.ScreenScraperUtils;

//Page object class for the scan product page
public class ScanProduct extends PageObject{

    @FindBy(className = "page-title-wrapper")
    private WebElement titleElement;

    @FindBy(xpath = "//div[@itemProp='description']")
    private WebElement descriptionElement;

    @FindBy(className  = "fotorama__stage__frame")
    private WebElement imageElement;

    @FindBy(className = "price-box")
    private WebElement priceElement;

    public ScanProduct(WebDriver driver){
        super(driver);
    }

    //returns the text of the product title
    public String getHeading(){
        return titleElement.getText();
    }

    /*
     * Turns the bulletpoints of the "quick overview" section into a long paragraph that is used as a description
     * Returns the description 
     */
    public String getDescription(){
        List<WebElement> descriptionPoints = descriptionElement.findElements(By.tagName("li"));

        String description = "";    //String which holds the description paragraph
        
        //for each bulletpoint
        for(WebElement point: descriptionPoints)
            description = description + point.getText() + " ";  //add point to description and then add a space at the end

        //return the final description
        return description;
    }

    //return the current url of the window, as the url attribute for the product
    public String getUrl(){
        return driver.getCurrentUrl();
    }

    //finds the image html element and returns its "href" attribute 
    public String getImageUrl(){
        //await the image for a maximum of 10 seconds since it may take longer to load
        WebElement awaitImageElement = ScreenScraperUtils.awaitElementVisible(driver, imageElement, 10);
        return awaitImageElement.getAttribute("href");  //return the link to the image
    }

    /*
     * finds the price on the page and removes any extra text or symbols
     * returns the final integer
     */
    public int getPriceInCents(){
        WebElement priceElementInner = priceElement.findElement(By.className("price"));     //find price element
        String priceText = priceElementInner.getText();                                                 //get text
        String priceCleaned = priceText.replaceAll("€", "")                         //remove € sign from string
        .replace(".", "")                                                          //remove . from string
        .replace(",", "");                                                         //remove , from string
        return Integer.parseInt(priceCleaned);                                                          //cast to integer and return
    }

    /*
     * Verifies that the driver is currently on the page of a product on scanmalta
     * Checks whether the hmtl element for the heading of the "Related Products" seciton exits
     * If it does exits return true, otherwise return false
     */
    public boolean isOnProductPage(){
        //find the heading of the "Related Products" section
        List<WebElement> relatedProduct = driver.findElements(By.xpath("//strong[@id='block-related-heading']"));

        //return whether such an element was found
        return !relatedProduct.isEmpty();
    }
}
