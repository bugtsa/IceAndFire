package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.tasks.LoadCharacterByRemoteIdOperation;
import com.bugtsa.iceandfire.data.storage.tasks.LoadTitleHouseOperation;
import com.bugtsa.iceandfire.databinding.FragmentCharacterBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.SnackBarUtils;
import com.bugtsa.iceandfire.utils.StringUtils;
import com.redmadrobot.chronos.ChronosConnector;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_CHAR;
import static com.bugtsa.iceandfire.utils.ConstantManager.NEW_STRING_SYMBOL_STR;
import static com.bugtsa.iceandfire.utils.ConstantManager.PARCELABLE_KEY;

public class CharacterActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentCharacterBinding mBinding;

    private DataManager mDataManager;

    private Context mContext;

    private ChronosConnector mConnector;

    private CharacterDTO mCharacterDTO;

    private CharacterOfHouse mFatherOfCharacter;

    private CharacterOfHouse mMotherOfCharacter;

    private CharacterDTO mOpenCharacter;

    private String mDiedCharacterMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_character);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        mBinding.fatherCharacterButton.setOnClickListener(this);
        mBinding.motherCharacterButton.setOnClickListener(this);

        setupToolBar();
        initCharacterData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.father_character_button:
                mOpenCharacter = new CharacterDTO(mFatherOfCharacter);
                break;
            case R.id.mother_character_button:
                mOpenCharacter = new CharacterDTO(mMotherOfCharacter);
                break;
        }
        Intent intent = new Intent(this, CharacterActivity.class);
        intent.putExtra(PARCELABLE_KEY, mOpenCharacter);
        startActivity(intent);
    }

    /**
     * Устанавливает ToolBar
     */
    private void setupToolBar() {
        setSupportActionBar(mBinding.toolbarCharacter);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    /**
     * Обрабатывает событие onBackPressed(нажатие системной клавиши back)
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    /**
     * Инициализирует данные пользователя
     */
    private void initCharacterData() {
        mCharacterDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        if (mCharacterDTO.getName() != null) {
            mBinding.collapsingToolbarCharacter.setTitle(mCharacterDTO.getName());
        }

        getWords(mCharacterDTO.getHouseRemoteId());

        if (mCharacterDTO.getBorn() != null) {
            mBinding.bornCharacterTextView.setText(mCharacterDTO.getBorn());
        }

        if (mCharacterDTO.getDied() != null) {
            if (!mCharacterDTO.getDied().isEmpty()) {
                mDiedCharacterMessage = mCharacterDTO.getName() + " " + getString(R.string.died) + " " + mCharacterDTO.getDied();
                SnackBarUtils.show(mBinding.coordinatorLayoutCharacter, mDiedCharacterMessage);
            }
        }

        if (mCharacterDTO.getTitles() != null) {
            mBinding.titlesCharacterTextView.setText(getStringList(mCharacterDTO.getTitles()));
        }

        if (mCharacterDTO.getAliases() != null) {
            mBinding.aliasesCharacterTextView.setText(getStringList(mCharacterDTO.getAliases()));
        }

        if (!mCharacterDTO.getMother().isEmpty()) {
            getParentName(mCharacterDTO.getMother());
        } else {
            setVisibleMother(View.INVISIBLE);
        }

        if (!mCharacterDTO.getFather().isEmpty()) {
            getParentName(mCharacterDTO.getFather());
        } else {
            setVisibleFather(View.INVISIBLE);
        }
    }

    private String getStringList(List<String> stringList) {
        String listForOutput = "";
        if (!stringList.isEmpty()) {
            for (String title : stringList) {
                listForOutput += title;
                listForOutput += NEW_STRING_SYMBOL_STR;
            }
            listForOutput = StringUtils.removeLastChar(listForOutput, NEW_STRING_SYMBOL_CHAR);
        }
        return listForOutput;
    }

    private void getWords(String houseRemoteId) {
        mConnector.runOperation(new LoadTitleHouseOperation(houseRemoteId), false);
    }

    private void getParentName(String parentRemoteId) {
        mConnector.runOperation(new LoadCharacterByRemoteIdOperation(parentRemoteId), false);
    }

    public void onOperationFinished(final LoadTitleHouseOperation.Result result) {
        if (!result.getOutput().isEmpty()) {
            mBinding.wordsCharacterTextView.setText(result.getOutput());
        } else {
//            SnackBarUtils.showWithAction(mBinding.coordinatorLayoutCharacter, mDiedCharacterMessage, getString(R.string.allegiances_dont_have));
        }
    }

    public void onOperationFinished(final LoadCharacterByRemoteIdOperation.Result result) {
        CharacterOfHouse parentOfCharacter = result.getOutput();
        if (parentOfCharacter.getRemoteId().equals(mCharacterDTO.getMother())) {
            mMotherOfCharacter = parentOfCharacter;
            mBinding.motherCharacterButton.setText(parentOfCharacter.getName());
            setVisibleMother(View.VISIBLE);
        } else if (parentOfCharacter.getRemoteId().equals(mCharacterDTO.getFather())) {
            mFatherOfCharacter = parentOfCharacter;
            mBinding.fatherCharacterButton.setText(parentOfCharacter.getName());
            setVisibleFather(View.VISIBLE);
        }
    }

    private void setVisibleFather(int stateVisible) {
        mBinding.fatherCharacterButton.setVisibility(stateVisible);
        mBinding.fatherCharacterTextView.setVisibility(stateVisible);
    }

    private void setVisibleMother(int stateVisible) {
        mBinding.motherCharacterButton.setVisibility(stateVisible);
        mBinding.motherCharacterTextView.setVisibility(stateVisible);
    }

//    private void loadParentNameFronNetwork(String parentUrl) {
//        Call<CharacterRes> call = mDataManager.getCharacterFromNetwork(StringUtils.getIdFromUrlApi(parentUrl));
//        call.enqueue(new Callback<CharacterRes>() {
//            @Override
//            public void onResponse(Call<CharacterRes> call, Response<CharacterRes> response) {
//                if(response.code() == ConstantManager.RESPONSE_OK) {
//                    parentFatherName = response.body().getName();
//                } else {
//                    parentFatherName = null;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CharacterRes> call, Throwable t) {
//                parentFatherName = null;
//            }
//        });
//    }
}
