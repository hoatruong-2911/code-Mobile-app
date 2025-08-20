package com.example.truongthanhhoa_2123110457;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPass;
    Button btnLogin;
    String url = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtPhone = findViewById(R.id.editTextPhone);
        edtPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPhone = edtPhone.getText().toString().trim();
                String inputPass = edtPass.getText().toString().trim();

                if (inputPhone.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                boolean isLoggedIn = false;
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject user = response.getJSONObject(i);
                                        String phone = user.getString("phone");
                                        String pass = user.getString("pass");

                                        if (inputPhone.equals(phone) && inputPass.equals(pass)) {
                                            isLoggedIn = true;

                                            // ✅ Lấy toàn bộ dữ liệu người dùng từ JSON Object
                                            String fullName = user.has("fullName") ? user.getString("fullName") : user.getString("name");
                                            String dob = user.getString("dob");
                                            String gender = user.getString("gender");
                                            String address = user.getString("address");
                                            String email = user.getString("email");
                                            String id = user.getString("id");

                                            // ✅ Tạo Intent và truyền tất cả dữ liệu sang HomeActivity
                                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                            intent.putExtra("id", id);
                                            intent.putExtra("fullName", fullName);
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("dob", dob);
                                            intent.putExtra("gender", gender);
                                            intent.putExtra("address", address);
                                            intent.putExtra("email", email);

                                            startActivity(intent);
                                            finish(); // Kết thúc activity hiện tại
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (!isLoggedIn) {
                                    Toast.makeText(SignInActivity.this, "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignInActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonArrayRequest);
            }
        });

        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}