package com.example.medivault;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public TextView loginText;
    EditText full_name, email, phone_no, pass;
    Button buttonReg;
    ProgressBar Progress_Bar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        loginText = findViewById(R.id.loginText);
        full_name = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone_no = findViewById(R.id.phoneNumber);
        pass = findViewById(R.id.password);
        buttonReg = findViewById(R.id.registerButton);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fullname = full_name.getText().toString();
                String Email = email.getText().toString();
                String Phone = phone_no.getText().toString();
                String Pass = pass.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Pass)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(Email, Pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String uid = firebaseUser.getUid();

                                    // Save data to Realtime Database
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("patent");
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Full Name", Fullname);
                                    map.put("Email", Email);
                                    map.put("Mobile No", Phone);

                                    // Add custom numeric User ID (optional)
                                    String userId = String.valueOf(System.currentTimeMillis());
                                    map.put("UserID", userId);

                                    ref.child(uid).setValue(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Show AlertDialog with user info
                                                        StringBuilder userInfo = new StringBuilder();
                                                        userInfo.append("Account Created Successfully!\n\n");
                                                        userInfo.append("UID: ").append(uid).append("\n");
                                                        userInfo.append("Full Name: ").append(Fullname).append("\n");
                                                        userInfo.append("Email: ").append(Email).append("\n");
                                                        userInfo.append("Mobile No: ").append(Phone);

                                                        new android.app.AlertDialog.Builder(Register.this)
                                                                .setTitle("User Info")
                                                                .setMessage(userInfo.toString())
                                                                .setPositiveButton("Continue", (dialog, which) -> {
                                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                    finish();
                                                                })
                                                                .setCancelable(false)
                                                                .show();
                                                    } else {
                                                        Toast.makeText(Register.this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    Toast.makeText(Register.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Highlight the "Already have an account?" text
        String text = "Already have an account? Log in.";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 22, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)), 25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginText.setText(spannableString);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
