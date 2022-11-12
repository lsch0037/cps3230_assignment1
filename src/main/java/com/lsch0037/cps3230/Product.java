package com.lsch0037.cps3230;

import org.json.JSONObject;

public class Product {
    private int alertType;
    private String heading;
    private String description;
    private String url;
    private String imageUrl;
    private String postedBy;
    private int priceInCents;


    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        object.put("alertType", this.alertType);
        object.put("heading", this.heading);
        object.put("description", this.description);
        object.put("url", this.url);
        object.put("imageUrl", this.imageUrl);
        object.put("postedBy", this.postedBy);
        object.put("priceInCents", this.priceInCents);

        return object;
    }

    //Getters and Setters
    public int getAlertType(){
        return this.alertType;
    }

    public void setAlertType(int alertType){
        this.alertType = alertType;
    }

    public String getHeading(){
        return this.heading;
    }

    public void setHeading(String heading){
        this.heading = heading;
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

    public String getPostedBy(){
        return this.postedBy;
    }

    public void setPostedBy(String postedBy){
        this.postedBy = postedBy;
    }

    public int getPriceInCents(){
        return this.priceInCents;
    }

    public void setPriceInCents(int priceInCents){
        this.priceInCents = priceInCents;
    }
}
