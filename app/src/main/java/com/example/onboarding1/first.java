package com.example.onboarding1;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.LinearLayout;

public class first extends AppCompatActivity {
   ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        viewPager=findViewById(R.id.viewPager);
        sliderAdapter adapter=new sliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

}
