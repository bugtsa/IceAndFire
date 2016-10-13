package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.LoadDoneEvent;
import com.bugtsa.iceandfire.data.events.TimeEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.tasks.LoadCharacterListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveCharacterOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveHousesListOperation;
import com.bugtsa.iceandfire.databinding.ActivityHouseListBinding;
import com.bugtsa.iceandfire.ui.adapters.CharactersAdapter;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseListActivity extends BaseActivity {
    public static final String ACTION_BAR_TITLE = "action_bar_title";
    private static final String TAG = ConstantManager.TAG_PREFIX + HouseListActivity.class.getSimpleName();
    private static String VIEWPAGER_VISIBLE = "viewpager_visible";
    private static Fragment targarienFragment;
    private static Fragment lannisterFragment;
    private static Fragment starkFragment;

    private ActivityHouseListBinding mBinding;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;
    private DataManager mDataManager;
    private Context mContext;
    private CharactersAdapter mCharactersAdapter;

    private MenuItem mSearchItem;
    private ChronosConnector mConnector;
    private Handler mHandler;
    private ViewPagerAdapter adapter;


    private Long mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_house_list);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        mHandler = new Handler();

        mStart = System.currentTimeMillis();

        if (savedInstanceState != null) {
            Navigator.changeViewPagerVisibility(this, savedInstanceState.getBoolean(VIEWPAGER_VISIBLE));
            getSupportActionBar().setTitle(savedInstanceState.getString(ACTION_BAR_TITLE));
        }
        initViewPager();
        setupToolbar();
        setupDrawer();
        showSplash();
        loadCharacterFromDb();
//        selectPage(ConstantManager.STARK_MENU_ID);
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
//        FragmentUtils.addFragment(this, R.id.fragment_container, starkFragment, getString(R.string.stark_title), true);
    }

    private void initViewPager() {
        setupViewPager(mBinding.viewpagerHouseList);
        mBinding.viewpagerHouseList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment item = adapter.getItem(position);
                item.onResume();
                String tag = item.getTag();
                Navigator.setTabTag(tag);
//                EventBus.getDefault().post(new CloseActionModeEvent());
                LogUtils.d("onPageSelected" + tag);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.tabsHouseList.setupWithViewPager(mBinding.viewpagerHouseList);
    }

//    private void setupRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        mBinding.houseListContent.recyclerViewHouseList.setLayoutManager(linearLayoutManager);
//    }

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
        if (mBinding.viewpagerHouseList != null) {
            outState.putBoolean(VIEWPAGER_VISIBLE, mBinding.viewpagerHouseList.getVisibility() == View.VISIBLE);
        }
        outState.putString(ACTION_BAR_TITLE, getSupportActionBar().getTitle().toString());
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

        drawerUserFullName.setText(mDataManager.getPreferencesManager().getUserFullName());
        drawerUserEmail.setText(mDataManager.getPreferencesManager().getUserEmail());

        insertDrawerAvatar(mDataManager.getPreferencesManager().loadUserAvatar());

        mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
        mBinding.navigationViewHouseList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setChecked(true);
                mBinding.navigationDrawerHouseList.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.stark_menu:
                        selectPage(ConstantManager.STARK_MENU_ID);
//                        mFragmentContainer = starkFragment;
                        break;
                    case R.id.targarien_menu:
                        selectPage(ConstantManager.TARGARIEN_MENU_ID);
//                        mFragmentContainer = targarienFragment;
                        break;
                    case R.id.lannister_menu:
                        selectPage(ConstantManager.LANNISTER_MENU_ID);
//                        mFragmentContainer = lannisterFragment;
                        break;
                }
//                FragmentUtils.replaceFragment((Activity)mContext, R.id.fragment_container, mFragmentContainer, true);
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Устанавливает обработку поиска по имени пользователя в списке
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.search_action);
        android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint(getString(R.string.search_input_name_user));
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showUserByQuery(newText);
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Отображает список пользователей при поиске по имени
     *
     * @param query строка поиска
     */
    private void showUserByQuery(final String query) {
        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                if (!query.isEmpty()) {
//                    loadUsersByNameFromDb(query);
                } else {
//                    loadHousesListFromDb();
                }
            }
        };

        mHandler.removeCallbacks(searchUsers);
        if (!query.isEmpty()) {
            mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
        } else {
            mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_WITHOUT_DELAY);
        }
    }

    /**
     * Слушает событие TimeEvent
     *
     * @param timeEvent слушает окончание отображение списка пользователей
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeEvent(TimeEvent timeEvent) {
        switch (timeEvent.getTimeCode()) {

            case ConstantManager.END_SHOW_USERS:
                hideSplash();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDoneEvent(LoadDoneEvent loadDoneEvent) {
        Long timeOfEvent = loadDoneEvent.getTimeOfEvent();
        Long end = System.currentTimeMillis();
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
        hideSplash();
        EventBus.getDefault().post(new TimeEvent(ConstantManager.HIDE_SPLASH));
    }

    private void loadCharacterFromDb() {
        try {
            mConnector.runOperation(new LoadCharacterListOperation(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onOperationFinished(final LoadCharacterListOperation.Result result) {
        if (result.getOutput().isEmpty()) {
            int countPage = 43;
            int perPage = 50;
            for (int currentPage = 1; currentPage <= countPage; currentPage++) {
                loadCharacterFromNetwork(currentPage, perPage);
            }
        } else {
            loadHousesListFromDb();
        }
    }

    private void loadCharacterFromNetwork(final int currentPage, final int perPage) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<List<CharacterRes>> call = mDataManager.getCharacterPageFromNetwork(String.valueOf(currentPage), String.valueOf(perPage));
            call.enqueue(new Callback<List<CharacterRes>>() {
                @Override
                public void onResponse(Call<List<CharacterRes>> call, Response<List<CharacterRes>> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        mConnector.runOperation(new SaveCharacterOperation(response), false);
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

    private void loadHousesFromNetwork(int houseKey) {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<HouseRes> call = mDataManager.getHouseFromNetwork(String.valueOf(houseKey));
            call.enqueue(new Callback<HouseRes>() {
                @Override
                public void onResponse(Call<HouseRes> call, Response<HouseRes> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        mConnector.runOperation(new SaveHousesListOperation(response), false);
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
        } else {
            if (result.getOutput().size() == 3) {
                EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
            }
//            loadCharacterOfHouse();
        }
    }

    public void onOperationFinished(final SaveHousesListOperation.Result result) {
        loadHousesListFromDb();
    }
}
