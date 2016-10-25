package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.mvp.views.ICharacterView;

public interface ICharacterPresenter extends IIceAndFirePresenter {

    void takeView(ICharacterView splashView);

    @Nullable
    ICharacterView getView();

    void onFatherButtonClick();

    void onMotherButtonClick();
}
