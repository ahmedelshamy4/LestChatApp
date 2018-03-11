package com.example.ahmed.lestchatapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewForget, textViewRegistered;
    FirebaseAuth auth;
    // FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initViews();

//        listener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//                    finish();
//                }
        //  }
        //  };
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.edit_txt_email);
        editTextPassword = findViewById(R.id.edit_txt_password);
        buttonLogin = findViewById(R.id.login_up_button);
        textViewForget = findViewById(R.id.tv_forget);
        textViewRegistered = findViewById(R.id.tv_Register);

        buttonLogin.setOnClickListener(this);
        textViewRegistered.setOnClickListener(this);
        textViewForget.setOnClickListener(this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(listener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        auth.addAuthStateListener(listener);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_up_button:
                Login();
                break;
            case R.id.tv_forget:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                Toast.makeText(this, "Forget Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_Register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                Toast.makeText(this, "Register Activity", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void Login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter email address!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("email_not_formatted");
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter password!");
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        } else {
            Utilities.showLoadingDialog(this, Color.WHITE);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Utilities.dismissLoadingDialog();
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User has been login", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                        intent.putExtra("Email",auth.getCurrentUser().getEmail());
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
