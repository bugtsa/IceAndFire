package com.bugtsa.iceandfire.mvp.presenters;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.LoadDoneEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.Alias;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.Title;
import com.bugtsa.iceandfire.data.storage.tasks.LoadCharacterListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveHouseOperation;
import com.bugtsa.iceandfire.mvp.views.ISplashView;
import com.bugtsa.iceandfire.utils.AppConfig;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.NetworkStatusChecker;
import com.redmadrobot.chronos.ChronosConnector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bugtsa.iceandfire.utils.ConstantManager.PER_PAGE;
import static com.bugtsa.iceandfire.utils.ConstantManager.QUANTITY_PAGE;

public class SplashPresenter implements ISplashPresenter {

    private static SplashPresenter ourInstance = new SplashPresenter(DataManager.getInstance());

    private ISplashView mSplashView;

    private static DataManager sDataManager;
    private Context mContext;

    private ChronosConnector mConnector;
    private Long mStart;

    private AsyncSession mAsyncDbSession;
    private final AsyncOperationListener mDbListener = operation -> {
        if (operation.isCompletedSucessfully()) {
            EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
        }
    };

    private List<Integer> listResponsePageCharacter = new ArrayList<>();
    private List<CharacterOfHouse> mCharacterList = new ArrayList<>();
    private List<Title> mTitleList = new ArrayList<>();
    private List<Alias> mAliasList = new ArrayList<>();

    private SplashPresenter(DataManager dataManager) {
        sDataManager = dataManager;
        mContext = sDataManager.getContext();
//        mAuthModel = new AuthModel(sDataManager);
//        if (getView() != null) {
//            getView().getAuthPanel().setAuthPresenter(ourInstance);
//        }
    }

    public static SplashPresenter getInstance() {
        return ourInstance;
    }

    @Override
    public void takeView(ISplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void dropView() {
        mSplashView = null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getView() != null) {
            mConnector = new ChronosConnector();
            mConnector.onCreate(this, savedInstanceState);

            getView().showSplash();
            loadCharacterFromDb();
        }
    }

    @Override
    public void onResume() {
        mConnector.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        mConnector.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mConnector.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public ISplashView getView() {
        return mSplashView;
    }

    public void loadCharacterFromDb() {
        mStart = System.currentTimeMillis();
        try {
            mConnector.runOperation(new LoadCharacterListOperation(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onOperationFinished(final LoadCharacterListOperation.Result result) {
        if (result.getOutput().isEmpty()) {
            mAsyncDbSession = sDataManager.getDaoSession().startAsyncSession();
            mAsyncDbSession.setListenerMainThread(mDbListener);
            loadAllCharactersFromNetwork();
            loadHousesListFromDb();
        } else {
            EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
        }
    }

    private void loadAllCharactersFromNetwork() {
        for (int currentPage = 1; currentPage <= QUANTITY_PAGE; currentPage++) {
            loadCharactersFromNetwork(currentPage, PER_PAGE);
        }
    }

    private void loadCharactersFromNetwork(final int currentPage, final int perPage) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
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
            if (getView() != null) {
                getView().showMessage(mContext.getString(R.string.hint_not_connection_inet));
            }
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
            mConnector.runOperation(new LoadHousesListOperation(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает результат операции LoadHousesListOperation
     *
     * @param result результат операции
     */
    public void onOperationFinished(final LoadHousesListOperation.Result result) {
        if (result.getOutput().isEmpty()) {
            loadHousesFromNetwork(ConstantManager.STARK_KEY);
            loadHousesFromNetwork(ConstantManager.TARGARIEN_KEY);
            loadHousesFromNetwork(ConstantManager.LANNISTER_KEY);
        }
    }


    private void loadHousesFromNetwork(int houseKey) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<HouseRes> call = sDataManager.getHouseFromNetwork(String.valueOf(houseKey));
            call.enqueue(new Callback<HouseRes>() {
                @Override
                public void onResponse(Call<HouseRes> call, Response<HouseRes> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        mConnector.runOperation(new SaveHouseOperation(response), false);
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
            getView().showMessage(mContext.getString(R.string.hint_not_connection_inet));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoneEvent(LoadDoneEvent loadDoneEvent) {
        Long timeOfEvent = loadDoneEvent.getTimeOfEvent();
        Long durationApp = timeOfEvent - mStart;
        if (durationApp >= AppConfig.SHOW_SPLASH_DELAY) {
            loadIsDone();
        } else {
            Long timeForSleep = AppConfig.SHOW_SPLASH_DELAY - durationApp;
            try {
                Thread.sleep(timeForSleep);
                loadIsDone();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadIsDone() {
        if (getView() != null) {
            getView().setOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
            getView().hideSplash();
        }
    }
}
