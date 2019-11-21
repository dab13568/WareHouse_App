package com.example.onboarding1;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {
    ViewPager viewPager;
    TextView back;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_third, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);
        back=view.findViewById(R.id.textViewBack);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewPager.setCurrentItem(1);
            }
        });
        return view;
    }

}
