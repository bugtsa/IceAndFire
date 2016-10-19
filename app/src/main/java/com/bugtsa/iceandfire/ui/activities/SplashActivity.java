package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.LoadDoneEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.managers.PreferencesManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.Alias;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.Title;
import com.bugtsa.iceandfire.data.storage.tasks.LoadCharacterListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveHouseOperation;
import com.bugtsa.iceandfire.databinding.ActivitySplashBinding;
import com.bugtsa.iceandfire.ui.adapters.ViewPagerAdapter;
import com.bugtsa.iceandfire.ui.fragments.HouseFragment;
import com.bugtsa.iceandfire.utils.AppConfig;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.NetworkStatusChecker;
import com.bugtsa.iceandfire.utils.SnackBarUtils;
import com.redmadrobot.chronos.ChronosConnector;
import com.squareup.picasso.Picasso;

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

public class SplashActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + SplashActivity.class.getSimpleName();

    private static Fragment targarienFragment;
    private static Fragment lannisterFragment;
    private static Fragment starkFragment;

    private ActivitySplashBinding mBinding;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;
    private DataManager mDataManager;
    private PreferencesManager mPreferencesManager;
    private Context mContext;

    private ChronosConnector mConnector;
    private ViewPagerAdapter adapter;

    private Long mStart;

    private List<Integer> listResponsePageCharacter = new ArrayList<>();
    private List<CharacterOfHouse> mCharacterList = new ArrayList<>();
    private List<Title> mTitleList = new ArrayList<>();
    private List<Alias> mAliasList = new ArrayList<>();
    private AsyncSession mAsyncDbSession;
    private final AsyncOperationListener mDbListener = operation -> {
        if (operation.isCompletedSucessfully()) {
            EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();
        if (mPreferencesManager.isFirstLaunch()) {
            mPreferencesManager.saveFirstLaunch(false);
        }
        mContext = mDataManager.getContext();

        initViewPager();
        setupToolbar();
        setupDrawer();
        showSplash();
        loadCharacterFromDb();
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(supportFragmentManager);
        if (starkFragment == null) {
            starkFragment = HouseFragment.newInstance(ConstantManager.STARK_KEY);
            targarienFragment = HouseFragment.newInstance(ConstantManager.TARGARIEN_KEY);
            lannisterFragment = HouseFragment.newInstance(ConstantManager.LANNISTER_KEY);
        }
        adapter.addFragment(starkFragment, getString(R.string.stark_title));
        adapter.addFragment(targarienFragment, getString(R.string.targarien_title));
        adapter.addFragment(lannisterFragment, getString(R.string.lannister_title));
        viewPager.setAdapter(adapter);
        if (mPreferencesManager.isFirstLaunch()) {
            selectPage(ConstantManager.STARK_MENU_ID);
        }
    }

    private void initViewPager() {
        setupViewPager(mBinding.viewpagerHouseList);

        mBinding.viewpagerHouseList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment item = adapter.getItem(position);
                item.onResume();
                setCheckedItemNavigationView(position);
                String tag = item.getTag();
                Navigator.setTabTag(tag);
                LogUtils.d("onPageSelected" + tag);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.tabsHouseList.setupWithViewPager(mBinding.viewpagerHouseList);
    }

    private void setCheckedItemNavigationView(int position) {
        switch (position) {
            case ConstantManager.STARK_MENU_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
                break;
            case ConstantManager.TARGARIEN_MENU_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.targarien_menu);
                break;
            case ConstantManager.LANNISTER_MENU_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.lannister_menu);
                break;
            default:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
        }
    }

    /**
     * Обрабатывает событие onResume жизненного цикла Activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    /**
     * Обрабатывает событие onPause жизненного цикла Activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
    }

    /**
     * Обрабатывает событие onStart жизненного цикла Activity
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Обрабатывает событие onStop жизненного цикла Activity
     */
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mBinding.navigationDrawerHouseList.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Устанавливает аватар пользователя со скруглёнными краями в Navigation Drawer
     *
     * @param selectedImage
     */
    private void insertDrawerAvatar(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.avatar_bg)
                .into(drawerUserAvatar);
    }

    /**
     * инициализирует NavigationDrawer
     */
    private void setupDrawer() {
        View headerLayout = mBinding.navigationViewHouseList.getHeaderView(0);

        drawerUserAvatar = (ImageView) mBinding.navigationViewHouseList.getHeaderView(0).findViewById(R.id.drawer_user_avatar_iv);

        drawerUserFullName = (TextView) headerLayout.findViewById(R.id.drawer_user_name_tv);
        drawerUserEmail = (TextView) headerLayout.findViewById(R.id.drawer_user_email_tv);

//        drawerUserFullName.setText(mDataManager.getPreferencesManager().getUserFullName());
//        drawerUserEmail.setText(mDataManager.getPreferencesManager().getUserEmail());
//
//        insertDrawerAvatar(mDataManager.getPreferencesManager().loadUserAvatar());

        mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
        mBinding.navigationViewHouseList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setChecked(true);
                mBinding.navigationDrawerHouseList.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.stark_menu:
                        selectPage(ConstantManager.STARK_MENU_ID);
                        break;
                    case R.id.targarien_menu:
                        selectPage(ConstantManager.TARGARIEN_MENU_ID);
                        break;
                    case R.id.lannister_menu:
                        selectPage(ConstantManager.LANNISTER_MENU_ID);
                        break;
                }
                return false;
            }
        });
    }

    void selectPage(int pageIndex) {
        mBinding.tabsHouseList.setScrollPosition(pageIndex, 0f, true);
        mBinding.viewpagerHouseList.setCurrentItem(pageIndex);
    }

    /**
     * инициализирует ToolBar
     */
    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbarHouseList);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.my_name);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        hideSplash();
    }

    private void loadCharacterFromDb() {
        mStart = System.currentTimeMillis();
        try {
            mConnector.runOperation(new LoadCharacterListOperation(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllCharactersFromNetwork() {
        for (int currentPage = 1; currentPage <= QUANTITY_PAGE; currentPage++) {
            loadCharactersFromNetwork(currentPage, PER_PAGE);
        }
    }

    public void onOperationFinished(final LoadCharacterListOperation.Result result) {
        if (result.getOutput().isEmpty()) {
            mAsyncDbSession = mDataManager.getDaoSession().startAsyncSession();
            mAsyncDbSession.setListenerMainThread(mDbListener);
            loadAllCharactersFromNetwork();
            loadHousesListFromDb();
        } else {
            EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
        }
    }

    private void loadCharactersFromNetwork(final int currentPage, final int perPage) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<List<CharacterRes>> call = mDataManager.getCharacterPageFromNetwork(String.valueOf(currentPage), String.valueOf(perPage));
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
                    LogUtils.d("failes server");
                }
            });
        } else {
            SnackBarUtils.show(mBinding.coordinatorLayoutHouseList, getString(R.string.hint_not_connection_inet));
        }
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


    private void loadHousesFromNetwork(int houseKey) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<HouseRes> call = mDataManager.getHouseFromNetwork(String.valueOf(houseKey));
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
            SnackBarUtils.show(mBinding.coordinatorLayoutHouseList, getString(R.string.hint_not_connection_inet));
        }
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
}
