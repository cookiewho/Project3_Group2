package com.example.rocketcorner.HomeAdaptersHelperClasses;

public class FeaturedHelperClass {

    String imgLink, title, description;

    public FeaturedHelperClass(String imgLink, String title, String description) {
        this.imgLink = imgLink;
        this.title = title;
        this.description = description;
    }

    public String getImage() {
        return imgLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}