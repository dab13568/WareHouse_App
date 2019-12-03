package com.example.onboarding1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {

    ViewPager viewPager;
    TextView next;
    TextView back;
    Spinner type_package;
    Spinner breakable;
    static TextView address;
    static TextView address_icon;
    static SharedPreferences prefs;
    EditText weight;
    static ImageButton add_package;
    static MainActivity mainActivity;
    static TextView error_message;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_second, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);

        weight = view.findViewById(R.id.weight);
        add_package=view.findViewById(R.id.add_package);
        breakable=view.findViewById(R.id.breakable);
        type_package=view.findViewById(R.id.package_type);
        address = view.findViewById(R.id.address);
        address_icon = view.findViewById(R.id.address_icon);
        error_message=view.findViewById(R.id.missing_details);
        back=view.findViewById(R.id.textViewBack);

        add_package.setEnabled(false);
        error_message.setVisibility(View.VISIBLE);
        add_package.setImageResource(R.drawable.error);

        //sign up the fields to Text/selection Changed
        breakable.setOnItemSelectedListener(this);
        type_package.setOnItemSelectedListener(this);
        address.addTextChangedListener(this);
        weight.addTextChangedListener(this);

        mainActivity = (MainActivity) getActivity();

        //put the address in the fragment
        prefs = getContext().getSharedPreferences("maps", MODE_PRIVATE);
        final String name = prefs.getString("add", "GPS Error");
        address.setText(name);
        address = view.findViewById(R.id.address);

        //change the color of the address
        if (name == "" || name == "לא ניתן למצוא את מיקומך הנוכחי" || name == "נא הפעל GPS" || name == "Permission Denied...") {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_off, 0, 0, 0);
            address.setTextColor(Color.parseColor("#FF0000"));
        } else {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on, 0, 0, 0);
            address.setTextColor(Color.parseColor("#FFffffff"));
        }



        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(getActivity(), delivered_activity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });


        return view;
    }


    //change the value if the address
    public static void sync() {
        String name = prefs.getString("add", "GPS Error");
        address.setText(name);

        if (name == "" || name == "לא ניתן למצוא את מיקומך הנוכחי" || name == "נא הפעל GPS" || name == "Permission Denied...") {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_off, 0, 0, 0);
            address.setTextColor(Color.parseColor("#FF0000"));
        } else {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on, 0, 0, 0);
            address.setTextColor(Color.parseColor("#FFCEFDCF"));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.package_type:
                if (parent.getItemAtPosition(position).toString() == "סוג החבילה")
                    MainActivity.valid_packagetype = false;
                else {
                    //String text = parent.getItemAtPosition(position).toString();
                    MainActivity.valid_packagetype = true;
                }
            case R.id.breakable:
                if (parent.getItemAtPosition(position)=="תוכן שביר")
                    MainActivity.valid_breakable = false;
                else
                    MainActivity.valid_breakable = true;
        }
        valid();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (address.getText() == "" || address.getText() == "לא ניתן למצוא את מיקומך הנוכחי" || address.getText() == "נא הפעל GPS" || address.getText() == "Permission Denied...")
            MainActivity.valid_address = false;
        else
            MainActivity.valid_address = true;

        if (weight.getText().toString() != "")
            MainActivity.valid_weight = true;
        else
            MainActivity.valid_weight = false;

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (address.getText() == "" || address.getText() == "לא ניתן למצוא את מיקומך הנוכחי" || address.getText() == "נא הפעל GPS" || address.getText() == "Permission Denied...")
            MainActivity.valid_address = false;
        else
            MainActivity.valid_address = true;

        if (weight.getText().toString() != "")
            MainActivity.valid_weight = true;
        else
            MainActivity.valid_weight = false;
    }

    public static void valid()
    {
        if (mainActivity.valid()) {
            add_package.setEnabled(true);
            add_package.setImageResource(R.drawable.checked);
            error_message.setVisibility(View.GONE);
        }
        else
        {
            add_package.setEnabled(false);
            error_message.setVisibility(View.VISIBLE);
            add_package.setImageResource(R.drawable.error);
        }
    }

}
