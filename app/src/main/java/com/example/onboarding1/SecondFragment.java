package com.example.onboarding1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    ViewPager viewPager;
    TextView next;
    TextView back;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_second, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);
        next=view.findViewById(R.id.textViewNext);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewPager.setCurrentItem(2);
            }
            });
        back=view.findViewById(R.id.textViewBack);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewPager.setCurrentItem(0);
            }
        });
        return view;
    }

}
