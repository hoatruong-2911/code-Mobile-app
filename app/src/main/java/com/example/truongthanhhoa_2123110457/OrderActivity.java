package com.example.truongthanhhoa_2123110457;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

        // ✅ Cài đặt RecyclerView và listener
        setupCartRecyclerView();

        // ✅ Xử lý sự kiện khi click nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Xử lý logic thanh toán thực tế cho các sản phẩm đã chọn
                Toast.makeText(this, "Đang xử lý thanh toán cho " + selectedItems.size() + " sản phẩm...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Phương thức cài đặt RecyclerView
    private void setupCartRecyclerView() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        if (cartItems.isEmpty()) {
            txtEmptyCart.setVisibility(TextView.VISIBLE);
            rcvOrderItems.setVisibility(RecyclerView.GONE);
            // Ẩn layout tổng tiền nếu giỏ hàng trống
            findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        } else {
            txtEmptyCart.setVisibility(TextView.GONE);
            rcvOrderItems.setVisibility(RecyclerView.VISIBLE);
            // Hiển thị layout tổng tiền
            findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);

            rcvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            cartItemAdapter = new CartItemAdapter(this, cartItems, this); // Truyền listener
            rcvOrderItems.setAdapter(cartItemAdapter);
        }
        updateTotalPrice(); // ✅ Cập nhật tổng tiền ngay khi khởi tạo
    }

    // ✅ Phương thức cập nhật tổng tiền
    @Override
    public void onCartUpdated() {
        updateTotalPrice();
        // Kiểm tra lại nếu giỏ hàng rỗng thì ẩn layout tổng tiền
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            txtEmptyCart.setVisibility(TextView.VISIBLE);
            findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        } else {
            txtEmptyCart.setVisibility(View.GONE);
            findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalPrice() {
        double total = 0;
        // Chỉ tính tổng tiền của các sản phẩm đã được chọn
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        double vndTotal = total * 25000;
        DecimalFormat df = new DecimalFormat("#,###đ");
        txtTotalPrice.setText(df.format(vndTotal));
    }

    // ✅ Phương thức lấy danh sách sản phẩm đã chọn
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