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
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bugtsa.iceandfire.BuildConfig;
import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.PreferencesManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
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

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_PAGE_ID;

public class SplashActivity extends AppCompatActivity implements ISplashView {
    private static final String TAG = ConstantManager.TAG_PREFIX + SplashActivity.class.getSimpleName();

    SplashPresenter mPresenter = SplashPresenter.getInstance();

    private HouseFragment targarienFragment;
    private HouseFragment lannisterFragment;
    private HouseFragment starkFragment;

    private ActivitySplashBinding mBinding;
    private ImageView drawerUserAvatar;
    private TextView drawerUserFullName;
    private TextView drawerUserEmail;
    private PreferencesManager mPreferencesManager;

    private ViewPagerAdapter mViewPagerAdapter;

    protected ProgressDialog mProgressDialog;

    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mBinding.navigationDrawerHouseList.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager mViewPager) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mViewPagerAdapter = new ViewPagerAdapter(supportFragmentManager, this);
        if (starkFragment == null) {
            starkFragment = new HouseFragment();
            targarienFragment = new HouseFragment();
            lannisterFragment = new HouseFragment();
        }
        mViewPagerAdapter.addFragment(starkFragment, getString(R.string.stark_title));
        mViewPagerAdapter.addFragment(targarienFragment, getString(R.string.targarien_title));
        mViewPagerAdapter.addFragment(lannisterFragment, getString(R.string.lannister_title));
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initViewPager() {
        setupViewPager(mBinding.viewpagerHouseList);

        mBinding.viewpagerHouseList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment item = mViewPagerAdapter.getItem(position);
//                item.onResume();
                setCheckedItemNavigationView(position);
                String tag = item.getTag();
                currentIndex = position;
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
            case ConstantManager.STARK_PAGE_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
                break;
            case TARGARIEN_PAGE_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.targarien_menu);
                break;
            case ConstantManager.LANNISTER_PAGE_ID:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.lannister_menu);
                break;
            default:
                mBinding.navigationViewHouseList.setCheckedItem(R.id.stark_menu);
        }
        starkFragment.showPage(position);
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
                        selectPage(ConstantManager.STARK_PAGE_ID);
                        break;
                    case R.id.targarien_menu:
                        selectPage(TARGARIEN_PAGE_ID);
                        break;
                    case R.id.lannister_menu:
                        selectPage(ConstantManager.LANNISTER_PAGE_ID);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void selectPage(int pageIndex) {
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

    public void showCharacters(List<CharacterOfHouse> characterOfHouseList) {
        if (currentIndex == ConstantManager.STARK_PAGE_ID) {
            starkFragment.showCharacters(characterOfHouseList);
        }
        if (currentIndex == ConstantManager.TARGARIEN_PAGE_ID)
        {
            targarienFragment.showCharacters(characterOfHouseList);
        }
        if (currentIndex == ConstantManager.LANNISTER_PAGE_ID) {
            lannisterFragment.showCharacters(characterOfHouseList);
        }
    }
}
