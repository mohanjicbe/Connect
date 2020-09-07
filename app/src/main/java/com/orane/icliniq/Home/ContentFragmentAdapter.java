package com.orane.icliniq.Home;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.orane.icliniq.fragment.ArticlesFragment;
import com.orane.icliniq.fragment.DoctorsFragment;
import com.orane.icliniq.fragment.HomeFragment;


class ContentFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    private final Context c;

    public ContentFragmentAdapter(FragmentManager fragmentManager, Context context, int item_count) {
        super(fragmentManager);
        NUM_ITEMS = item_count;
        c = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance(position);
            case 1:
                return DoctorsFragment.newInstance(position);
            case 2:
                return ArticlesFragment.newInstance(position);
        }
        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Doctors";
            case 2:
                return "Articles";

        }


        return null;
    }

}
