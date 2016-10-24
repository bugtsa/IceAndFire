package com.bugtsa.iceandfire.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private Context mContext;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
//        mFragmentTitleList.add(mContext.getString(R.string.stark_title));
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(mContext.getString(R.string.stark_title));
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(mContext.getString(R.string.stark_title));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
