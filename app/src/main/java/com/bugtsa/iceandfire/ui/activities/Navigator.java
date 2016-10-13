package com.bugtsa.iceandfire.ui.activities;

import android.app.Activity;
import android.view.View;

import com.bugtsa.iceandfire.R;

public class Navigator {

    private static String sTabTag = "";

    public static void changeViewPagerVisibility(Activity activity, boolean visibility) {
        View tabLayout = activity.findViewById(R.id.tabs_house_list);
        View fragmentContainer = activity.findViewById(R.id.fragment_container);
        View viewPager = activity.findViewById(R.id.viewpager_house_list);
        if (visibility) {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
        } else {
            if (viewPager.getVisibility() == View.VISIBLE) {
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    public static String getTabTag() {
        return sTabTag;
    }

    public static void setTabTag(String tabTag) {
        Navigator.sTabTag = tabTag;
    }
}
