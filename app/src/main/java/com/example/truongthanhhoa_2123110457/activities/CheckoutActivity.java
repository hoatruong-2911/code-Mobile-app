// File: app/src/main/java/com/example/truongthanhhoa_2123110457/activities/CheckoutActivity.java

package com.example.truongthanhhoa_2123110457.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

// Thêm các import cần thiết cho Volley và JSON
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout productLayout;
    private TextView txtSubtotal, txtTotal, txtAddressInfo;
    private Button btnPlaceOrder, btnChangeAddress;
    private double totalPrice = 0.0;
    private ArrayList<CartItem> selectedItems;

    // ✅ Khai báo URL API thanh toán của bạn
    private String paymentApiUrl = "https://68a6956f639c6a54e99f2c11.mockapi.io/payment/payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        productLayout = findViewById(R.id.productLayout);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTotal = findViewById(R.id.txtTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        txtAddressInfo = findViewById(R.id.txtAddressInfo);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);

        loadUserInfoFromSharedPreferences();

        selectedItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("selectedItems");

        if (selectedItems != null && !selectedItems.isEmpty()) {
            displaySelectedProducts(selectedItems);
            calculateTotalPrice(selectedItems);
        } else {
            Toast.makeText(this, "Không có sản phẩm nào được chọn.", Toast.LENGTH_SHORT).show();
        }

        btnPlaceOrder.setOnClickListener(v -> {
            if (selectedItems != null && !selectedItems.isEmpty()) {
                // ✅ Gọi phương thức gửi dữ liệu lên API
                createPaymentOnApi();
            } else {
                Toast.makeText(this, "Không có sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void showAddressUpdateDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_address, null);
        EditText etFullName = dialogView.findViewById(R.id.etFullName);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        etFullName.setText(sharedPreferences.getString("fullName", ""));
        etPhone.setText(sharedPreferences.getString("phone", ""));
        etAddress.setText(sharedPreferences.getString("address", ""));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật địa chỉ nhận hàng");
        builder.setView(dialogView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newFullName = etFullName.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            String newAddress = etAddress.getText().toString().trim();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fullName", newFullName);
            editor.putString("phone", newPhone);
            editor.putString("address", newAddress);
            editor.apply();

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

    // ✅ Phương thức mới để gửi dữ liệu thanh toán lên API
    private void createPaymentOnApi() {
        try {
            // Lấy thông tin từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String fullName = sharedPreferences.getString("fullName", "N/A");
            String phone = sharedPreferences.getString("phone", "N/A");
            String address = sharedPreferences.getString("address", "N/A");

            // Tạo đối tượng JSON cho toàn bộ đơn hàng
            JSONObject paymentData = new JSONObject();
            paymentData.put("customerName", fullName);
            paymentData.put("phone", phone);
            paymentData.put("address", address);
            paymentData.put("totalPrice", totalPrice); // Gửi totalPrice dưới dạng số

            // Tạo mảng JSON cho danh sách sản phẩm
            JSONArray productsArray = new JSONArray();
            for (CartItem item : selectedItems) {
                JSONObject productObject = new JSONObject();
                productObject.put("productId", item.getProduct().getId());
                productObject.put("title", item.getProduct().getTitle());
                productObject.put("price", item.getProduct().getPrice());
                productObject.put("quantity", item.getQuantity());
                productObject.put("image", item.getProduct().getImage());
                productsArray.put(productObject);
            }
            paymentData.put("products", productsArray);

            // Gửi yêu cầu POST lên API
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, paymentApiUrl, paymentData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // ✅ Khi gửi thành công, tiến hành xóa sản phẩm khỏi giỏ hàng
                            for (CartItem item : selectedItems) {
                                CartManager.getInstance().removeItem(item);
                            }
                            Toast.makeText(CheckoutActivity.this, "thanh toán thành công !", Toast.LENGTH_LONG).show();
                            finish(); // Kết thúc màn hình thanh toán
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("API_ERROR", "Lỗi khi gửi dữ liệu lên API: " + error.getMessage());
                    Toast.makeText(CheckoutActivity.this, "Lỗi: Không thể đặt hàng!", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi định dạng dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
}