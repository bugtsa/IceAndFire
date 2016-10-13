package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.bugtsa.iceandfire.utils.StringUtils;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.ArrayList;
import java.util.List;

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
        CharacterOfHouseDao characterOfHouseDao = dataManager.getDaoSession().getCharacterOfHouseDao();
        HouseRes houseRes = mResponse.body();
        houseRes.setUrl(StringUtils.getIdFromUrlApi(houseRes.getUrl()));

        List<CharacterOfHouse> characterList = new ArrayList<>();
        for (String memberUrl : houseRes.getSwornMembers()) {
            characterList.add(new CharacterOfHouse(StringUtils.getIdFromUrlApi(memberUrl), houseRes.getUrl()));
        }

        houseDao.insertOrReplaceInTx(new House(houseRes));
        characterOfHouseDao.insertOrReplaceInTx(characterList);

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
