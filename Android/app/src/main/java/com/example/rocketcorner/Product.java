package com.example.rocketcorner;

public class Product {
//    private String id;
    private String name;
    private String desc;
    private String imgLink;
    private double price;

//    public String getId() {
//        return id;
//    }

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

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", price=" + price +
                '}';
    }
}
