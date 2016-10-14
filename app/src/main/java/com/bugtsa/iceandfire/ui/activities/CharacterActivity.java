package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.databinding.FragmentCharacterBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;

public class CharacterActivity extends AppCompatActivity {

    private FragmentCharacterBinding mBinding;

    private DataManager mDataManager;

    private Context mContext;

    private String mRemoteIdShowUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_character);


        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        setupToolBar();
        initCharacterData();
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
    private void initCharacterData() {
        CharacterDTO characterDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

//        mRemoteIdShowUser = characterDTO.getRemoteId();

        if (characterDTO.getName() != null) {
            mBinding.collapsingToolbarCharacter.setTitle(characterDTO.getName());
        }

        if (characterDTO.getBorn() != null) {
            mBinding.bornCharacterTextView.setText(characterDTO.getBorn());
        }

        if (characterDTO.getAlias() != null) {
            mBinding.aliasesCharacterTextView.setText(characterDTO.getAlias());
        }

        int visibleMotherComponents;
        if (characterDTO.getMother().isEmpty()) {
            visibleMotherComponents = View.INVISIBLE;
        } else {
            visibleMotherComponents = View.VISIBLE;
            mBinding.motherCharacterButton.setText(characterDTO.getMother());
        }
        mBinding.motherCharacterButton.setVisibility(visibleMotherComponents);
        mBinding.motherCharacterTextView.setVisibility(visibleMotherComponents);

        int visibleFatherComponents;
        if (characterDTO.getFather().isEmpty()) {
            visibleFatherComponents = View.INVISIBLE;
        } else {
            visibleFatherComponents = View.VISIBLE;
            mBinding.fatherCharacterButton.setText(characterDTO.getFather());
        }
        mBinding.fatherCharacterButton.setVisibility(visibleFatherComponents);
        mBinding.fatherCharacterTextView.setVisibility(visibleFatherComponents);
    }
}
