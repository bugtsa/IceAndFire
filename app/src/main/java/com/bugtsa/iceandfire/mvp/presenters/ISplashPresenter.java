package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.views.ISplashView;

public interface ISplashPresenter extends IIceAndFirePresenter{

    void takeView(ISplashView splashView);

    @Nullable
    ISplashView getView();
}
