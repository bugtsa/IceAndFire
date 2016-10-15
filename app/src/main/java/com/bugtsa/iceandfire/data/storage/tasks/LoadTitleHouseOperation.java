package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

public class LoadTitleHouseOperation extends ChronosOperation<String> {

    private String mHouseRemoteId;

    public LoadTitleHouseOperation(String houseRemoteId) {
        mHouseRemoteId = houseRemoteId;
    }

    @Nullable
    @Override
    public String run() {
        House house = IceAndFireApplication.getDaoSession().queryBuilder(House.class)
                .where(HouseDao.Properties.RemoteId.eq(mHouseRemoteId))
                .build()
                .unique();
        return house.getWords();
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<String> {
    }
}
