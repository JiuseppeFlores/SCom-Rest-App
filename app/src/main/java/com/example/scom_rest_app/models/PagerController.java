package com.example.scom_rest_app.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scom_rest_app.Fragments.FragmentBebidas;
import com.example.scom_rest_app.Fragments.FragmentPlatillos;

import java.util.ArrayList;

public class PagerController extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    int numOfTabs;

    public PagerController(@NonNull FragmentManager fm, int behavior){
        super(fm,behavior);
        this.numOfTabs = behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
