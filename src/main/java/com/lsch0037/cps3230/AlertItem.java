package com.lsch0037.cps3230;

public class AlertItem {
    //TODO: GIVE THIS CLASS AND ITS ATTRIBUTES BETTER NAMES
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private int priceInCents;
    //TODO: MAKE THE USERID AND THE ALERTTYPE PART OF THIS OBJECT


    //TODO: MAKE A BETTER WAY OF INITIALISING THIS
    public void AlertItem(){
        int x = 0;
    }

    //Getters and Setters
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public int getPriceInCents(){
        return this.priceInCents;
    }

    public void setPriceInCents(int priceInCents){
        this.priceInCents = priceInCents;
    }
}
