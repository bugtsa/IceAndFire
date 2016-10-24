package com.bugtsa.iceandfire.ui.activities;

import android.app.ProgressDialog;
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

import com.bugtsa.iceandfire.BuildConfig;
import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.managers.PreferencesManager;
import com.bugtsa.iceandfire.databinding.ActivitySplashBinding;
import com.bugtsa.iceandfire.mvp.presenters.ISplashPresenter;
import com.bugtsa.iceandfire.mvp.presenters.SplashPresenter;
import com.bugtsa.iceandfire.mvp.views.ISplashView;
import com.bugtsa.iceandfire.ui.adapters.ViewPagerAdapter;
import com.bugtsa.iceandfire.ui.fragments.HouseFragment;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.SnackBarUtils;
import com.squareup.picasso.Picasso;

public class SplashActivity extends BaseActivity implements ISplashView {
    private static final String TAG = ConstantManager.TAG_PREFIX + SplashActivity.class.getSimpleName();

    SplashPresenter mPresenter = SplashPresenter.getInstance();

    private static Fragment targarienFragment;
    private static Fragment lannisterFragment;
    private static Fragment starkFragment;

    private ActivitySplashBinding mBinding;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;
    private DataManager mDataManager;
    private PreferencesManager mPreferencesManager;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();
        if (mPreferencesManager.isFirstLaunch()) {
            mPreferencesManager.saveFirstLaunch(false);
        }

        initViewPager();
        setupToolbar();
        setupDrawer();

        mPresenter.takeView(this);
        mPresenter.initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    /**
     * Обрабатывает событие onResume жизненного цикла Activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    /**
     * Обрабатывает событие onPause жизненного цикла Activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mBinding.navigationDrawerHouseList.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public ISplashPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showMessage(int messageKey) {
        String message = "";
        switch (messageKey) {
            case ConstantManager.NETWORK_IS_NOT_AVAILABLE:
                message = getString(R.string.hint_not_connection_inet);
                break;

        }
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        SnackBarUtils.show(mBinding.coordinatorLayoutHouseList, message);
    }

    @Override
    public void showErrors(Throwable e) {
        if (BuildConfig.DEBUG) {
            showMessage(e.toString());
            e.printStackTrace();
        } else {
            showMessage(getString(R.string.not_correct_working_app));
        }
    }

    @Override
    public void showSplash() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.splash_screen);
    }

    @Override
    public void hideSplash() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }
    }

    @Override
    public void setOrientation(int ActivityInfo) {
        setRequestedOrientation(ActivityInfo);
    }
}
