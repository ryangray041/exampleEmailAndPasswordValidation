package com.example.week1project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton;
    private EditText emailValidate;
    private EditText passwordValidate;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = findViewById(R.id.sign_in_btn_2);
        signInButton.setOnClickListener(this);

        emailValidate = findViewById(R.id.input_email);
        passwordValidate = findViewById(R.id.input_password);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_btn_2) {
            userLogin();
        }
    }

    private boolean isEmailValid(EditText emailValidate){
        String email = emailValidate.getText().toString();
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(EditText passwordValidate){
        String password = passwordValidate.getText().toString();
        return !password.isEmpty() && PASSWORD_PATTERN.matcher(password).matches();
    }

    private void userLogin() {
        String email = emailValidate.getText().toString();
        String password = passwordValidate.getText().toString();

        //noinspection deprecation
        @SuppressLint("UseCompatLoadingForDrawables") Drawable customErrorDrawable = getResources().getDrawable(R.drawable.cross);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());

        if (email.isEmpty()) {
            emailValidate.setError("Email cannot be blank", customErrorDrawable);
            emailValidate.requestFocus();
            return;
        }
        if (!isEmailValid(emailValidate)) {
            emailValidate.setError("Enter a valid email", customErrorDrawable);
            emailValidate.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordValidate.setError("Password cannot be blank", customErrorDrawable);
            passwordValidate.requestFocus();
            return;
        }
        if (!isPasswordValid(passwordValidate)) {
            passwordValidate.setError("Invalid password", customErrorDrawable);
            passwordValidate.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()){
                                Toast.makeText(SignInActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, UserProfileActivity.class));
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(SignInActivity.this, "Email verification has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignInActivity.this, "Failed to Login! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}