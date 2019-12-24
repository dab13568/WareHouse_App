package com.example.onboarding1.Data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.onboarding1.UI.FirstFragment;
import com.example.onboarding1.UI.SecondFragment;

public class sliderAdapter extends FragmentPagerAdapter {
    public sliderAdapter(FragmentManager fm) {
        super(fm);

    }

    Fragment fragment1;
    Fragment fragment2;

    @Override

    public Fragment getItem(int position)
    {
        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();
        switch(position){
            case 0: return fragment1;
            case 1: return fragment2;
            default: return null;
        }
    }


    @Override
    public int getCount() {
        return 2;
    }
}
