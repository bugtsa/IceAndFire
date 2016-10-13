package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

public class LoadCharacterListOperation extends ChronosOperation<List<CharacterOfHouse>> {

    private int mHouseId;

    public LoadCharacterListOperation() {
    }

    @Nullable
    @Override
    public List<CharacterOfHouse> run() {
        return IceAndFireApplication.getDaoSession().queryBuilder(CharacterOfHouse.class)
                .where(CharacterOfHouseDao.Properties.Id.gt(0))
                .build()
                .list();
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<CharacterOfHouse>> {

    }
}
