package com.bugtsa.iceandfire.mvp.models;

import com.bugtsa.iceandfire.data.callbacks.CharacterListByHouseIdCallback;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.DaoSession;

import java.util.List;

public class HouseModel {

    private static DataManager sDataManager;
    private DaoSession syncDaoSession;

    private CharacterListByHouseIdCallback mCharacterListByHouseIdCallback;

    public HouseModel () {
        sDataManager = DataManager.getInstance();
        syncDaoSession = sDataManager.getDaoSession();
    }

    public void setCharacterListByHouseIdCallback(CharacterListByHouseIdCallback characterListByHouseIdCallback) {
        mCharacterListByHouseIdCallback = characterListByHouseIdCallback;
    }

    public void loadCharactersByHouseIdFromDb(int houseKey) {
        try {
            List<CharacterOfHouse> list = syncDaoSession.queryBuilder(CharacterOfHouse.class)
                    .where(CharacterOfHouseDao.Properties.HouseRemoteId.eq(houseKey))
                    .build()
                    .list();
            if (!list.isEmpty()) {
                mCharacterListByHouseIdCallback.getCharactersList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
