package com.example.truongthanhhoa_2123110457;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    // Khai báo biến để lưu trữ dữ liệu người dùng
    private Bundle userBundle;

    // Các biến khác của bạn
    RecyclerView rcvCategories;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;
    RecyclerView rcvProducts;
    ProductAdapter adapter;
    List<Product> productList;
    EditText edtSearch;
    String url = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/products";
    private ViewPager2 bannerViewPager;
    private TabLayout bannerIndicator;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

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

        // ----------------- BANNER -----------------
        bannerViewPager = findViewById(R.id.bannerViewPager);
        bannerIndicator = findViewById(R.id.bannerIndicator);

        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.java);
        bannerImages.add(R.drawable.python);
        bannerImages.add(R.drawable.javascript);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(bannerIndicator, bannerViewPager, (tab, position) -> {}).attach();
        // -------------------------------------------

        // ----------------- LỜI CHÀO -----------------
        // ✅ Lấy toàn bộ dữ liệu người dùng từ Intent và lưu vào userBundle
        userBundle = getIntent().getExtras();
        if (userBundle != null) {
            String fullName = userBundle.getString("fullName");
            TextView txtWelcome = findViewById(R.id.txtWelcome);
            if (fullName != null && !fullName.isEmpty()) {
                txtWelcome.setText("Xin chào\n" + fullName + " !");
            }
        }
        // --------------------------------------------

        // ----------------- BOTTOM NAV -----------------
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                rcvProducts.smoothScrollToPosition(0);
                return true;
            } else if (id == R.id.nav_order) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                // ✅ Kiểm tra dữ liệu và chuyển sang ProfileActivity
                if (userBundle != null) {
                    Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                    profileIntent.putExtras(userBundle); // Truyền Bundle chứa dữ liệu người dùng
                    startActivity(profileIntent);
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
        // ---------------------------------------------

        // ----------------- SETUP RECYCLER VIEW -----------------
        edtSearch = findViewById(R.id.searchBarTitle);
        rcvProducts = findViewById(R.id.rcvProducts);
        rcvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        rcvProducts.setAdapter(adapter);

        loadProducts();
        // --------------------------------------------------------
    }

    // ----------------- LOAD API SẢN PHẨM -----------------
    private void loadProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            productList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String id = obj.getString("id");
                                String title = obj.getString("title");
                                double price = obj.getDouble("price");
                                String image = obj.getString("image");
                                String category = obj.getString("category");

                                productList.add(new Product(id, title, price, image, category));
                            }
                            adapter.notifyDataSetChanged();
                            setupCategories(response);
                        } catch (Exception e) {
                            Log.e("PARSE_ERROR", "Lỗi parse dữ liệu JSON: " + e.getMessage(), e);
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "Lỗi parse dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API_ERROR", "Lỗi kết nối API: " + error.toString(), error);
                if (error.networkResponse != null) {
                    Log.e("API_ERROR", "Mã HTTP trả về: " + error.networkResponse.statusCode);
                    Log.e("API_ERROR", "Nội dung trả về: " + new String(error.networkResponse.data));
                }
                Toast.makeText(HomeActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void setupCategories(JSONArray productsJson) {
        try {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < productsJson.length(); i++) {
                JSONObject obj = productsJson.getJSONObject(i);
                String category = obj.getString("category");
                String image = obj.getString("image");

                if (!map.containsKey(category)) {
                    map.put(category, image);
                }
            }

            categoryList = new ArrayList<>();
            categoryList.add(new Category("Tất cả sản phẩm", "https://cdn-icons-png.flaticon.com/512/2099/2099058.png"));

            for (Map.Entry<String, String> entry : map.entrySet()) {
                categoryList.add(new Category(entry.getKey(), entry.getValue()));
            }

            rcvCategories = findViewById(R.id.rcvCategories);
            rcvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            categoryAdapter = new CategoryAdapter(this, categoryList, category -> {
                if (category.getName().equals("Tất cả sản phẩm")) {
                    adapter.updateList(productList);
                } else {
                    List<Product> filtered = new ArrayList<>();
                    for (Product p : productList) {
                        if (p.getCategory().equals(category.getName())) {
                            filtered.add(p);
                        }
                    }
                    adapter.updateList(filtered);
                }
            });
            rcvCategories.setAdapter(categoryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}