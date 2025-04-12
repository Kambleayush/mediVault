package com.example.medivault;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private Button addRecord, backlogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        backlogin = findViewById(R.id.backlogin);
        addRecord = findViewById(R.id.addrecord);

        // Check if the user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // If user is not logged in, go directly to login screen
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Set click listener for Add Record button
        addRecord.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, add_record.class);
            startActivity(intent);
        });

        // Set click listener for Back to Login button
        backlogin.setOnClickListener(view -> {
            // Log out the user
            FirebaseAuth.getInstance().signOut();

            // Navigate to the login screen
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the Home activity
        });

        // Handle system bar insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
