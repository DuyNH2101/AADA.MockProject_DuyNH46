package com.example.aadamockproject_duynh46.presentation.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText et_Email;
    private EditText et_Password;
    private LinearLayout layout_googleLogin;
    private Button btn_Login;

    private TextView tv_Register;

    private static final int RC_SIGN_IN = 9001;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // neu user da dang nhap vao tu phien truoc thi su dung user luon
        }


        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        et_Email = findViewById(R.id.etEmail);
        et_Password = findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btnLogin);
        layout_googleLogin = findViewById(R.id.btnGoogleLogin);
        tv_Register = findViewById(R.id.tvRegister);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_Email.getText().toString().trim();
                String pass = et_Password.getText().toString().trim();
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                OnclickLogin(email, pass);
            }
        });

        layout_googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnGoogleLogin) {
                    signInWithGoole();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
                };
                // TODO
            }
        });

        tv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == tv_Register.getId()) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleSign", "signInResult:failed code=" + e.getStatusCode());
            Log.w("GoogleSignE", "signInResult:failed code=" + e);
            updateUI(null);
        }
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            // Lấy thông tin người dùng từ tài khoản Google
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();

            // Log để kiểm tra
            Log.d("GoogleSign", "Name: " + personName + ", Email: " + personEmail);

            // Tạo intent để chuyển sang màn hình khác (ví dụ: MainActivity)
            Intent intent = new Intent(this, MainActivity.class);

            // Gửi thông tin người dùng sang MainActivity (nếu cần)
            intent.putExtra("name", personName);
            intent.putExtra("email", personEmail);
            if (personPhoto != null) {
                intent.putExtra("photoUrl", personPhoto.toString());
            }
            // Xóa lịch sử back stack để không quay lại màn đăng nhập
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Kết thúc activity đăng nhập
            finish();
        } else {
            // Đăng nhập thất bại → hiển thị thông báo
            Toast.makeText(this, "Đăng nhập Google thất bại, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInWithGoole() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void OnclickLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Main", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                    //updateUI(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Email or Password is incorrect!",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }


}