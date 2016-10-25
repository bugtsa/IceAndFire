package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.mvp.models.CharacterModel;
import com.bugtsa.iceandfire.mvp.views.ICharacterView;
import com.bugtsa.iceandfire.utils.StringUtils;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_CHAR;
import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_STR;

public class CharacterPresenter implements ICharacterPresenter {

    private static CharacterPresenter ourInstance = new CharacterPresenter();

    private ICharacterView mCharacterView;

    private CharacterModel mCharacterModel;

    private CharacterDTO mFatherOfCharacterDTO;

    private CharacterDTO mMotherOfCharacterDTO;

    private CharacterDTO mCharacterDTO;

    private CharacterPresenter() {
        mCharacterModel = new CharacterModel();
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
            getView().openParentOfCharacter(mFatherOfCharacterDTO);
        }
    }

    @Override
    public void onMotherButtonClick() {
        if (getView() != null) {
            getView().openParentOfCharacter(mMotherOfCharacterDTO);
        }
    }

    private void setCallbacks() {
        mCharacterModel.setTitleHouseCallback(titleHouse -> setTitleHouse(titleHouse));
        mCharacterModel.setCharacterByRemoteIdCallback(characterDTO -> setParentOfCharacter(characterDTO));
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

            mCharacterModel.getWords(mCharacterDTO.getHouseRemoteId());

            if (!TextUtils.isEmpty(mCharacterDTO.getBorn())) {
                getView().setDateBorn(mCharacterDTO.getBorn());
            }

            if (!TextUtils.isEmpty(mCharacterDTO.getDied())) {
                getView().showSeasonDiedCharacter(mCharacterDTO.getName(), getLastSeason(mCharacterDTO.getSeasons()), mCharacterDTO.getDied());
            }

            if (mCharacterDTO.getTitles() != null) {
                getView().setTitlesCharacter(getStringList(mCharacterDTO.getTitles()));
            }

            if (mCharacterDTO.getAliases() != null) {
                getView().setAliasesCharacter(getStringList(mCharacterDTO.getAliases()));
            }

            if (!mCharacterDTO.getMother().isEmpty()) {
                mCharacterModel.getParent(mCharacterDTO.getMother());
            } else {
                getView().setVisibleMother(View.INVISIBLE);
            }

            if (!mCharacterDTO.getFather().isEmpty()) {
                mCharacterModel.getParent(mCharacterDTO.getFather());
            } else {
                getView().setVisibleFather(View.INVISIBLE);
            }
        }
    }

    private String getLastSeason(List<String> seasonList) {
        String dateDied = "";
        if (seasonList != null && seasonList.size() > 0) {
            dateDied = seasonList.get(seasonList.size() - 1);
        }
        return dateDied;
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

    public void setParentOfCharacter(CharacterDTO character) {
        if (character.getRemoteId().equals(mCharacterDTO.getMother())) {
            mMotherOfCharacterDTO = character;
            mMotherOfCharacterDTO.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setMotherName(character.getName());
            getView().setVisibleMother(View.VISIBLE);
        } else if (character.getRemoteId().equals(mCharacterDTO.getFather())) {
            character.getSeasons();
            mFatherOfCharacterDTO = character;
            mFatherOfCharacterDTO.setHouseRemoteId(mCharacterDTO.getHouseRemoteId());
            getView().setFatherName(character.getName());
            getView().setVisibleFather(View.VISIBLE);
        }
    }
}
