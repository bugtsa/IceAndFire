package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.models.DataModel;
import com.bugtsa.iceandfire.mvp.views.IHouseView;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_PAGE_ID;

public class HousePresenter implements IHousePresenter {

    private static HousePresenter ourPresenter = new HousePresenter();

    private IHouseView mHouseView;

    private DataModel mDataModel;

    private HousePresenter() {
        mDataModel = new DataModel();
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
    public void initView() {
        setCallBack();
    }

    @Nullable
    @Override
    public IHouseView getView() {
        return mHouseView;
    }

    @Override
    public void setCallBack() {
        mDataModel.setLoadCharacterListByHouseId(listCharacter -> showCharacterList(listCharacter));
    }

    @Override
    public void loadCharactersOfHouseFromDb(int housePageId) {
        int houseKey;
        switch (housePageId) {
            case STARK_PAGE_ID:
                houseKey = STARK_KEY;
                break;
            case LANNISTER_PAGE_ID:
                houseKey = LANNISTER_KEY;
                break;
            case TARGARIEN_PAGE_ID:
                houseKey = TARGARIEN_KEY;
                break;
            default:
                houseKey = STARK_KEY;
        }
        mDataModel.loadCharactersByHouseIdFromDb(houseKey);
    }

    public void showCharacterList(List<CharacterOfHouse> characterList) {
        if (getView() != null) {
            getView().showCharacters(characterList);
        }
    }
}
