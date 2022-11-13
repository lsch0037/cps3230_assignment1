package com.lsch0037.cps3230.Pages;

import org.openqa.selenium.WebDriver;

public class ScanProduct extends PageObject{

    public ScanProduct(WebDriver driver){
        super(driver);
    }

    public String getHeading(){

    }

    public String getDescription(){

    }

    public String getUrl(){

    }

    public String getImageUrl(){

    }

    public int getPriceInCents(){

    }

/*
    //Given the results, returns a corresponding AlertItem object with all the matching details
    //TODO: POSSIBLE SPLIT THIS UP INTO parseHeading, parseDescription.....
    public static Product parseResult(WebDriver driver, String userId, int alertType){

        //title
        String title = driver.findElement(By.className("page-title-wrapper")).getText();

        //description
        WebElement quickOverview = driver.findElement(By.className("overview"));
        List<WebElement> descriptionPoints = quickOverview.findElements(By.tagName("li"));

        String description = "";
        for(WebElement point: descriptionPoints)
            description = description + point.getText() + " ";

        //Url
        String url = driver.getCurrentUrl();

        //ImageUrl
        String imageUrl = driver.findElement(By.className("gallery-container"))
            .findElement(By.tagName("img"))
            .getAttribute("src");

        //Price
        WebElement priceBox = driver.findElement(By.className("price-box"));
        WebElement priceElement = priceBox.findElement(By.className("price"));
        String priceText = priceElement.getText();
        String priceCleaned = priceText.replaceAll("€", "")
        .replace(".", "")
        .replace(",", "");
        int priceInCents = Integer.parseInt(priceCleaned);

        //Create new alert item with the acquired info
        Product alert = new Product();
        alert.setAlertType(alertType);
        alert.setHeading(title);
        alert.setDescription(description);
        alert.setUrl(url);
        alert.setImageUrl(imageUrl);
        alert.setPostedBy(userId);
        alert.setPriceInCents(priceInCents);

        //return alert
        return alert;
    }
    */
}
