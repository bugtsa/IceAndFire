package com.bugtsa.iceandfire.data.storage.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.storage.models.Alias;
import com.bugtsa.iceandfire.data.storage.models.AliasDao;
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

import static com.bugtsa.iceandfire.utils.ConstantManager.DONE_SAVE_CHARACTER_KEY;

public class SaveCharacterOperation extends ChronosOperation<String> {

    private Response<List<CharacterRes>> mResponse;

    public SaveCharacterOperation(Response<List<CharacterRes>> response) {
        mResponse = response;
    }

    @Nullable
    @Override
    public String run() {
        String doneString = DONE_SAVE_CHARACTER_KEY;
        CharacterOfHouseDao characterOfHouseDao = IceAndFireApplication.getDaoSession().getCharacterOfHouseDao();
        TitleDao titleDao = IceAndFireApplication.getDaoSession().getTitleDao();
        AliasDao aliasDao = IceAndFireApplication.getDaoSession().getAliasDao();
        List<CharacterOfHouse> characterList = new ArrayList<>();

        try {
            for (int pos = 0; pos < mResponse.body().size(); pos++) {
                List<Title> titleList;
                List<Alias> aliasList;
                CharacterRes characterRes = mResponse.body().get(pos);
                CharacterOfHouse characterOfHouse = new CharacterOfHouse(characterRes);
                characterList.add(characterOfHouse);
                titleList = getTitleList(characterOfHouse.getRemoteId(), characterRes);
                titleDao.insertOrReplaceInTx(titleList);
                aliasList = getAliasList(characterOfHouse.getRemoteId(), characterRes);
                aliasDao.insertOrReplaceInTx(aliasList);
                characterOfHouse.setAliasTitle(getAliasTitle(characterRes));
            }
        } catch (Exception e) {
            LogUtils.d("Exception SaveCharacterOperation " + e.toString());
        }
        characterOfHouseDao.insertOrReplaceInTx(characterList);

        return doneString;
    }

    private String getAliasTitle(CharacterRes characterRes) {
        String aliasTitle = "";
        if (characterRes.getAliases() != null) {
            if (!characterRes.getAliases().isEmpty()) {
                aliasTitle = characterRes.getAliases().get(0);
            }
        } else if (characterRes.getTitles() != null) {
            if (!characterRes.getTitles().isEmpty()) {
                aliasTitle = characterRes.getTitles().get(0);
            }
        }
        return aliasTitle;
    }

    private List<Title> getTitleList(String characterRemoteId, CharacterRes characterRes) {
        List<Title> titleList = new ArrayList<>();
        for (int index = 0; index < characterRes.getTitles().size(); index++) {
            Title title = new Title(characterRemoteId, characterRes.getTitles().get(index));
            titleList.add(title);
        }
        return titleList;
    }

    private List<Alias> getAliasList(String characterRemoteId, CharacterRes characterRes) {
        List<Alias> aliasList = new ArrayList<>();
        for (int index = 0; index < characterRes.getAliases().size(); index++) {
            Alias alias = new Alias(characterRemoteId, characterRes.getAliases().get(index));
            aliasList.add(alias);
        }
        return aliasList;
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<String> {
    }
}
