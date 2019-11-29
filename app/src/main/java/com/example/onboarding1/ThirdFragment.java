package com.example.onboarding1;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationResult;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class ThirdFragment extends Fragment
{
    ViewPager viewPager;
    TextView back;
    View view;


    public ThirdFragment() {
        // Required empty public constructor
         }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_third, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);

        final TextView textView=view.findViewById(R.id.address);

        SharedPreferences prefs = getContext().getSharedPreferences("maps", MODE_PRIVATE);
        String name = prefs.getString("add", "No name defined");
        textView.setText(name);

        return view;


    }


}