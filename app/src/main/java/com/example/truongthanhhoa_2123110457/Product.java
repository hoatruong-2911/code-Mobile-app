package com.example.truongthanhhoa_2123110457;

public class Product {
    private String id;
    private String title;
    private double price;
    private String image;
    private String category;

    public Product(String id, String title, double price, String image, String category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
}
