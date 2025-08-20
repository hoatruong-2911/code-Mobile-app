// File: app/src/main/java/com/example/truongthanhhoa_2123110457/activities/ProfileActivity.java

package com.example.truongthanhhoa_2123110457.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.truongthanhhoa_2123110457.R;

public class ProfileActivity extends AppCompatActivity {

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

        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvDob = findViewById(R.id.tvDob);
        tvGender = findViewById(R.id.tvGender);
        tvAddress = findViewById(R.id.tvAddress);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fullName = extras.getString("fullName", "N/A");
            String phone = extras.getString("phone", "N/A");
            String email = extras.getString("email", "N/A");
            String dob = extras.getString("dob", "N/A");
            String gender = extras.getString("gender", "N/A");
            String address = extras.getString("address", "N/A");

            // ✅ Lưu thông tin người dùng vào SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fullName", fullName);
            editor.putString("phone", phone);
            editor.putString("address", address);
            editor.apply();

            tvFullName.setText(fullName);
            tvPhone.setText("Số điện thoại: " + phone);
            tvEmail.setText("Email: " + email);
            tvDob.setText("Ngày sinh: " + dob);
            tvGender.setText("Giới tính: " + gender);
            tvAddress.setText("Địa chỉ: " + address);
        }
    }
}