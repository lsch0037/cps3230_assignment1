package com.lsch0037.cps3230;

public class AlertItem {
    //int type;
    String title;
    String description;
    String url;
    String imageUrl;
    int priceInCents;

    public void AlertItem(){

    }


    //Getters and Setters
    /*
    public int getType(){
        return this.type;
    }

    public void setType(int type){
        this.type = type;
    }
    */

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

    /*
    public String getPostedBy(){
        return this.postedBy;
    }

    public void setPostedBy(String postedBy){
        this.postedBy = postedBy;
    }
    */

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
