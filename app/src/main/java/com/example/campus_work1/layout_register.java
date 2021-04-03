package com.example.campus_work1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class layout_register extends AppCompatActivity {

    private EditText Enrollment,Email,Username,Password;
    private TextView login;
    private Button register;
    private ProgressBar progressBar;
    private ProgressDialog pd;
    private FirebaseAuth auth;
   private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_register);

        auth = FirebaseAuth.getInstance();

        login = (TextView) findViewById(R.id.LOGINHERE);
        register = (Button) findViewById(R.id.register);
        Enrollment = (EditText) findViewById(R.id.editTextEnroll);
        Email = (EditText) findViewById(R.id.editTextEmail);
        Username = (EditText) findViewById(R.id.editTextUsername);
        Password = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.LOGINHERE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(layout_register.this, layout_login.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enrollment = Enrollment.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String username = Username.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(enrollment)) {
                    Toast.makeText(getApplicationContext(), "Enter Enrollment!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(layout_register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(layout_register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(layout_register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }

                                else if(task.isSuccessful())
                                {
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    String userId=firebaseUser.getUid();


                                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("id",userId);
                                    hashMap.put("fullname",username);
                                    hashMap.put("username",email.toLowerCase());
                                    hashMap.put("bio","");
                                    hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/instagram-21814.appspot.com/o/myphoto.jfif?alt=media&token=3e8acd86-614e-4b27-a4c2-1bf75b417e53");

                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                pd.dismiss();
                                                Intent intent = new Intent(layout_register.this,layout_login.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                else {
                                    startActivity(new Intent(layout_register.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
