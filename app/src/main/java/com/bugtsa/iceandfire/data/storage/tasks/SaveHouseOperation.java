package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.bugtsa.iceandfire.utils.StringUtils;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import retrofit2.Response;


public class SaveHouseOperation extends ChronosOperation<String> {
    private Response<HouseRes> mResponse;

    public SaveHouseOperation(Response<HouseRes> response) {
        mResponse = response;
    }

    @Nullable
    @Override
    public String run() {
        DataManager dataManager = DataManager.getInstance();
        HouseDao houseDao = dataManager.getDaoSession().getHouseDao();
        HouseRes houseRes = mResponse.body();
        houseRes.setUrl(StringUtils.getIdFromUrlApi(houseRes.getUrl()));
        houseDao.insertOrReplaceInTx(new House(houseRes));

//        CharacterOfHouseDao characterOfHouseDao = dataManager.getDaoSession().getCharacterOfHouseDao();
//        List<CharacterOfHouse> characterList = new ArrayList<>();
//        for (String memberUrl : houseRes.getSwornMembers()) {
////            characterList.add(new CharacterOfHouse(StringUtils.getIdFromUrlApi(memberUrl), houseRes.getUrl()));
//        }
//        characterOfHouseDao.insertOrReplaceInTx(characterList);

        return "complete";
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }


    public static final class Result extends ChronosOperationResult<String> {

    }
}
