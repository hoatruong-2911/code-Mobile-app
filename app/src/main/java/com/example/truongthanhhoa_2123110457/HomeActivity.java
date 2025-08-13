package com.example.truongthanhhoa_2123110457;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {



    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ViewPager2 bannerViewPager;
    private TabLayout bannerIndicator;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://fakestoreapi.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // -------- hiện thị banner
        // 🔹 1. Ánh xạ View
        bannerViewPager = findViewById(R.id.bannerViewPager);
        bannerIndicator = findViewById(R.id.bannerIndicator);

        // 🔹 2. Tạo danh sách ảnh banner từ drawable
        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.java);        // ảnh java.png
        bannerImages.add(R.drawable.python);      // ảnh python.png
        bannerImages.add(R.drawable.javascript);  // ảnh javascript.png

        // 🔹 3. Gắn Adapter cho ViewPager2
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
//        // Auto slide mỗi 3 giây
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                int currentItem = bannerViewPager.getCurrentItem();
//                int totalItem = bannerAdapter.getItemCount();
//                int nextItem = (currentItem + 1) % totalItem; // quay lại ảnh đầu
//                bannerViewPager.setCurrentItem(nextItem, true);
//                handler.postDelayed(this, 3000); // 3 giây đổi ảnh
//            }
//        };
//        handler.postDelayed(runnable, 3000);

        // 🔹 4. Kết nối TabLayout indicator với ViewPager2
        new TabLayoutMediator(bannerIndicator, bannerViewPager,
                (tab, position) -> {
                    // Không cần set text cho tab, chỉ làm chấm tròn
                }).attach();
        //-------------------------------

        // 🔹 Nhận dữ liệu từ Intent khi đăng nhập thành công
        Intent intent = getIntent();
        String fullName = intent.getStringExtra("fullName"); // Lấy tên từ SignInActivity
        String phone = intent.getStringExtra("phone");       // Có thể cần nếu muốn hiển thị số điện thoại

        // 🔹 Hiển thị lời chào ngay lập tức mà KHÔNG cần gọi lại API
        if (fullName != null && !fullName.isEmpty()) {
            TextView txtWelcome = findViewById(R.id.txtWelcome);
            txtWelcome.setText("Xin chào\n" + fullName + " !");
        }

        // 🔹 Giữ nguyên phần BottomNavigation như cũ
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                ListView mainContent = findViewById(R.id.list);
                mainContent.smoothScrollToPosition(0);
                return true;
            } else if (id == R.id.nav_order) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });

        // ❌ Bỏ gọi getData() vì đã có fullName từ Intent
        // getData();

        loadProductListFromApi();



    }

    // ❌ Hàm getData() không cần thiết nữa, nhưng nếu muốn dùng lại thì vẫn giữ
    private void getData() {
        String emailFromLogin = getIntent().getStringExtra("email");
        String userApiUrl = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/user";

        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, userApiUrl,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            String email = user.getString("email");

                            if (email.equals(emailFromLogin)) {
                                String fullName = "";
                                if (user.has("fullName")) {
                                    fullName = user.getString("fullName");
                                } else if (user.has("name")) {
                                    fullName = user.getString("name");
                                }
                                TextView txtWelcome = findViewById(R.id.txtWelcome);
                                txtWelcome.setText("Xin chào\n" + fullName + " !");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.i(TAG, "Error: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                });

        mRequestQueue.add(mStringRequest);



    }

    //-------------------------------------

    // 🆕 Hàm mới: Lấy dữ liệu sản phẩm từ API và hiển thị lên ListView
    private void loadProductListFromApi() {
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<String> imageUrls = new ArrayList<>();
                        ArrayList<String> prices = new ArrayList<>();
                        ArrayList<String> categories = new ArrayList<>();
                        ArrayList<String> ratings = new ArrayList<>();

                        int itemCount = Math.min(jsonArray.length(), 10);
                        for (int i = 0; i < itemCount; i++) {
                            JSONObject productObj = jsonArray.getJSONObject(i);

                            titles.add(productObj.getString("title"));
                            imageUrls.add(productObj.getString("image"));
                            prices.add("Price: $" + productObj.getDouble("price"));
                            categories.add("Category: " + productObj.getString("category"));

                            JSONObject ratingObj = productObj.getJSONObject("rating");
                            ratings.add("Rating: " + ratingObj.getDouble("rate") + " (" + ratingObj.getInt("count") + ")");
                        }

                        ListView listView = findViewById(R.id.list);
                        CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, titles, imageUrls, prices, categories, ratings);
                        listView.setAdapter(customAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Lỗi xử lý dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.i(TAG, "Error: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối API sản phẩm", Toast.LENGTH_SHORT).show();
                });

        mRequestQueue.add(mStringRequest);
    }




}
