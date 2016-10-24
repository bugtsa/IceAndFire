package com.bugtsa.iceandfire.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.models.SplashModel;
import com.bugtsa.iceandfire.mvp.views.IHouseView;

public class HousePresenter implements IHousePresenter {

    private static HousePresenter ourPresenter = new HousePresenter();

    private IHouseView mHouseView;

    private SplashModel mSplashModel;

    private HousePresenter() {
        mSplashModel = new SplashModel();
    }

    public static HousePresenter getInstance() {
        return ourPresenter;
    }

    @Override
    public void takeView(IHouseView houseView) {
        mHouseView = houseView;
    }

    @Override
    public void dropView() {
        mHouseView = null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSplashModel.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public IHouseView getView() {
        return mHouseView;
    }

    @Override
    public void onResume() {
        mSplashModel.onResume();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mSplashModel.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSplashModel.onSavedInstanceState(outState);
    }

    public void loadCharactersOfHouseFromDb(int houseKey) {
        mSplashModel.loadCharactersOfHouseFromDb(houseKey);
    }

}
