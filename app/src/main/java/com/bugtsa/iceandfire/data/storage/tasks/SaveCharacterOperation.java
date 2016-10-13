package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SaveCharacterOperation extends ChronosOperation<List<CharacterOfHouse>>{

    private Response<List<CharacterRes>> mResponse;

    private String mRemoteCharacterId;

    private String mRemoteHouseId;

    public SaveCharacterOperation(Response<List<CharacterRes>> response) {
        mResponse = response;
    }

    @Nullable
    @Override
    public List<CharacterOfHouse> run() {
        DataManager dataManager = DataManager.getInstance();
        CharacterOfHouseDao characterOfHouseDao = dataManager.getDaoSession().getCharacterOfHouseDao();

        List<CharacterOfHouse> characterList = new ArrayList<>();

        for (int pos = 0; pos < mResponse.body().size(); pos++) {
            characterList.add(new CharacterOfHouse(mResponse.body().get(pos)));
        }
        characterOfHouseDao.insertOrReplaceInTx(characterList);

        return characterList;
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }


    public static final class Result extends ChronosOperationResult<List<CharacterOfHouse>> {

    }
}
