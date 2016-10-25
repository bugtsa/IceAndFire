package com.bugtsa.iceandfire.mvp.presenters;

import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.mvp.models.HouseModel;
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

    private HouseModel mHouseModel;

    private HousePresenter() {
        mHouseModel = new HouseModel();
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

    /**
     * устанавливает callback with HouseModel
     */
    @Override
    public void setCallBack() {
        mHouseModel.setCharacterListByHouseIdCallback(listCharacter -> showCharacterList(listCharacter));
    }

    /**
     * преобразует индекс view Pager`a в удалённый ключ дома
     * @param housePageId индекс текущей страницы view Pager`a
     */
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
        mHouseModel.loadCharactersByHouseIdFromDb(houseKey);
    }

    /**
     * посылает инициативу по отображению списка героев дома
     * @param characterList список персонажей дома
     */
    public void showCharacterList(List<CharacterOfHouse> characterList) {
        if (getView() != null) {
            getView().showCharacters(characterList);
        }
    }
}
