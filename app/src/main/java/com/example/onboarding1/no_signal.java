package com.example.onboarding1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class no_signal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_signal);

        ImageView imageView = findViewById(R.id.img);
        Glide.with(this).load(R.drawable.no_internet_gif).into(imageView);
    }
}
