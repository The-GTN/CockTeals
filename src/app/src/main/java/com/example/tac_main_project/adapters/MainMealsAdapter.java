package com.example.tac_main_project.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tac_main_project.models.view.MainFragment;

public class MainMealsAdapter extends FragmentPagerAdapter {

    private int[] colors;

    public MainMealsAdapter(FragmentManager mgr, int[] colors) {
        super(mgr);
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return(2);
    }

    @Override
    public Fragment getItem(int position) {
        return(MainFragment.newInstance(position, this.colors[position]));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return "Meals";
        }
        else{
            return "Favorites";
        }
    }
}