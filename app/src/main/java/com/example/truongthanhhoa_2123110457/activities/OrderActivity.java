// File: app/src/main/java/com/example/truongthanhhoa_2123110457/activities/OrderActivity.java

package com.example.truongthanhhoa_2123110457.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truongthanhhoa_2123110457.models.CartItem;
import com.example.truongthanhhoa_2123110457.adapters.CartItemAdapter;
import com.example.truongthanhhoa_2123110457.managers.CartManager;
import com.example.truongthanhhoa_2123110457.R;
import com.example.truongthanhhoa_2123110457.activities.CheckoutActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class OrderActivity extends AppCompatActivity implements CartItemAdapter.OnCartUpdateListener {

    private RecyclerView rcvOrderItems;
    private TextView txtEmptyCart, txtTotalPrice;
    private Button btnCheckout;
    private CartItemAdapter cartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các views
        rcvOrderItems = findViewById(R.id.rcvOrderItems);
        txtEmptyCart = findViewById(R.id.txtEmptyCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        // ✅ Xử lý sự kiện khi click nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(OrderActivity.this, CheckoutActivity.class);
                intent.putExtra("selectedItems", (Serializable) new ArrayList<CartItem>(selectedItems));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ✅ Cập nhật giỏ hàng mỗi khi Activity hiển thị trở lại
        setupCartRecyclerView();
    }

    private void setupCartRecyclerView() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        if (cartItems.isEmpty()) {
            txtEmptyCart.setVisibility(TextView.VISIBLE);
            rcvOrderItems.setVisibility(RecyclerView.GONE);
            findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        } else {
            txtEmptyCart.setVisibility(View.GONE);
            rcvOrderItems.setVisibility(View.VISIBLE);
            findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);

            rcvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            cartItemAdapter = new CartItemAdapter(this, cartItems, this);
            rcvOrderItems.setAdapter(cartItemAdapter);
        }
        updateTotalPrice();
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            txtEmptyCart.setVisibility(View.VISIBLE);
            findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        } else {
            txtEmptyCart.setVisibility(View.GONE);
            findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        double vndTotal = total * 25000;
        DecimalFormat df = new DecimalFormat("#,###đ");
        txtTotalPrice.setText(df.format(vndTotal));
    }

    private List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
}