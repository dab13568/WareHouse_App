package com.example.onboarding1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment implements AdapterView.OnItemSelectedListener
{

    ViewPager viewPager;
    TextView next;
    TextView back;
    Spinner type_package;

    public SecondFragment() {
        // Required empty public constructor
    }

//ghjhjh

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_second, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);

        type_package = view.findViewById(R.id.package_type);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getContext(), R.array.package_type,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_package.setAdapter(arrayAdapter);
        type_package.setOnItemSelectedListener(this);

        final MainActivity mainActivity=(MainActivity)getActivity();


        final TextView textView=view.findViewById(R.id.address);
        SharedPreferences prefs = getContext().getSharedPreferences("maps", MODE_PRIVATE);
        final String name = prefs.getString("add", "GPS Error");

        textView.setText(name);


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Snackbar.make(mainActivity.findViewById(android.R.id.content),name, Snackbar.LENGTH_LONG).show();
//                           }
//        }, 2000);



        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final TextView textView=view.findViewById(R.id.address);
                            SharedPreferences prefs = getContext().getSharedPreferences("maps", MODE_PRIVATE);
                            String name = prefs.getString("add", "GPS Error");
                            textView.setText(name);                     }
                    }, 0,2000);
                }

            }
        });

//        mainActivity.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                // Stuff that updates the UI
//
//            }
//        });





//        next=view.findViewById(R.id.textViewNext);
//        next.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                viewPager.setCurrentItem(2);
//            }
//            });
//        back=view.findViewById(R.id.textViewBack);
//        back.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                viewPager.setCurrentItem(0);
//            }
//        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).toString().equals("סוג החבילה"));
        else
            {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getContext().sendBroadcast(poke);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
