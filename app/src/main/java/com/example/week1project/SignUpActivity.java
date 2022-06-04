package com.example.week1project;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    private EditText emailValidate;
    private EditText passwordValidate;
    private EditText confirmPassword;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailValidate = findViewById(R.id.input_email);
        passwordValidate = findViewById(R.id.input_password);
        confirmPassword = findViewById(R.id.input_password_again);

        signUpButton = findViewById(R.id.create_acc_btn2);
        signUpButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.create_acc_btn2) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = emailValidate.getText().toString();
        String password = passwordValidate.getText().toString();
        String passwordAgain = confirmPassword.getText().toString();

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
        if (passwordAgain.isEmpty()) {
            confirmPassword.setError("Password cannot be blank", customErrorDrawable);
            confirmPassword.requestFocus();
            return;
        }
        if (!doesPasswordMatch(passwordValidate, confirmPassword)) {
            confirmPassword.setError("Password does not match", customErrorDrawable);
            confirmPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "User failed to register, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, "User failed to register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean isEmailValid(EditText emailValidate) {
        String email = emailValidate.getText().toString();
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(EditText passwordValidate) {
        String password = passwordValidate.getText().toString();
        return !password.isEmpty() && PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean doesPasswordMatch(EditText passwordValidate, EditText confirmPassword) {
        String password = passwordValidate.getText().toString();
        String passwordAgain = confirmPassword.getText().toString();
        return !passwordAgain.isEmpty() && password.equals(passwordAgain);
    }
}