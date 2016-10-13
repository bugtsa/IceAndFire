package com.bugtsa.iceandfire.ui.routers;

import android.app.Activity;

public class Router <T extends Activity> {

    private T mActivity;

    public Router(T activity) {
        mActivity = activity;
    }

    protected T getActivity() {
        return mActivity;
    }
}
