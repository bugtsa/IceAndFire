package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.databinding.FragmentCharacterBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterActivity extends AppCompatActivity {

    private FragmentCharacterBinding mBinding;

    private DataManager mDataManager;

    private Context mContext;

    private String mRemoteIdShowUser;

    private String parentMotherName = null;
    private String parentFatherName = null;
    private String parentName = null;


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

        if (!characterDTO.getFather().isEmpty()) {
            getParentName(characterDTO.getFather());
        }

    }

    private void setFather() {
        int visibleFatherComponents = View.VISIBLE;
        mBinding.fatherCharacterButton.setVisibility(visibleFatherComponents);
        mBinding.fatherCharacterTextView.setVisibility(visibleFatherComponents);
        mBinding.fatherCharacterButton.setText(parentFatherName);
    }

    private void getParentName(String parentUrl) {
        Call<CharacterRes> call = mDataManager.getCharacterFromNetwork(StringUtils.getIdFromUrlApi(parentUrl));
        call.enqueue(new Callback<CharacterRes>() {
            @Override
            public void onResponse(Call<CharacterRes> call, Response<CharacterRes> response) {
                if(response.code() == ConstantManager.RESPONSE_OK) {
                    parentFatherName = response.body().getName();
                    setFather();
                } else {
                    parentFatherName = null;
                }
            }

            @Override
            public void onFailure(Call<CharacterRes> call, Throwable t) {
                parentFatherName = null;
            }
        });
    }
}
