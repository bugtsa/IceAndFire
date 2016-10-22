package com.bugtsa.iceandfire.mvp.presenters;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.LoadDoneEvent;
import com.bugtsa.iceandfire.data.events.ShowMessageEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.mvp.models.SplashModel;
import com.bugtsa.iceandfire.mvp.views.ISplashView;
import com.bugtsa.iceandfire.utils.AppConfig;
import com.redmadrobot.chronos.ChronosConnector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashPresenter implements ISplashPresenter {

    private static SplashPresenter ourInstance = new SplashPresenter(DataManager.getInstance());

    private ISplashView mSplashView;

    private SplashModel mSplashModel;

    private static DataManager sDataManager;

    private Context mContext;

    private ChronosConnector mConnector;

    private SplashPresenter(DataManager dataManager) {
        sDataManager = dataManager;
        mContext = sDataManager.getContext();
        mSplashModel = new SplashModel(sDataManager);
    }

    public static SplashPresenter getInstance() {
        return ourInstance;
    }

    @Override
    public void takeView(ISplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void dropView() {
        mSplashView = null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getView() != null) {
            mConnector = new ChronosConnector();
            mConnector.onCreate(this, savedInstanceState);

            getView().showSplash();
            loadCharacterFromDb();
        }
    }

    @Override
    public void onResume() {
        mConnector.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mConnector.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mConnector.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public ISplashView getView() {
        return mSplashView;
    }

    private void loadCharacterFromDb() {
        mSplashModel.loadCharacterFromDb();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(ShowMessageEvent showMessageEvent) {
        if (getView() != null) {
            getView().showMessage(showMessageEvent.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoneEvent(LoadDoneEvent loadDoneEvent) {
        Long timeOfEvent = loadDoneEvent.getTimeOfEvent();
        Long durationApp = timeOfEvent - mStart;
        if (durationApp >= AppConfig.SHOW_SPLASH_DELAY) {
            loadIsDone();
        } else {
            Long timeForSleep = AppConfig.SHOW_SPLASH_DELAY - durationApp;
            try {
                Thread.sleep(timeForSleep);
                loadIsDone();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadIsDone() {
        if (getView() != null) {
            getView().setOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
            getView().hideSplash();
        }
    }
}
