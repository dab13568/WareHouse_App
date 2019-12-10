package com.example.onboarding1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class delivered_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_activity);


        ImageView imageView = findViewById(R.id.img);
        Glide.with(this).load(R.drawable.delivered_gif).into(imageView);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent =new Intent(delivered_activity.this, AddPackage.class);
                startActivity(intent);
                finish();
            }
        }, 3450);
    }
}
