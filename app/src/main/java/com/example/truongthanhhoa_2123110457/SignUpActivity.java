package com.example.truongthanhhoa_2123110457;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText fullName, phone, dob, address, email, password, confirmPassword;
    RadioGroup radioGroupGender;
    RadioButton radioMale, radioFemale;
    Button btnSignUp;
    TextView txtGoToLogin;

    // API chứa dữ liệu user
    private static final String API_URL = "https://68940f0ebe3700414e11e224.mockapi.io/logIncrete/user";

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

        // ================== ÁNH XẠ VIEW ==================
        fullName = findViewById(R.id.editTextFullName);
        phone = findViewById(R.id.editTextPhone);
        dob = findViewById(R.id.editTextDob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        address = findViewById(R.id.editTextAddress);
        email = findViewById(R.id.editTextSignUpEmail);
        password = findViewById(R.id.editTextSignUpPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        txtGoToLogin = findViewById(R.id.textGoToLogin);

        // ================== CHUYỂN SANG MÀN ĐĂNG NHẬP ==================
        txtGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        // ================== XỬ LÝ NÚT ĐĂNG KÝ ==================
        btnSignUp.setOnClickListener(v -> {
            // 1. Kiểm tra các trường bắt buộc
            if (fullName.getText().toString().isEmpty() ||
                    phone.getText().toString().isEmpty() ||
                    dob.getText().toString().isEmpty() ||
                    address.getText().toString().isEmpty() ||
                    email.getText().toString().isEmpty() ||
                    password.getText().toString().isEmpty() ||
                    confirmPassword.getText().toString().isEmpty()) {

                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Kiểm tra đã chọn giới tính chưa
            if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Validate số điện thoại: phải bắt đầu bằng 0 và đủ 10 số
            String phoneInput = phone.getText().toString().trim();
            if (!phoneInput.matches("^0\\d{9}$")) {
                Toast.makeText(this, "Số điện thoại không hợp lệ — phải bắt đầu bằng 0 và đủ 10 chữ số", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
                return;
            }

            // 4. Kiểm tra mật khẩu khớp
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 5. Bắt đầu kiểm tra trùng trên API (Phone -> FullName -> Email)
            checkPhoneExists(phoneInput);
        });
    }

    // ================== HÀM KIỂM TRA SỐ ĐIỆN THOẠI TỒN TẠI ==================
    private void checkPhoneExists(String phoneNumber) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    boolean exists = false;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject user = response.getJSONObject(i);
                            String apiPhone = user.getString("phone");

                            if (apiPhone.equals(phoneNumber)) {
                                exists = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (exists) {
                        Toast.makeText(this, "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
                        phone.requestFocus();
                    } else {
                        // Nếu phone chưa tồn tại -> kiểm tra tên
                        checkFullNameExists(fullName.getText().toString().trim());
                    }
                },
                error -> Toast.makeText(this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    // ================== HÀM KIỂM TRA FULLNAME TỒN TẠI ==================
    private void checkFullNameExists(String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    boolean exists = false;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject user = response.getJSONObject(i);
                            String apiName = user.has("fullName") ? user.getString("fullName") : user.optString("name");

                            if (apiName.equalsIgnoreCase(name)) {
                                exists = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (exists) {
                        Toast.makeText(this, "Tên này đã được đăng ký!", Toast.LENGTH_SHORT).show();
                        fullName.requestFocus();
                    } else {
                        // Nếu tên chưa tồn tại -> kiểm tra email
                        checkEmailExists(email.getText().toString().trim());
                    }
                },
                error -> Toast.makeText(this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    // ================== HÀM KIỂM TRA EMAIL TỒN TẠI ==================
    private void checkEmailExists(String emailInput) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    boolean exists = false;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject user = response.getJSONObject(i);
                            String apiEmail = user.getString("email");

                            if (apiEmail.equalsIgnoreCase(emailInput)) {
                                exists = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (exists) {
                        Toast.makeText(this, "Email này đã được đăng ký!", Toast.LENGTH_SHORT).show();
                        email.requestFocus();
                    } else {
                        // Nếu không trùng bất cứ thứ gì -> tạo user
                        createUser();
                    }
                },
                error -> Toast.makeText(this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    // ================== HÀM TẠO USER MỚI ==================
    private void createUser() {
        String fullNameValue = fullName.getText().toString();
        String phoneValue = phone.getText().toString();
        String dobValue = dob.getText().toString();
        String genderValue = radioMale.isChecked() ? "Nam" : "Nữ";
        String addressValue = address.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fullName", fullNameValue);
            jsonBody.put("phone", phoneValue);
            jsonBody.put("dob", dobValue);
            jsonBody.put("gender", genderValue);
            jsonBody.put("address", addressValue);
            jsonBody.put("email", emailValue);
            jsonBody.put("pass", passwordValue);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo dữ liệu JSON!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                jsonBody,
                response -> {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                },
                error -> Toast.makeText(this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
