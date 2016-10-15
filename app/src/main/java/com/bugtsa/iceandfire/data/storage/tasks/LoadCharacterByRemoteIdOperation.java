package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

public class LoadCharacterByRemoteIdOperation extends ChronosOperation<CharacterOfHouse> {

    private String mCharacterRemoteId;

    public LoadCharacterByRemoteIdOperation(String characterRemoteId) {
        mCharacterRemoteId = characterRemoteId;
    }

    @Nullable
    @Override
    public CharacterOfHouse run() {
        return IceAndFireApplication.getDaoSession().queryBuilder(CharacterOfHouse.class)
                .where(CharacterOfHouseDao.Properties.RemoteId.eq(mCharacterRemoteId))
                .build()
                .unique();
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<CharacterOfHouse> {

    }
}
