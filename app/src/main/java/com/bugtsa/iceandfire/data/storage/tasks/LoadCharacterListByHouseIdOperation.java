package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class LoadCharacterListByHouseIdOperation extends ChronosOperation<List<CharacterOfHouse>> {

    private int mHouseId;

    public LoadCharacterListByHouseIdOperation(int houseId) {
        mHouseId = houseId;
    }

    @Nullable
    @Override
    public List<CharacterOfHouse> run() {
        return IceAndFireApplication.getDaoSession().queryBuilder(CharacterOfHouse.class)
                .where(CharacterOfHouseDao.Properties.HouseRemoteId.eq(mHouseId))
                .build()
                .list();
    }

    @NonNull
    @Override
    public Class<LoadCharacterListOperation.Result> getResultClass() {
        return LoadCharacterListOperation.Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<CharacterOfHouse>> {

    }
}
