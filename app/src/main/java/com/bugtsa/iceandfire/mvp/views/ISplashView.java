package com.bugtsa.iceandfire.mvp.views;

import com.bugtsa.iceandfire.mvp.presenters.ISplashPresenter;
import com.redmadrobot.chronos.ChronosConnector;

public interface ISplashView {

    ISplashPresenter getPresenter();

    ChronosConnector getChronosConnector();

    void showMessage(String message);

    void showErrors(Throwable e);

    void showSplash();

    void hideSplash();

    void setOrientation(int ActivityInfo);
}
