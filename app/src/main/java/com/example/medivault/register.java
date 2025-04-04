package com.example.medivault;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class register extends AppCompatActivity {
    public TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        loginText = findViewById(R.id.loginText);

        // Highlight the "Already have an account?" text
        String text = "Already have an account? Log in.";
        SpannableString spannableString = new SpannableString(text);

        // Apply an underline to the "Log in" part of the text
        spannableString.setSpan(new UnderlineSpan(), 22, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Change the color of "Log in" to blue (or any other color you like)
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)), 25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginText.setText(spannableString);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this,MainActivity.class);
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