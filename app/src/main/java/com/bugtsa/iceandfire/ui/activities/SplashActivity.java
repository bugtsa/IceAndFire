package com.bugtsa.iceandfire.ui.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugtsa.iceandfire.BuildConfig;
import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.databinding.ActivitySplashBinding;
import com.bugtsa.iceandfire.mvp.presenters.ISplashPresenter;
import com.bugtsa.iceandfire.mvp.presenters.SplashPresenter;
import com.bugtsa.iceandfire.mvp.views.ISplashView;
import com.bugtsa.iceandfire.ui.adapters.ViewPagerAdapter;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.SnackBarUtils;
import com.squareup.picasso.Picasso;

import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.QUANTITY_VIEW_PAGE;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_PAGE_ID;

public class SplashActivity extends AppCompatActivity implements ISplashView {
    private static final String TAG = ConstantManager.TAG_PREFIX + SplashActivity.class.getSimpleName();

    SplashPresenter mPresenter = SplashPresenter.getInstance();

    private ActivitySplashBinding mBinding;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;

    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupToolbar();
        setupDrawer();

        mPresenter.takeView(this);
        mPresenter.initView();
    }

    @Override
    protected void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mBinding.navigationDrawerHouseList.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        mBinding.tabsHouseList.setupWithViewPager(mBinding.viewpagerHouseList);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mBinding.viewpagerHouseList.setOffscreenPageLimit(QUANTITY_VIEW_PAGE);
        mBinding.viewpagerHouseList.setAdapter(pagerAdapter);
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
                        selectPage(STARK_PAGE_ID);
                        break;
                    case R.id.lannister_menu:
                        selectPage(LANNISTER_PAGE_ID);
                        break;
                    case R.id.targarien_menu:
                        selectPage(TARGARIEN_PAGE_ID);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void selectPage(int pageIndex) {
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
        setupViewPager();
    }

    @Override
    public void setOrientation(int ActivityInfo) {
        setRequestedOrientation(ActivityInfo);
    }
}
