package com.neo.androidkeyboardinputs.settings;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that stores fragments for tabs
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    // list to hold tab fragments
    private final List<Fragment> mFragmentList = new ArrayList<>();


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){                 // method to add fragment to ViewPager adapter fragment list
        mFragmentList.add(fragment);
    }

}
