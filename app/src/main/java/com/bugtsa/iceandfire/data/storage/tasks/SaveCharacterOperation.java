package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.Title;
import com.bugtsa.iceandfire.data.storage.models.TitleDao;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SaveCharacterOperation extends ChronosOperation<String> {

    private Response<List<CharacterRes>> mResponse;

    public SaveCharacterOperation(Response<List<CharacterRes>> response) {
        mResponse = response;
    }

    @Nullable
    @Override
    public String run() {
        DataManager dataManager = DataManager.getInstance();
        CharacterOfHouseDao characterOfHouseDao = dataManager.getDaoSession().getCharacterOfHouseDao();
        TitleDao titleDao = dataManager.getDaoSession().getTitleDao();

        List<CharacterOfHouse> characterList = new ArrayList<>();
        List<Title> titleList = new ArrayList<>();

        try {
            for (int pos = 0; pos < mResponse.body().size(); pos++) {
                CharacterRes characterRes = mResponse.body().get(pos);
                CharacterOfHouse characterOfHouse = new CharacterOfHouse(characterRes);
                characterList.add(characterOfHouse);

                if (characterRes.getTitles() != null) {
                    if (!characterRes.getTitles().isEmpty()) {
                        for (int index = 0; index < characterRes.getTitles().size(); index++) {
                            Title title = new Title(characterOfHouse.getRemoteId(), characterRes.getTitles().get(index));
                            titleList.add(title);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.d("Exception Save " + e.toString());
            e.toString();
        }
        characterOfHouseDao.insertOrReplaceInTx(characterList);
        titleDao.insertOrReplaceInTx(titleList);

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
