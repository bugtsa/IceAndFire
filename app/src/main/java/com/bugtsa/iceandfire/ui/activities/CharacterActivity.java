package com.bugtsa.iceandfire.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.databinding.ActivityCharacterBinding;
import com.bugtsa.iceandfire.mvp.presenters.CharacterPresenter;
import com.bugtsa.iceandfire.mvp.views.ICharacterView;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.SnackBarUtils;

import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_KEY;

public class CharacterActivity extends AppCompatActivity implements View.OnClickListener, ICharacterView {

    private CharacterPresenter mCharacterPresenter = CharacterPresenter.getInstance();

    private ActivityCharacterBinding mBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_character);

        mBinding.fatherCharacterButton.setOnClickListener(this);
        mBinding.motherCharacterButton.setOnClickListener(this);

        setupToolBar();
        mCharacterPresenter.takeView(this);
        mCharacterPresenter.initView();
        mCharacterPresenter.initCharacterData(getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY));
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.father_character_button:
//                mOpenCharacter = new CharacterDTO(mFatherOfCharacter);
//                break;
//            case R.id.mother_character_button:
//                mOpenCharacter = new CharacterDTO(mMotherOfCharacter);
//                break;
//        }
//        Intent intent = new Intent(this, CharacterActivity.class);
//        intent.putExtra(PARCELABLE_KEY, mOpenCharacter);
//        startActivity(intent);
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

    private int getIdDrawable(String houseRemoteId) {
        int idDrawable = R.drawable.stark;
        switch (Integer.parseInt(houseRemoteId)) {
            case STARK_KEY:
                idDrawable = R.drawable.stark;
                break;
            case TARGARIEN_KEY:
                idDrawable = R.drawable.targarien;
                break;
            case LANNISTER_KEY:
                idDrawable = R.drawable.lannister;
                break;
            default:
                idDrawable = R.drawable.stark;
        }
        return idDrawable;
    }

    @Override
    public void setNameCharacter(String nameCharacter) {
        mBinding.collapsingToolbarCharacter.setTitle(nameCharacter);
    }

    @Override
    public void setImageHouse(String houseRemoteId) {
//        houseRemoteId
//        mBinding.characterImageView.setImageDrawable(getDrawable(getIdDrawable(mCharacterDTO.getHouseRemoteId())));
    }

    @Override
    public void setDateBorn(String dateBorn) {
        mBinding.bornCharacterTextView.setText(dateBorn);
    }

    @Override
    public void setAliasesCharacter(String aliases) {
        mBinding.aliasesCharacterTextView.setText(aliases);
    }

    @Override
    public void setTitlesCharacter(String titles) {
        mBinding.titlesCharacterTextView.setText(titles);
    }

    @Override
    public void setWoodsCharacter(String woods) {
        if (woods.isEmpty()) {
            woods = getString(R.string.wood_not_found);
        }
        mBinding.wordsCharacterTextView.setText(woods);
    }

    @Override
    public void showSeasonDiedCharacter(String nameCharacter, String season) {
        String diedCharacterMessage = nameCharacter + " " + getString(R.string.died) + " " + season + " " + getString(R.string.season);
        SnackBarUtils.show(mBinding.coordinatorLayoutCharacter, diedCharacterMessage);
    }

    @Override
    public void setVisibleFather(int stateVisible) {
        mBinding.fatherCharacterButton.setVisibility(stateVisible);
        mBinding.fatherCharacterTextView.setVisibility(stateVisible);
    }

    @Override
    public void setVisibleMother(int stateVisible) {
        mBinding.motherCharacterButton.setVisibility(stateVisible);
        mBinding.motherCharacterTextView.setVisibility(stateVisible);
    }

    @Override
    public void setFatherName(String nameFather) {
        mBinding.motherCharacterButton.setText(nameFather);
    }

    @Override
    public void setMotherName(String nameMother) {
        mBinding.motherCharacterButton.setText(nameMother);
    }
}
