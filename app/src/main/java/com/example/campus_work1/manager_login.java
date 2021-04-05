package com.example.campus_work1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class manager_login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputEmail.getText().toString().equals("admin@gmail.com") && inputPassword.getText().toString().equals("admin@123")){
                    startActivity(new Intent(manager_login.this,Dashboard.class));
                    finish();
                    Toast.makeText(manager_login.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(manager_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}