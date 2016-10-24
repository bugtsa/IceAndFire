package com.bugtsa.iceandfire.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.views.IHouseView;

public interface IHousePresenter {

    void takeView(IHouseView splashView);

    void dropView();

    void initView(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onSaveInstanceState(Bundle outState);

    @Nullable
    IHouseView getView();
}
