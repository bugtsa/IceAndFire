package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.models.DataModel;
import com.bugtsa.iceandfire.mvp.views.ICharacterView;
import com.bugtsa.iceandfire.utils.StringUtils;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_CHAR;
import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_STR;

public class CharacterPresenter implements ICharacterPresenter {

    private static CharacterPresenter ourInstance = new CharacterPresenter();

    private ICharacterView mCharacterView;

    private DataModel mDataModel;

    private CharacterOfHouse mFatherOfCharacter;

    private CharacterOfHouse mMotherOfCharacter;

    private CharacterDTO mCharacterDTO;

    private String mDiedCharacterMessage;

    private CharacterPresenter() {
        mDataModel = new DataModel();
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
    public void initView() {
        setCallbacks();
    }

    @Nullable
    @Override
    public ICharacterView getView() {
        return mCharacterView;
    }

    @Override
    public void onFatherButtonClick() {
        if (getView() != null) {
            getView().openParentOfCharacter(new CharacterDTO(mFatherOfCharacter));
        }
    }

    @Override
    public void onMotherButtonClick() {
        if (getView() != null) {
            getView().openParentOfCharacter(new CharacterDTO(mMotherOfCharacter));
        }
    }

    private void setCallbacks() {
        mDataModel.setLoadTitleHouse(titleHouse -> setTitleHouse(titleHouse));
        mDataModel.setLoadCharacterByRemoteId(character -> setParentOfCharacter(character));
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

            mDataModel.getWords(mCharacterDTO.getHouseRemoteId());

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
                mDataModel.getParent(mCharacterDTO.getMother());
            } else {
                getView().setVisibleMother(View.INVISIBLE);
            }

            if (!mCharacterDTO.getFather().isEmpty()) {
                mDataModel.getParent(mCharacterDTO.getFather());
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

    public void setParentOfCharacter(CharacterOfHouse character) {
        if (character.getRemoteId().equals(mCharacterDTO.getMother())) {
            mMotherOfCharacter = character;
            mMotherOfCharacter.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setMotherName(character.getName());
            getView().setVisibleMother(View.VISIBLE);
        } else if (character.getRemoteId().equals(mCharacterDTO.getFather())) {
            mFatherOfCharacter = character;
            mFatherOfCharacter.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setFatherName(character.getName());
            getView().setVisibleFather(View.VISIBLE);
        }
    }
}
