package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class LoadHousesListOperation extends ChronosOperation<List<House>> {
    @Nullable
    @Override
    public List<House> run() {
        return IceAndFireApplication.getDaoSession().queryBuilder(House.class)
                .where(HouseDao.Properties.Id.gt(0))
//                .orderDesc(HouseDao.Properties.Rating)
                .build()
                .list();
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<House>> {

    }
}
