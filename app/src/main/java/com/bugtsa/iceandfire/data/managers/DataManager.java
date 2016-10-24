package com.bugtsa.iceandfire.data.managers;

import android.content.Context;

import com.bugtsa.iceandfire.IceAndFireApplication;
import com.bugtsa.iceandfire.data.network.PicassoCache;
import com.bugtsa.iceandfire.data.network.RestService;
import com.bugtsa.iceandfire.data.network.ServiceGenerator;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.DaoSession;
import com.bugtsa.iceandfire.utils.NetworkStatusChecker;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;


public class DataManager {

    private static DataManager INSTANCE = null;

    private RestService mRestService;

    private Context mContext;

    private PreferencesManager mPreferencesManager;

    private Picasso mPicasso;

    private static DaoSession mDaoSession;

    public DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = IceAndFireApplication.getContext();
        mRestService = ServiceGenerator.createService(RestService.class);
        mPicasso = new PicassoCache(mContext).getPicassoInstance();
        mDaoSession = IceAndFireApplication.getDaoSession();
    }

    /**
     * Создаёт экземпляр класса
     *
     * @return singleton класса
     */
    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    /**
     * Получает SharedPreferences
     *
     * @return SharedPreferences
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region ============Network=================

    public boolean isNetworkAvailable() {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            return true;
        } else {
            return false;
        }
    }

    public Call<HouseRes> getHouseFromNetwork(String house) {
        return mRestService.getHouse(house);
    }

    public Call<CharacterRes> getCharacterFromNetwork(String characterId) {
        return mRestService.getCharacter(characterId);
    }

    public Call<List<CharacterRes>> getCharacterPageFromNetwork(String currentPage, String perPage) {
        return mRestService.getCharacterPage(currentPage, perPage);
    }

    //end region

    //region ============Network=================

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    //end region
}
