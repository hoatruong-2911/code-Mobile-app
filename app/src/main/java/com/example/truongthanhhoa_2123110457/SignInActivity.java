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

public class SignInActivity extends AppCompatActivity {

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

        // sự kiện bắt nút login qua home cách 1

        Button btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText objemail = findViewById(R.id.editTextEmail);
                String txtemail = objemail.getText().toString();

                EditText objpass = findViewById(R.id.editTextPassword);
                String txtpass = objpass.getText().toString();

                CharSequence text = txtemail + " " + txtpass;
                int duration = Toast.LENGTH_SHORT;

                if(txtemail.equals("hoa") && txtpass.equals("123"))
                {
                    // Sau khi kiểm tra đăng nhập thành công -> vào HomeActivity
                      Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                      intent.putExtra("email",txtemail);
                      intent.putExtra("pass", txtpass);

                      startActivity(intent);
                    //    finish(); // đóng SignInActivity để không quay lại khi bấm Back
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "sai tài khoản hoặc mật khẩu ", duration);
                    toast.show();
                }




            }
        });

        /*  cách 2
         btnLogin.setOnClickListener(v -> {
            // Sau khi kiểm tra đăng nhập thành công -> vào HomeActivity
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);

            //    finish(); // đóng SignInActivity để không quay lại khi bấm Back
        });

        * */



        // 🔹 Bắt sự kiện click nút Create New Account
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
