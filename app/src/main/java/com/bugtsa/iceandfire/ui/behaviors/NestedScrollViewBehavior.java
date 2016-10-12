package com.bugtsa.iceandfire.ui.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.utils.UiHelper;

public class NestedScrollViewBehavior<V extends LinearLayout> extends AppBarLayout.ScrollingViewBehavior {
    private final int mMaxAppbarHeight;
    private final int mMinAppbarHeight;
    private final int mMaxRatingBarHeight;

    public NestedScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinAppbarHeight = UiHelper.getStatusBarHeight() + UiHelper.getActionBarHeight();
        mMaxAppbarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_height_256);
        mMaxRatingBarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_info_height_112);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float friction = UiHelper.currentFriction(mMinAppbarHeight, mMaxAppbarHeight, dependency.getBottom());
        int transY = UiHelper.lerp(mMaxRatingBarHeight / 2, mMaxRatingBarHeight, friction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.topMargin = transY;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }
}
