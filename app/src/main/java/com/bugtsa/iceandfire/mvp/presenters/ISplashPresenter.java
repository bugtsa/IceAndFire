package com.bugtsa.iceandfire.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.views.ISplashView;

public interface ISplashPresenter {

    void takeView(ISplashView splashView);

    void dropView();

    void initView(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onSaveInstanceState(Bundle outState);

    @Nullable
    ISplashView getView();
}
