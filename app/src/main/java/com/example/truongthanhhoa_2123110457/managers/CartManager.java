package com.example.truongthanhhoa_2123110457.managers;

import com.example.truongthanhhoa_2123110457.models.CartItem;
import com.example.truongthanhhoa_2123110457.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator; // Thêm import cho Iterator

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

    // Phương thức để thêm sản phẩm với số lượng tùy chỉnh.
    public void addItem(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
    }

    // ✅ Phương thức mới để xóa sản phẩm dựa trên ID
    public void removeItemById(String productId) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                // Thoát khỏi vòng lặp sau khi xóa
                return;
            }
        }
    }

    // ✅ Sửa phương thức removeItem để sử dụng removeItemById
    // Chúng ta vẫn giữ phương thức này để tránh lỗi ở những chỗ khác.
    // Hoặc bạn có thể xóa nó và sửa lại tất cả các chỗ gọi.
    public void removeItem(CartItem item) {
        removeItemById(item.getProduct().getId());
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