package com.lsch0037.cps3230.Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.lsch0037.cps3230.ScreenScraperUtils;

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

    public String getHeading(){
        return titleElement.getText();
    }

    public String getDescription(){
        List<WebElement> descriptionPoints = descriptionElement.findElements(By.tagName("li"));

        String description = "";
        for(WebElement point: descriptionPoints)
            description = description + point.getText() + " ";

        return description;
    }

    public String getUrl(){
        return driver.getCurrentUrl();
    }

    public String getImageUrl(){
        WebElement awaitImageElement = ScreenScraperUtils.awaitElementVisible(driver, imageElement, 10);
        return awaitImageElement.getAttribute("href");
    }

    public int getPriceInCents(){
        WebElement priceElementInner = priceElement.findElement(By.className("price"));
        String priceText = priceElementInner.getText();
        String priceCleaned = priceText.replaceAll("€", "")
        .replace(".", "")
        .replace(",", "");
        return Integer.parseInt(priceCleaned);
    }

    public boolean isOnProductPage(){
        //TODO: CHECK IF WE ARE ON THE PRODUCT PAGE
        return false;
    }
}
