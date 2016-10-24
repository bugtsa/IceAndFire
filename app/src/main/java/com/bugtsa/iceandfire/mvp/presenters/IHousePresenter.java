package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.views.IHouseView;

public interface IHousePresenter extends IIceAndFirePresenter {

    void takeView(IHouseView splashView);

    @Nullable
    IHouseView getView();

    void setCallBack();

    void loadCharactersOfHouseFromDb(int houseKey);
}
