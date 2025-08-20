// File: app/src/main/java/com/example/truongthanhhoa_2123110457/activities/CheckoutActivity.java

package com.example.truongthanhhoa_2123110457.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Thêm import này
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.truongthanhhoa_2123110457.R;
import com.example.truongthanhhoa_2123110457.managers.CartManager;
import com.example.truongthanhhoa_2123110457.models.CartItem;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Thêm các import cần thiết cho AlertDialog
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout productLayout;
    private TextView txtSubtotal, txtTotal, txtAddressInfo;
    private Button btnPlaceOrder, btnChangeAddress; // Ánh xạ btnChangeAddress
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        productLayout = findViewById(R.id.productLayout);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTotal = findViewById(R.id.txtTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        txtAddressInfo = findViewById(R.id.txtAddressInfo);
        btnChangeAddress = findViewById(R.id.btnChangeAddress); // Ánh xạ nút

        loadUserInfoFromSharedPreferences();

        ArrayList<CartItem> selectedItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("selectedItems");

        if (selectedItems != null && !selectedItems.isEmpty()) {
            displaySelectedProducts(selectedItems);
            calculateTotalPrice(selectedItems);
        } else {
            Toast.makeText(this, "Không có sản phẩm nào được chọn.", Toast.LENGTH_SHORT).show();
        }

        btnPlaceOrder.setOnClickListener(v -> {
            if (selectedItems != null) {
                for (CartItem item : selectedItems) {
                    CartManager.getInstance().removeItem(item);
                }
            }
            Toast.makeText(this, "Đơn hàng của bạn đã được đặt thành công!", Toast.LENGTH_LONG).show();
            finish();
        });

        // ✅ Thêm OnClickListener cho nút "Chỉnh sửa"
        btnChangeAddress.setOnClickListener(v -> showAddressUpdateDialog());
    }

    private void loadUserInfoFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String address = sharedPreferences.getString("address", "N/A");

        String fullAddress = String.format("Tên khách hàng: %s - %s\nĐịa chỉ: %s", fullName, phone, address);
        txtAddressInfo.setText(fullAddress);
    }

    // ✅ Phương thức mới để hiển thị dialog cập nhật thông tin
    private void showAddressUpdateDialog() {
        // Lấy layout cho dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_address, null);

        // Ánh xạ các EditText trong dialog
        EditText etFullName = dialogView.findViewById(R.id.etFullName);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);

        // Lấy thông tin hiện tại để hiển thị sẵn trong dialog
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        etFullName.setText(sharedPreferences.getString("fullName", ""));
        etPhone.setText(sharedPreferences.getString("phone", ""));
        etAddress.setText(sharedPreferences.getString("address", ""));

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật địa chỉ nhận hàng");
        builder.setView(dialogView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Lấy dữ liệu mới từ EditText
            String newFullName = etFullName.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            String newAddress = etAddress.getText().toString().trim();

            // Lưu dữ liệu mới vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fullName", newFullName);
            editor.putString("phone", newPhone);
            editor.putString("address", newAddress);
            editor.apply();

            // Cập nhật lại giao diện
            loadUserInfoFromSharedPreferences();
            Toast.makeText(this, "Đã cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displaySelectedProducts(List<CartItem> items) {
        productLayout.removeAllViews();
        for (CartItem item : items) {
            View productView = getLayoutInflater().inflate(R.layout.item_checkout_product, productLayout, false);
            ImageView imgProduct = productView.findViewById(R.id.imgProduct);
            TextView txtProductName = productView.findViewById(R.id.txtProductName);
            TextView txtProductPrice = productView.findViewById(R.id.txtProductPrice);

            Glide.with(this).load(item.getProduct().getImage()).into(imgProduct);
            txtProductName.setText(item.getProduct().getTitle());

            double vndPrice = item.getProduct().getPrice() * item.getQuantity() * 25000;
            DecimalFormat df = new DecimalFormat("#,###đ");
            txtProductPrice.setText(df.format(vndPrice) + " x" + item.getQuantity());

            productLayout.addView(productView);
        }
    }

    private void calculateTotalPrice(List<CartItem> items) {
        totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }

        double vndSubtotal = totalPrice * 25000;
        DecimalFormat df = new DecimalFormat("#,###đ");
        txtSubtotal.setText(df.format(vndSubtotal));
        txtTotal.setText(df.format(vndSubtotal));
    }
}