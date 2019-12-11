package com.example.onboarding1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.onboarding1.Data.Action;
import com.example.onboarding1.Data.Firebase_DBManager;
import com.example.onboarding1.Data.Parcel;

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
    EditText weight;
    ProgressBar progressBar;
    static TextView address;
    static TextView address_icon;
    static SharedPreferences prefs;
    static ImageButton add_package;
    static AddPackage mainActivity;
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
        progressBar=view.findViewById(R.id.progress_circular);

        add_package.setEnabled(false);
        error_message.setVisibility(View.VISIBLE);
        add_package.setImageResource(R.drawable.error);
        progressBar.setVisibility(View.GONE);




        //sign up the fields to Text/selection Changed
        breakable.setOnItemSelectedListener(this);
        type_package.setOnItemSelectedListener(this);
        address.addTextChangedListener(this);
        weight.addTextChangedListener(this);

        int r=6;
        mainActivity = (AddPackage) getActivity();

        //put the address in the fragment
        prefs = getContext().getSharedPreferences("maps", MODE_PRIVATE);
        final String name = prefs.getString("add", "GPS Error");
        address.setText(name);
        address = view.findViewById(R.id.address);

        //change the color of the address
        if (name.equals( "") || name.equals( "לא ניתן למצוא את מיקומך הנוכחי") || name.equals( "נא הפעל GPS") || name.equals("Permission Denied...")) {
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
                addParcel();
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

        if (name.equals( "") || name.equals( "לא ניתן למצוא את מיקומך הנוכחי") || name.equals("נא הפעל GPS") || name.equals( "Permission Denied...")) {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_off, 0, 0, 0);
            address.setTextColor(Color.parseColor("#FF0000"));
        } else {
            address_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on, 0, 0, 0);
            address.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.package_type:
                if (parent.getItemAtPosition(position).toString().equals( "סוג החבילה"))
                    AddPackage.valid_packagetype = false;
                else {
                    //String text = parent.getItemAtPosition(position).toString();
                    AddPackage.valid_packagetype = true;
                }
                break;
            case R.id.breakable:
                if (parent.getItemAtPosition(position).toString().equals("תוכן שביר"))
                    AddPackage.valid_breakable = false;
                else
                    AddPackage.valid_breakable = true;
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
        if (address.getText().toString().equals( "") || address.getText().toString().equals( "לא ניתן למצוא את מיקומך הנוכחי") || address.getText().toString().equals( "נא הפעל GPS") || address.getText().toString().equals( "Permission Denied..."))
            AddPackage.valid_address = false;
        else
            AddPackage.valid_address = true;

        if (!weight.getText().toString().equals( ""))
            AddPackage.valid_weight = true;
        else
            AddPackage.valid_weight = false;
        valid();
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (address.getText().toString().equals( "") || address.getText().toString().equals( "לא ניתן למצוא את מיקומך הנוכחי" )|| address.getText().toString().equals( "נא הפעל GPS") || address.getText().toString().equals( "Permission Denied..."))
            AddPackage.valid_address = false;
        else
            AddPackage.valid_address = true;

        if (!weight.getText().toString().equals( ""))
            AddPackage.valid_weight = true;
        else
            AddPackage.valid_weight = false;
        valid();
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
    private Parcel getParcel()
    {
        Parcel p=new Parcel();
        p.setDistributionCenterAddress(address.getText().toString());
        if(breakable.getSelectedItem().toString().equals("תוכן החבילה שביר"))
            p.setFragile(true);
        else
            p.setFragile(false);
        p.setRecipientAddress("ישעיהו הנביא");
        p.setParcelId("0");
        p.setRecipientName(FirstFragment.name.toString());
        p.setStatus(Parcel.Status.Registered);
        p.setRecipientPhoneNumber(FirstFragment.phone.toString());
        if(type_package.getSelectedItem().toString().equals("חבילה גדולה"))
            p.setType(Parcel.Type.LargePackage);
        if(type_package.getSelectedItem().toString().equals("חבילה קטנה"))
            p.setType(Parcel.Type.SmallPackage);
        if(type_package.getSelectedItem().toString().equals("מעטפה"))
            p.setType(Parcel.Type.Envelope);
        //double a = Double.valueOf(weight.getText().toString());
        p.setWeight(Double.valueOf(weight.getText().toString()));
        return p;
    }



    private void addParcel() {
        /*
        Parcel parcel = new Parcel();
        parcel.setRecipientPhoneNumber("0588745888");
        parcel.setRecipientName("moshe levi");
        */
        try {

            //Parcel parcel = getStudent();

            Firebase_DBManager.addParcel(getParcel(), new Action<String>()
        {
                @Override
                public void onSuccess(String obj) {
                    Intent intent =new Intent(getActivity(), delivered_activity.class);
                    progressBar.setVisibility(View.GONE);
                    add_package.setVisibility(View.VISIBLE);

                    startActivity(intent);
                    getActivity().finish();
                }

            @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    add_package.setVisibility(View.VISIBLE);
                }

                @Override
                public void onProgress(String status, double percent) {
                    if (percent != 100) {
                        add_package.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
