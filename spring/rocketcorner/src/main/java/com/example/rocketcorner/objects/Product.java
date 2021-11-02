package com.example.rocketcorner.objects;

public class Product {
    private String name;
    private String desc;
    private String imgLink;
    private double price;

    public Product() {
        super();
    }

    public Product(String name, String desc, String imgLink, double price) {
        this.name = name;
        this.desc = desc;
        this.imgLink = imgLink;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
