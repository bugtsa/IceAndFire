package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterActivity extends AppCompatActivity {

    @BindView(R.id.profile_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar_profile)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar_profile)
    Toolbar mToolBar;
    @BindView(R.id.user_profile_iv_profile)
    ImageView mProfileImage;
    @BindView(R.id.fab_user_profile)
    ImageView mUserLike;
    @BindView(R.id.about_me_et_profile)
    EditText mUserBio;
    @BindView(R.id.rating_quantity_static_tv)
    TextView mUserRating;
    @BindView(R.id.lines_code_quantity_static_tv)
    TextView mUserCodeLines;
    @BindView(R.id.project_quantity_static_tv)
    TextView mUserProjects;

    @BindView(R.id.repositories_list)
    ListView mRepoListView;

    private DataManager mDataManager;

    private Context mContext;

    private String mRemoteIdShowUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        setupToolBar();
        initProfileData();
    }

    /**
     * Устанавливает ToolBar
     */
    private void setupToolBar() {
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Инициализирует данные пользователя
     */
    private void initProfileData() {
        CharacterDTO characterDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        mRemoteIdShowUser = characterDTO.getRemoteId();
        mUserBio.setText(characterDTO.getBio());
        mUserRating.setText(characterDTO.getRating());
        mUserCodeLines.setText(characterDTO.getCodeLines());
        mUserProjects.setText(characterDTO.getProjects());

        mCollapsingToolbarLayout.setTitle(characterDTO.getFullName());

        Picasso.with(this)
                .load(characterDTO.getPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mProfileImage);
    }
}
