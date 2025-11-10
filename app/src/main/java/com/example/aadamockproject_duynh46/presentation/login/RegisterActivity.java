package com.example.aadamockproject_duynh46.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aadamockproject_duynh46.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button btn_register;
    TextView tv_Login;
    EditText et_Email;
    EditText  et_Password;
    EditText  et_ConfirmPassword;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_register = findViewById(R.id.btnRegister);
        tv_Login = findViewById(R.id.tvLoginLink);
        et_Password = findViewById(R.id.etPassword);
        et_ConfirmPassword = findViewById(R.id.etConfirmPassword);
        et_Email = findViewById(R.id.etEmail);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnRegister) {
                    String email = et_Email.getText().toString().trim();
                    String password = et_Password.getText().toString().trim();
                    String confirm = et_ConfirmPassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Yêu cầu nhập đầy đủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isConfirmTrue(password, confirm)) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_LONG).show();
                        return;
                    }

                    registerWithFirebase(email, password);
                }
            }
        });

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tvLoginLink) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                }

            }
        });



    }

    public boolean isConfirmTrue(String pass1, String pass2) {
        if(!pass1.equals(pass2)) {
            return false;
        }
        return true;
    }


    private void registerWithFirebase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this,task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Lưu Database nhưng không block màn hình
                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("uid", user.getUid());
                        FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(user.getUid())
                                .setValue(userData);

                        // Chuyển màn hình ngay
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "Email này đã tồn tại!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Đăng ký thất bại",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}