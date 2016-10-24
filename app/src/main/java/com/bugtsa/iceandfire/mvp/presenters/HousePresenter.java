package com.bugtsa.iceandfire.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.models.SplashModel;
import com.bugtsa.iceandfire.mvp.views.IHouseView;

import java.util.List;

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
        setCallBack();
    }

    @Nullable
    @Override
    public IHouseView getView() {
        return mHouseView;
    }

    public void setCallBack() {
        mSplashModel.setLoadCharacterListByHouseId(listCharacter -> showCharacterList(listCharacter));
    }

    public void loadCharactersOfHouseFromDb(int houseKey) {
        mSplashModel.loadCharactersByHouseIdFromDb(houseKey);
    }

    private void showCharacterList(List<CharacterOfHouse> characterList) {
        if (getView() != null) {
            getView().showCharacters(characterList);
        }
    }
}
