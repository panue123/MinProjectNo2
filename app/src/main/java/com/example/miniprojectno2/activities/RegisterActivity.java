package com.example.miniprojectno2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojectno2.R;
import com.example.miniprojectno2.dal.AppDB;
import com.example.miniprojectno2.entities.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText edtFullName, edtUsername, edtPassword;
    Button btnRegister, btnBackLogin;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDB.getInstance(this);
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackLogin = findViewById(R.id.btnBackLogin);

        btnRegister.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Validate họ tên: ít nhất 2 từ, không chứa số
            if (!isValidFullName(fullName)) {
                Toast.makeText(this, "Họ tên phải có ít nhất 2 từ và không chứa số", Toast.LENGTH_SHORT).show();
                return;
            }
            if (username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra username đã tồn tại chưa
            if (db.dao().getUserByUsername(username) != null) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.username = username;
            user.fullName = fullName;
            user.password = password;
            user.role = "customer";
            db.dao().insertUser(user);

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnBackLogin.setOnClickListener(v -> finish());
    }

    private boolean isValidFullName(String name) {
        if (name.isEmpty()) return false;
        // Không chứa số
        if (name.matches(".*\\d.*")) return false;
        // Ít nhất 2 từ
        String[] parts = name.trim().split("\\s+");
        return parts.length >= 2;
    }
}

