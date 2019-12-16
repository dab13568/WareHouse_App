package com.example.onboarding1;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements TextWatcher {
    TextView next;
    ViewPager viewPager;
    public  static EditText phone;
    public static EditText name;
    public static EditText addressee;


    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_first, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);

        addressee= view.findViewById(R.id.address_parcel_addressee);
        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.phone_number);
        next=view.findViewById(R.id.textViewNext);

        name.addTextChangedListener(this);
        phone.addTextChangedListener(this);
        addressee.addTextChangedListener(this);


        next=view.findViewById(R.id.textViewNext);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewPager.setCurrentItem(1);
            }
        });
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(name.getText().toString().equals( ""))
            AddPackage.valid_name=false;
        else AddPackage.valid_name=true;

        if(addressee.getText().toString().equals( ""))
            AddPackage.addresse=false;
        else AddPackage.addresse=true;

        if(!(phone.getText().toString().matches("05[0-9]{8}"))) {
            AddPackage.valid_phone = false;
            phone.setTextColor(Color.parseColor("#ff0000"));
        }
        else {
            AddPackage.valid_phone = true;
            phone.setTextColor(Color.parseColor("#040E69"));
        }
        SecondFragment.valid();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(name.getText().toString().equals( ""))
            AddPackage.valid_name=false;
        else AddPackage.valid_name=true;

        if(addressee.getText().toString().equals( ""))
            AddPackage.addresse=false;
        else AddPackage.addresse=true;


        if(!(phone.getText().toString().matches("05[0-9]{8}"))) {
            AddPackage.valid_phone = false;
            phone.setTextColor(Color.parseColor("#ff0000"));
        }
        else {
            AddPackage.valid_phone=true;
            phone.setTextColor(Color.parseColor("#040E69"));
        }
        SecondFragment.valid();
    }
}
