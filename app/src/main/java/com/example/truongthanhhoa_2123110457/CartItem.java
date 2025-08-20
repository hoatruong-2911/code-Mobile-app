package com.example.truongthanhhoa_2123110457;

public class CartItem {
    private Product product;
    private int quantity;

    private boolean isSelected; // ✅ Thêm thuộc tính này


    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.isSelected = true; // Mặc định sản phẩm mới thêm vào giỏ hàng sẽ được chọn

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

    // ✅ Thêm phương thức getter cho isSelected
    public boolean isSelected() {
        return isSelected;
    }

    // ✅ Thêm phương thức setter cho isSelected
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}