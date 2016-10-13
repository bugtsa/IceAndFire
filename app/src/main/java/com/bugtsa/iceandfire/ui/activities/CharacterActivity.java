package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.databinding.ItemCharacterBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.squareup.picasso.Picasso;

public class CharacterActivity extends AppCompatActivity {

    private ItemCharacterBinding mBinding;

    private DataManager mDataManager;

    private Context mContext;

    private String mRemoteIdShowUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.item_character);


        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        setupToolBar();
        initProfileData();
    }

    /**
     * Устанавливает ToolBar
     */
    private void setupToolBar() {
        setSupportActionBar(mBinding.toolbarCharacter);
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

        mBinding.collapsingToolbarCharacter.setTitle(characterDTO.getFullName());

        Picasso.with(this)
                .load(characterDTO.getPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mBinding.characterImageView);
    }
}
