package com.bugtsa.iceandfire.mvp.models;

import android.text.TextUtils;

import com.bugtsa.iceandfire.data.callbacks.CharacterByRemoteIdCallback;
import com.bugtsa.iceandfire.data.callbacks.TitleHouseCallback;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.DaoSession;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;

public class CharacterModel {

    private static DataManager sDataManager;
    private DaoSession syncDaoSession;

    private TitleHouseCallback mTitleHouseCallback;
    private CharacterByRemoteIdCallback mCharacterByRemoteIdCallback;

    public CharacterModel() {
        sDataManager = DataManager.getInstance();
        syncDaoSession = sDataManager.getDaoSession();
    }

    public void setTitleHouseCallback(TitleHouseCallback titleHouseCallback) {
        mTitleHouseCallback = titleHouseCallback;
    }

    public void setCharacterByRemoteIdCallback(CharacterByRemoteIdCallback characterByRemoteIdCallback) {
        mCharacterByRemoteIdCallback = characterByRemoteIdCallback;
    }

    public void getWords(String houseRemoteId) {
        if(!TextUtils.isEmpty(houseRemoteId)) {
            String titleHouse = syncDaoSession.queryBuilder(House.class)
                    .where(HouseDao.Properties.RemoteId.eq(houseRemoteId))
                    .build()
                    .unique()
                    .getWords();
            mTitleHouseCallback.getTitleHouse(titleHouse);
        }
    }

    public void getParent(String parentRemoteId) {
        CharacterOfHouse character = syncDaoSession.queryBuilder(CharacterOfHouse.class)
                .where(CharacterOfHouseDao.Properties.RemoteId.eq(parentRemoteId))
                .build()
                .unique();
        mCharacterByRemoteIdCallback.getCharacterByRemoteId(character);
    }
}
