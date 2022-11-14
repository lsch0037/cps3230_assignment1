package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

//Page object superclass
public class PageObject {
    WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); //intialise this object as a webdriver pageobject
    }
}
