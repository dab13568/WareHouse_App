package com.example.onboarding1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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
        View view= inflater.inflate(R.layout.fragment_second, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);

        type_package = view.findViewById(R.id.package_type);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getContext(), R.array.package_type,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_package.setAdapter(arrayAdapter);
        type_package.setOnItemSelectedListener(this);



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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
