package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.TimeEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveHousesListOperation;
import com.bugtsa.iceandfire.ui.activities.interfaces.CustomClickListener;
import com.bugtsa.iceandfire.ui.adapters.CharactersAdapter;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.NetworkStatusChecker;
import com.bugtsa.iceandfire.utils.SnackBarUtils;
import com.redmadrobot.chronos.ChronosConnector;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseListActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + HouseListActivity.class.getSimpleName();
    @BindView(R.id.coordinator_layout_users_list)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.navigation_drawer_users_list)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view_users_list)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar_users_list)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_user_list)
    RecyclerView mRecyclerView;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;
    private DataManager mDataManager;

    private Context mContext;
    private CharactersAdapter mCharactersAdapter;
    private List<Character> mCharacters;
    private List<House> mHouses;


    private MenuItem mSearchItem;

    private ChronosConnector mConnector;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        showSplash();

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        mHouses = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHandler = new Handler();
        setupToolbar();
        setupDrawer();
        loadHousesListFromDb();
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
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Обрабатывает событие onDestroy жизненного цикла Activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
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
            loadHousesFromNetwork();
        } else {
            mHouses = result.getOutput();
            showHouses();
        }
    }

    public void onOperationFinished(final SaveHousesListOperation.Result result) {
        loadHousesListFromDb();
    }

    private void loadHousesFromNetwork() {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<HouseRes> call = mDataManager.getUserListFromNetwork();
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
            SnackBarUtils.show(mCoordinatorLayout, getString(R.string.hint_not_connection_inet));
        }
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
        View headerLayout = mNavigationView.getHeaderView(0);

        drawerUserAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_avatar_iv);

        drawerUserFullName = (TextView) headerLayout.findViewById(R.id.drawer_user_name_tv);
        drawerUserEmail = (TextView) headerLayout.findViewById(R.id.drawer_user_email_tv);

        drawerUserFullName.setText(mDataManager.getPreferencesManager().getUserFullName());
        drawerUserEmail.setText(mDataManager.getPreferencesManager().getUserEmail());

        insertDrawerAvatar(mDataManager.getPreferencesManager().loadUserAvatar());

        mNavigationView.setCheckedItem(R.id.stark_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
                    case R.id.stark_menu:

                        break;
                    case R.id.targarien_menu:

                        break;
                    case R.id.lanister_menu:

                        break;
                }

                return false;
            }
        });
    }

    /**
     * инициализирует ToolBar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
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
     * Отображет список пользователей
     */
    private void showHouses() {
        hideSplash();
        if (mCharacters.size() == 0) {
            SnackBarUtils.show(mCoordinatorLayout, getString(R.string.error_load_users_list));
        } else {
            mCharactersAdapter = new CharactersAdapter(mCharacters, getApplicationContext(), new CustomClickListener() {
                @Override
                public void onUserItemClickListener(String action, int position) {
                    if (action.equals(ConstantManager.START_PROFILE_ACTIVITY_KEY)) {
//                        CharacterDTO characterDTO = new CharacterDTO(mCharacters.get(position));
//
//                        Intent profileIntent = new Intent(HouseListActivity.this, CharacterActivity.class);
//                        profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, characterDTO);
//                        startActivity(profileIntent);
                    } else if (action.equals(ConstantManager.LIKE_USER_KEY)) {

                    }
                }
            });
            mRecyclerView.swapAdapter(mCharactersAdapter, false);
        }
        EventBus.getDefault().post(new TimeEvent(ConstantManager.END_SHOW_USERS));
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
                    loadHousesListFromDb();
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

    /**
     * Разрешает осуществлять swipe элементов списка пользователей
     */
    private void setAllowSwipeUser() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int startPosition = viewHolder.getAdapterPosition();
                final int targetPosition = target.getAdapterPosition();

                mCharacters.add(targetPosition, mCharacters.remove(startPosition));
                mCharactersAdapter.notifyItemMoved(startPosition, targetPosition);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mCharacters.remove(position);
                mCharactersAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
