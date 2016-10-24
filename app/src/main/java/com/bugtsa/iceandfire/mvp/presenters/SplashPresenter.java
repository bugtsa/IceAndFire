package com.bugtsa.iceandfire.mvp.presenters;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.events.ShowMessageEvent;
import com.bugtsa.iceandfire.mvp.models.DataModel;
import com.bugtsa.iceandfire.mvp.views.ISplashView;
import com.bugtsa.iceandfire.utils.AppConfig;
import com.bugtsa.iceandfire.utils.ConstantManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashPresenter implements ISplashPresenter {

    private static SplashPresenter ourInstance = new SplashPresenter();

    private ISplashView mSplashView;

    private DataModel mDataModel;

    private Long mStart;

    private SplashPresenter() {
        mDataModel = new DataModel();
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
    public void initView() {
        if (getView() != null) {
            getView().showSplash();
            setCallBacks();
            loadCharacterFromDb();
        }
    }

    @Nullable
    @Override
    public ISplashView getView() {
        return mSplashView;
    }

    private void setCallBacks() {
        mDataModel.setOnLoadAllCharacterListListener(time -> loadAllCharactersDone(time));
    }

    private void loadCharacterFromDb() {
        mStart = System.currentTimeMillis();
        mDataModel.loadCharacterFromDb();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(ShowMessageEvent showMessageEvent) {
        if (getView() != null) {
            getView().showMessage(showMessageEvent.getMessageKey());
        }
    }

    private void loadAllCharactersDone(long time) {
        long durationApp = time - mStart;
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
            getView().selectPage(ConstantManager.STARK_PAGE_ID);
        }
    }
}
