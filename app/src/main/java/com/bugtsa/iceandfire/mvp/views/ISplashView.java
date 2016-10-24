package com.bugtsa.iceandfire.mvp.views;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.presenters.ISplashPresenter;

import java.util.List;

public interface ISplashView {

    ISplashPresenter getPresenter();

    void showMessage(String message);

    void showMessage(int messageKey);

    void showErrors(Throwable e);

    void showSplash();

    void hideSplash();

    void setOrientation(int ActivityInfo);

    void selectPage(int pageIndex);

    void showCharacters(List<CharacterOfHouse> characterOfHouseList);
}
