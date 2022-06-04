package com.example.week1project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button createAccountButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountButton = findViewById(R.id.create_acc_btn);
        createAccountButton.setOnClickListener(this);

        signInButton = findViewById(R.id.sign_in_btn);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_acc_btn:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.sign_in_btn:
                startActivity(new Intent(this, SignInActivity.class));
        }
    }
}