package com.example.truongthanhhoa_2123110457;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    // Khai báo các TextView để hiển thị thông tin người dùng
    private TextView tvFullName, tvPhone, tvEmail, tvDob, tvGender, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các TextView từ file layout
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvDob = findViewById(R.id.tvDob);
        tvGender = findViewById(R.id.tvGender);
        tvAddress = findViewById(R.id.tvAddress);

        // Lấy dữ liệu từ Intent đã được gửi từ SignInActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fullName = extras.getString("fullName", "N/A");
            String phone = extras.getString("phone", "N/A");
            String email = extras.getString("email", "N/A");
            String dob = extras.getString("dob", "N/A");
            String gender = extras.getString("gender", "N/A");
            String address = extras.getString("address", "N/A");

            // Gán dữ liệu nhận được vào các TextView
            tvFullName.setText( fullName);
            tvPhone.setText("Số điện thoại: " + phone);
            tvEmail.setText("Email: " + email);
            tvDob.setText("Ngày sinh: " + dob);
            tvGender.setText("Giới tính: " + gender);
            tvAddress.setText("Địa chỉ: " + address);
        }
    }
}