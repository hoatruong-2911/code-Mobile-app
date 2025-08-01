package com.example.truongthanhhoa_2123110457;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

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


        // 🔹 Bắt sự kiện click nút đã có tài khoảng chuyển qua LOgin
        TextView txtGoToLogin = findViewById(R.id.textGoToLogin);

        txtGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        // bắt nút Sign Up qua Login
        Button btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(v -> {
            // Sau này bạn sẽ thêm kiểm tra dữ liệu ở đây (Email, Password,...)

            // Tạm thời điều hướng sang HomeActivity
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);

            // Nếu muốn đóng SignUp để không quay lại khi bấm Back
            // finish();
        });

    }
}