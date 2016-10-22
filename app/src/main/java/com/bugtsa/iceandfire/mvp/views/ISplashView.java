package com.bugtsa.iceandfire.mvp.views;

import com.bugtsa.iceandfire.mvp.presenters.ISplashPresenter;

public interface ISplashView {

    ISplashPresenter getPresenter();

    void showMessage(String message);

    void showErrors(Throwable e);

    void showSplash();

    void hideSplash();

    void setOrientation(int ActivityInfo);
}
