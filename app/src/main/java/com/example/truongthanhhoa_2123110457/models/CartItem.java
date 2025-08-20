package com.example.truongthanhhoa_2123110457.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Product product;
    private int quantity;
    private boolean isSelected;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.isSelected = true;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}