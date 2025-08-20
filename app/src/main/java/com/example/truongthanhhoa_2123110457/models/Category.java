package com.example.truongthanhhoa_2123110457.models;

public class Category {
    private String name;      // Tên category
    private String imageUrl;  // Ảnh đại diện (lấy sản phẩm đầu tiên trong category)

    public Category(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
}