package com.example.truongthanhhoa_2123110457;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Phương thức mới để thêm sản phẩm với số lượng tùy chỉnh.
    // Phương thức này sẽ kiểm tra xem sản phẩm đã có trong giỏ hàng chưa.
    // Nếu có, nó sẽ cộng thêm số lượng mới vào số lượng hiện có.
    // Nếu chưa, nó sẽ thêm sản phẩm mới với số lượng đã chọn.
    public void addItem(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
    }

    // Các phương thức khác giữ nguyên.
    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public void increaseQuantity(CartItem item) {
        item.setQuantity(item.getQuantity() + 1);
    }

    public void decreaseQuantity(CartItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            removeItem(item);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }
}