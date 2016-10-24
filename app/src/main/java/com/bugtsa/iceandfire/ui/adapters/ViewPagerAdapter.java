package com.bugtsa.iceandfire.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bugtsa.iceandfire.ui.fragments.HouseFragment;
import com.bugtsa.iceandfire.utils.ConstantManager;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        HouseFragment fragment = new HouseFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantManager.KEY_HOUSE_INDEX, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(ConstantManager.HOUSE_NAME_RES[position]);
    }
}
