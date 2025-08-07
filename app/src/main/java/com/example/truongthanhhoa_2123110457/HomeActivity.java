package com.example.truongthanhhoa_2123110457;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

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

        //lấy dữ liệu đã truyền từ SignInActivity qua bằng Intent.
        Intent intent = getIntent();
        String txtEmail = intent.getStringExtra("email");
        String txtPass = intent.getStringExtra("pass");

        // Nhận dữ liệu từ Intent

        String fullName = intent.getStringExtra("fullName");

        //Lấy dữ liệu từ Intent và set vào TextView trong HomeActivity
//        TextView txtWelcome = findViewById(R.id.txtWelcome);
//        txtWelcome.setText("Xin chào " + txtEmail);


        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                ScrollView mainContent = findViewById(R.id.mainContent);
                mainContent.fullScroll(View.FOCUS_UP);
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


        getData();
    }

    private void getData() {
        String emailFromLogin = getIntent().getStringExtra("email");

        // Dùng lại API user từ MockAPI
        String userApiUrl = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/users";

        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, userApiUrl,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            String email = user.getString("email");

                            // So sánh đúng người dùng đang đăng nhập
                            if (email.equals(emailFromLogin)) {
                                String fullName = "";

                                // Một số user có key là "name", một số là "fullName"
                                if (user.has("fullName")) {
                                    fullName = user.getString("fullName");
                                } else if (user.has("name")) {
                                    fullName = user.getString("name");
                                }

                                TextView txtWelcome = findViewById(R.id.txtWelcome);
                                txtWelcome.setText("Xin chào\n " + fullName + " !");
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

}