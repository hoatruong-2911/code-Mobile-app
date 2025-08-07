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

    EditText edtEmail, edtPass;
    Button btnLogin;

    // URL API từ MockAPI đã tạo
    String url = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/users";

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

        // Ánh xạ view
        edtEmail = findViewById(R.id.editTextEmail);
        edtPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        // Sự kiện click nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = edtEmail.getText().toString().trim();
                String inputPass = edtPass.getText().toString().trim();

                // Kiểm tra dữ liệu rỗng
                if (inputEmail.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gửi yêu cầu GET đến API Mock để lấy danh sách user
                RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                boolean isLoggedIn = false;

                                // Duyệt mảng JSON trả về
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject user = response.getJSONObject(i);
                                        String email = user.getString("email");
                                        String pass = user.getString("pass");

                                        // So sánh với thông tin người dùng nhập vào
                                        if (inputEmail.equals(email) && inputPass.equals(pass)) {
                                            isLoggedIn = true;

                                            // 👉 Lấy tên người dùng từ "fullName" hoặc "name"
                                            String fullName = user.has("fullName") ? user.getString("fullName") : user.getString("name");

                                            // 👉 Truyền tên sang HomeActivity
                                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                            intent.putExtra("fullName", fullName); // truyền dữ liệu
                                            intent.putExtra("email", email); // 👈 THÊM DÒNG NÀY
                                            startActivity(intent);
                                         //   finish(); // Đóng màn hình login
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Nếu không tìm thấy tài khoản phù hợp
                                if (!isLoggedIn) {
                                    Toast.makeText(SignInActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignInActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                    }
                });

                // Thêm request vào hàng đợi
                queue.add(jsonArrayRequest);
            }
        });

        // 🔹 Bắt sự kiện click nút Create New Account
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
