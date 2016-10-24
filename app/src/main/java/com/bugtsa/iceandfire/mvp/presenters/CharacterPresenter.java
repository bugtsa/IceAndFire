package com.bugtsa.iceandfire.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.models.SplashModel;
import com.bugtsa.iceandfire.mvp.views.ICharacterView;
import com.bugtsa.iceandfire.utils.StringUtils;
import com.redmadrobot.chronos.ChronosConnector;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_CHAR;
import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_STR;

public class CharacterPresenter implements ICharacterPresenter {

    private static CharacterPresenter ourInstance = new CharacterPresenter();

    private ICharacterView mCharacterView;

    private SplashModel mSplashModel;

    private CharacterOfHouse mFatherOfCharacter;

    private CharacterOfHouse mMotherOfCharacter;

    private CharacterDTO mCharacterDTO;

    private CharacterDTO mOpenCharacter;

    private String mDiedCharacterMessage;

    private ChronosConnector mConnector;

    private CharacterPresenter() {
        mSplashModel = new SplashModel();
    }

    public static CharacterPresenter getInstance() {
        return ourInstance;
    }

    @Override
    public void takeView(ICharacterView splashView) {
        mCharacterView = splashView;
    }

    @Override
    public void dropView() {
        mCharacterView = null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getView() != null) {
            setCallbacks();
        }
    }

    @Nullable
    @Override
    public ICharacterView getView() {
        return mCharacterView;
    }

    private void setCallbacks() {
        mSplashModel.setLoadTitleHouse(titleHouse -> setTitleHouse(titleHouse));
        mSplashModel.setLoadCharacterByRemoteId(character -> setCharacterFatherAndMother(character));
    }

    /**
     * Инициализирует данные персонажа
     */
    public void initCharacterData(CharacterDTO characterDTO) {
        mCharacterDTO = characterDTO;

        if (getView() != null) {
            getView().setImageHouse(mCharacterDTO.getHouseRemoteId());

            if (!TextUtils.isEmpty(mCharacterDTO.getName())) {
                getView().setNameCharacter(mCharacterDTO.getName());
            }

            mSplashModel.getWords(mCharacterDTO.getHouseRemoteId());

            if (!TextUtils.isEmpty(mCharacterDTO.getBorn())) {
                getView().setDateBorn(mCharacterDTO.getBorn());
            }

            if (!TextUtils.isEmpty(mCharacterDTO.getDied())) {
                getView().showSeasonDiedCharacter(mCharacterDTO.getName(), mCharacterDTO.getDied());
            }

            if (mCharacterDTO.getTitles() != null) {
                getView().setTitlesCharacter(getStringList(mCharacterDTO.getTitles()));
            }

            if (mCharacterDTO.getAliases() != null) {
                getView().setAliasesCharacter(getStringList(mCharacterDTO.getAliases()));
            }

            if (!mCharacterDTO.getMother().isEmpty()) {
                mSplashModel.getParentName(mCharacterDTO.getMother());
            } else {
                getView().setVisibleMother(View.INVISIBLE);
            }

            if (!mCharacterDTO.getFather().isEmpty()) {
                mSplashModel.getParentName(mCharacterDTO.getFather());
            } else {
                getView().setVisibleFather(View.INVISIBLE);
            }
        }
    }

    private String getStringList(List<String> stringList) {
        String listForOutput = "";
        if (!stringList.isEmpty()) {
            for (String title : stringList) {
                listForOutput += title;
                listForOutput += NEW_STRING_SYMBOL_STR;
            }
            listForOutput = StringUtils.removeLastChar(listForOutput, NEW_STRING_SYMBOL_CHAR);
        }
        return listForOutput;
    }

    public void setTitleHouse(String titleHouse) {
        String resultString = "";
        if (!titleHouse.isEmpty()) {
            resultString = titleHouse;
        }
        getView().setWoodsCharacter(resultString);
    }

    public void setCharacterFatherAndMother(CharacterOfHouse character) {
        CharacterOfHouse parentOfCharacter = character;
        if (parentOfCharacter.getRemoteId().equals(mCharacterDTO.getMother())) {
            mMotherOfCharacter = parentOfCharacter;
            mMotherOfCharacter.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setMotherName(parentOfCharacter.getName());
            getView().setVisibleMother(View.VISIBLE);
        } else if (parentOfCharacter.getRemoteId().equals(mCharacterDTO.getFather())) {
            mFatherOfCharacter = parentOfCharacter;
            mFatherOfCharacter.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setFatherName(parentOfCharacter.getName());
            getView().setVisibleFather(View.VISIBLE);
        }
    }
}
