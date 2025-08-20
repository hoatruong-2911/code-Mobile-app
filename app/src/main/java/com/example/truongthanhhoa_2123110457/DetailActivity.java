// DetailActivity.java
package com.example.truongthanhhoa_2123110457;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailTitle, detailDescription, detailRatingCount;
    private TextView detailTotalPrice, txtQuantity;
    private Button btnOrderNow, btnMinus, btnPlus;
    private RatingBar detailRating;

    private String productId;
    private String url = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/products/";

    // Biến lưu trữ số lượng được chọn
    private int quantity = 1;
    // Biến để lưu trữ thông tin sản phẩm sau khi tải từ API
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ánh xạ view
        detailImage = findViewById(R.id.detail_image);
        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        detailRating = findViewById(R.id.detail_rating);
        detailRatingCount = findViewById(R.id.detail_rating_count);
        btnOrderNow = findViewById(R.id.btn_order_now);

        detailTotalPrice = findViewById(R.id.detail_total_price);
        txtQuantity = findViewById(R.id.txt_quantity);
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);

        // Lấy productId từ intent
        productId = getIntent().getStringExtra("productId");

        // Load dữ liệu từ API
        if (productId != null && !productId.isEmpty()) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        // ✅ Xử lý nút "Thêm vào giỏ hàng"
        btnOrderNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                // Truyền đối tượng sản phẩm và số lượng đã chọn
                CartManager.getInstance().addItem(currentProduct, quantity);
                Toast.makeText(this, "Đã thêm " + quantity + " " + currentProduct.getTitle() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không thể thêm sản phẩm vào giỏ hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện cho nút cộng trừ
        setupQuantityButtons();
    }

    private void loadProductDetail(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id,
                response -> {
                    try {
                        JSONObject product = new JSONObject(response);

                        // Tạo đối tượng Product và lưu vào biến currentProduct
                        String productId = product.getString("id");
                        String title = product.getString("title");
                        double price = product.getDouble("price");
                        String image = product.getString("image");
                        String category = product.getString("category");
                        currentProduct = new Product(productId, title, price, image, category);

                        // Cập nhật giao diện
                        detailTitle.setText(currentProduct.getTitle());
                        detailDescription.setText(product.getString("description"));

                        updateTotalPrice();

                        if (product.has("rating")) {
                            JSONObject ratingObj = product.getJSONObject("rating");
                            detailRating.setRating((float) ratingObj.getDouble("rate"));
                            detailRatingCount.setText("(" + ratingObj.getInt("count") + ")");
                        } else {
                            detailRating.setRating(0);
                            detailRatingCount.setText("(0)");
                        }

                        Glide.with(this)
                                .load(currentProduct.getImage())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_background)
                                .into(detailImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu sản phẩm: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Lỗi kết nối API: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return com.android.volley.Response.success(parsed, com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }

    // Hàm xử lý sự kiện cho nút cộng trừ
    private void setupQuantityButtons() {
        btnPlus.setOnClickListener(v -> {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            } else {
                Toast.makeText(this, "Số lượng không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cập nhật tổng giá tiền
    private void updateTotalPrice() {
        double totalPrice = (currentProduct != null ? currentProduct.getPrice() : 0.0) * quantity;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        detailTotalPrice.setText(currencyFormatter.format(totalPrice * 25000));
    }
}