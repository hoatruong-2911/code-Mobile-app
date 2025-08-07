package com.example.truongthanhhoa_2123110457;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    // Khai báo các trường nhập liệu
    EditText fullName, phone, dob, gender, address, email, password, confirmPassword;
    Button btnSignUp;
    TextView txtGoToLogin;

    // API URL (MockAPI)
    private static final String API_URL = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Ánh xạ các view từ layout
        fullName = findViewById(R.id.editTextFullName);
        phone = findViewById(R.id.editTextPhone);
        dob = findViewById(R.id.editTextDob);
        gender = findViewById(R.id.editTextGender);
        address = findViewById(R.id.editTextAddress);
        email = findViewById(R.id.editTextSignUpEmail);
        password = findViewById(R.id.editTextSignUpPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        txtGoToLogin = findViewById(R.id.textGoToLogin);

        // Bắt sự kiện click nút "Đã có tài khoản? Đăng nhập"
        txtGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        // Bắt sự kiện click nút Đăng ký
        btnSignUp.setOnClickListener(v -> {
            // Kiểm tra người dùng đã nhập đủ thông tin chưa
            if (fullName.getText().toString().isEmpty() ||
                    phone.getText().toString().isEmpty() ||
                    dob.getText().toString().isEmpty() ||
                    gender.getText().toString().isEmpty() ||
                    address.getText().toString().isEmpty() ||
                    email.getText().toString().isEmpty() ||
                    password.getText().toString().isEmpty() ||
                    confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu nhập lại
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            } else {
                // Gọi hàm tạo tài khoản
                createUser();
            }
        });
    }

    // Hàm tạo tài khoản người dùng
    private void createUser() {
        // Lấy dữ liệu từ các EditText
        String fullNameValue = fullName.getText().toString();
        String phoneValue = phone.getText().toString();
        String dobValue = dob.getText().toString();
        String genderValue = gender.getText().toString();
        String addressValue = address.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();

        // Tạo đối tượng JSON để gửi lên API
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fullName", fullNameValue);
            jsonBody.put("phone", phoneValue);
            jsonBody.put("dob", dobValue);
            jsonBody.put("gender", genderValue);
            jsonBody.put("address", addressValue);
            jsonBody.put("email", emailValue);
            jsonBody.put("password", passwordValue);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo dữ liệu JSON!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi POST request đến MockAPI
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                jsonBody,
                response -> {
                    // Khi đăng ký thành công
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Điều hướng sang màn hình đăng nhập
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc SignUpActivity để không quay lại bằng nút Back
                },
                error -> {
                    // Khi có lỗi xảy ra
                    Toast.makeText(this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Tạo hàng đợi request và thêm request vào
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    }
