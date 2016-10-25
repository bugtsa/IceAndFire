package com.bugtsa.iceandfire.mvp.models;

import android.text.TextUtils;

import com.bugtsa.iceandfire.data.callbacks.LoadCharacterByRemoteId;
import com.bugtsa.iceandfire.data.callbacks.LoadCharacterList;
import com.bugtsa.iceandfire.data.callbacks.LoadCharacterListByHouseId;
import com.bugtsa.iceandfire.data.callbacks.LoadTitleHouse;
import com.bugtsa.iceandfire.data.events.ShowMessageEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.managers.PreferencesManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.Alias;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.DaoSession;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;
import com.bugtsa.iceandfire.data.storage.models.Title;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bugtsa.iceandfire.utils.ConstantManager.PER_PAGE;
import static com.bugtsa.iceandfire.utils.ConstantManager.QUANTITY_PAGE;

public class DataModel {

    private static DataManager sDataManager;

    private PreferencesManager mPreferencesManager;

    private LoadCharacterList mLoadCharactersListListener;
    private LoadCharacterListByHouseId mLoadCharacterListByHouseId;
    private LoadTitleHouse mLoadTitleHouse;
    private LoadCharacterByRemoteId mLoadCharacterByRemoteId;

    private DaoSession syncDaoSession;
    private AsyncSession mAsyncDbSession;
    private final AsyncOperationListener mDbListener = operation -> {
        if (operation.isCompletedSucessfully()) {
            mLoadCharactersListListener.LoadingIsDone(System.currentTimeMillis());
        }
    };
    private AsyncSession mAsyncHouseSession;

    private List<Integer> listResponsePageCharacter = new ArrayList<>();
    private List<CharacterOfHouse> mCharacterList = new ArrayList<>();
    private List<Title> mTitleList = new ArrayList<>();
    private List<Alias> mAliasList = new ArrayList<>();

    public DataModel() {
        sDataManager = DataManager.getInstance();
        mPreferencesManager = sDataManager.getPreferencesManager();

        syncDaoSession = sDataManager.getDaoSession();

        mAsyncDbSession = sDataManager.getDaoSession().startAsyncSession();
        mAsyncHouseSession = sDataManager.getDaoSession().startAsyncSession();
        mAsyncDbSession.setListenerMainThread(mDbListener);
    }

    public void setOnLoadAllCharacterListListener(LoadCharacterList loadCharactersListListener) {
        mLoadCharactersListListener = loadCharactersListListener;
    }

    public void setLoadCharacterListByHouseId(LoadCharacterListByHouseId loadCharacterListByHouseId) {
        mLoadCharacterListByHouseId = loadCharacterListByHouseId;
    }

    public void setLoadTitleHouse(LoadTitleHouse loadTitleHouse) {
        mLoadTitleHouse = loadTitleHouse;
    }

    public void setLoadCharacterByRemoteId(LoadCharacterByRemoteId loadCharacterByRemoteId) {
        mLoadCharacterByRemoteId = loadCharacterByRemoteId;
    }

    public void loadCharacterFromDb() {
        try {
            List<CharacterOfHouse> list = syncDaoSession.getCharacterOfHouseDao().queryBuilder()
                    .where(CharacterOfHouseDao.Properties.Id.gt(0))
                    .build()
                    .list();
            if (list.isEmpty()) {
                loadAllCharactersFromNetwork();
                loadHousesListFromDb();
            } else {
                mLoadCharactersListListener.LoadingIsDone(System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCharactersByHouseIdFromDb(int houseKey) {
        try {
            List<CharacterOfHouse> list = syncDaoSession.queryBuilder(CharacterOfHouse.class)
                    .where(CharacterOfHouseDao.Properties.HouseRemoteId.eq(houseKey))
                    .build()
                    .list();
            if (!list.isEmpty()) {
                mLoadCharacterListByHouseId.getCharactersList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllCharactersFromNetwork() {
        for (int currentPage = 1; currentPage <= QUANTITY_PAGE; currentPage++) {
            loadCharactersFromNetwork(currentPage, PER_PAGE);
        }
    }

    private void loadCharactersFromNetwork(final int currentPage, final int perPage) {
        if (sDataManager.isNetworkAvailable()) {
            Call<List<CharacterRes>> call = sDataManager.getCharacterPageFromNetwork(String.valueOf(currentPage), String.valueOf(perPage));
            call.enqueue(new Callback<List<CharacterRes>>() {
                @Override
                public void onResponse(Call<List<CharacterRes>> call, Response<List<CharacterRes>> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        saveCharactersToLists(response);
                        if (checkLoadingAllCharacters()) {
                            saveAllCharactersInDb();
                        }
                    } else {
                        LogUtils.d("response not ok");
                    }
                }

                @Override
                public void onFailure(Call<List<CharacterRes>> call, Throwable t) {
                    LogUtils.d("failed server");
                }
            });
        } else {
            EventBus.getDefault().post(new ShowMessageEvent(ConstantManager.NETWORK_IS_NOT_AVAILABLE));
        }
    }

    private void saveCharactersToLists(Response<List<CharacterRes>> response) {
        try {
            for (int pos = 0; pos < response.body().size(); pos++) {
                CharacterRes characterRes = response.body().get(pos);
                CharacterOfHouse characterOfHouse = new CharacterOfHouse(characterRes);
                mTitleList.addAll(getTitleList(characterOfHouse.getRemoteId(), characterRes));
                mAliasList.addAll(getAliasList(characterOfHouse.getRemoteId(), characterRes));
                characterOfHouse.setAliasTitle(getAliasTitle(characterRes));
                mCharacterList.add(characterOfHouse);
            }
        } catch (Exception e) {
            LogUtils.d("Exception saveCharactersToLists " + e.toString());
        }
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

    private boolean checkLoadingAllCharacters() {
        listResponsePageCharacter.add(1);
        if (listResponsePageCharacter.size() == QUANTITY_PAGE) {
            return true;
        } else {
            return false;
        }
    }

    private void saveAllCharactersInDb() {
        mAsyncDbSession.insertOrReplaceInTx(CharacterOfHouse.class, mCharacterList);
        mAsyncDbSession.insertOrReplaceInTx(Title.class, mTitleList);
        mAsyncDbSession.insertOrReplaceInTx(Alias.class, mAliasList);
    }

    /**
     * Загружает данные о домах из БД
     */
    private void loadHousesListFromDb() {
        try {
            List<House> listHouse = syncDaoSession.queryBuilder(House.class)
                    .where(HouseDao.Properties.Id.gt(0))
                    .build()
                    .list();
            if (listHouse.isEmpty()) {
                loadHousesFromNetwork(ConstantManager.STARK_KEY);
                loadHousesFromNetwork(ConstantManager.TARGARIEN_KEY);
                loadHousesFromNetwork(ConstantManager.LANNISTER_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHousesFromNetwork(int houseKey) {
        if (sDataManager.isNetworkAvailable()) {
            Call<HouseRes> call = sDataManager.getHouseFromNetwork(String.valueOf(houseKey));
            call.enqueue(new Callback<HouseRes>() {
                @Override
                public void onResponse(Call<HouseRes> call, Response<HouseRes> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        saveHouseInDb(response);
                        LogUtils.d("response ok");
                    } else {
                        LogUtils.d("response not ok");
                    }
                }

                @Override
                public void onFailure(Call<HouseRes> call, Throwable t) {
                    LogUtils.d("failed server");
                }
            });
        } else {
            EventBus.getDefault().post((new ShowMessageEvent(ConstantManager.NETWORK_IS_NOT_AVAILABLE)));
        }
    }

    private void saveHouseInDb(Response<HouseRes> response) {
        HouseRes houseRes = response.body();
        houseRes.setUrl(StringUtils.getIdFromUrlApi(houseRes.getUrl()));
        mAsyncHouseSession.insertOrReplaceInTx(House.class, new House(houseRes));
    }

    public void getWords(String houseRemoteId) {
        if(!TextUtils.isEmpty(houseRemoteId)) {
            String titleHouse = syncDaoSession.queryBuilder(House.class)
                    .where(HouseDao.Properties.RemoteId.eq(houseRemoteId))
                    .build()
                    .unique()
                    .getWords();
            mLoadTitleHouse.getTitleHouse(titleHouse);
        }
    }

    public void getParent(String parentRemoteId) {
        CharacterOfHouse character = syncDaoSession.queryBuilder(CharacterOfHouse.class)
                .where(CharacterOfHouseDao.Properties.RemoteId.eq(parentRemoteId))
                .build()
                .unique();
        mLoadCharacterByRemoteId.getCharacterByRemoteId(character);
    }
}
