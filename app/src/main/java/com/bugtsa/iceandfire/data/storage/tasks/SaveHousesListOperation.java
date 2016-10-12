package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import retrofit2.Response;


public class SaveHousesListOperation extends ChronosOperation<String> {
    private Response<HouseRes> mResponse;

    public SaveHousesListOperation(Response<HouseRes> response) {
        mResponse = response;
    }

    @Nullable
    @Override
    public String run() {
        DataManager dataManager = DataManager.getInstance();
        HouseDao houseDao = dataManager.getDaoSession().getHouseDao();
        House house = new House(mResponse.body());
        houseDao.insertOrReplaceInTx(house);

        return null;
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }


    public static final class Result extends ChronosOperationResult<String> {

    }
}
