package com.bugtsa.iceandfire.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.databinding.ActivityCharacterBinding;
import com.bugtsa.iceandfire.mvp.presenters.CharacterPresenter;
import com.bugtsa.iceandfire.mvp.views.ICharacterView;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.SnackBarUtils;

import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.PARCELABLE_CHARACTER_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_KEY;

public class CharacterActivity extends AppCompatActivity implements ICharacterView {

    private CharacterPresenter mCharacterPresenter = CharacterPresenter.getInstance();

    private ActivityCharacterBinding mBinding;

    public static Intent newIntent(Context packageContext, CharacterDTO characterDTO) {
        Intent intent = new Intent(packageContext, CharacterActivity.class);
        intent.putExtra(PARCELABLE_CHARACTER_KEY, characterDTO);
        return intent;
    }

    //region Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_character);

        mBinding.fatherCharacterButton.setOnClickListener(view -> mCharacterPresenter.onFatherButtonClick());
        mBinding.motherCharacterButton.setOnClickListener(view -> mCharacterPresenter.onMotherButtonClick());

        setupToolBar();
        mCharacterPresenter.takeView(this);
        mCharacterPresenter.initView();
        mCharacterPresenter.initCharacterData(getIntent().getParcelableExtra(PARCELABLE_CHARACTER_KEY));
    }
    //endregion

    //region init ui components

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Обрабатывает событие onBackPressed(нажатие системной клавиши back)
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    //endregion

    //region ICharacterView
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

    private Drawable getHouseImage(final String houseRemoteId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(getIdDrawable(houseRemoteId), DataManager.getInstance().getContext().getTheme());
        } else {
            return getResources().getDrawable(getIdDrawable(houseRemoteId));
        }
    }

    @Override
    public void setNameCharacter(String nameCharacter) {
        mBinding.collapsingToolbarCharacter.setTitle(nameCharacter);
    }

    @Override
    public void setImageHouse(String houseRemoteId) {
        mBinding.characterImageView.setImageDrawable(getHouseImage(houseRemoteId));
    }

    @Override
    public void setDateBorn(String dateBorn) {
        mBinding.bornLayout.setVisibility(View.VISIBLE);
        mBinding.bornCharacterTextView.setText(dateBorn);
    }

    @Override
    public void setInvisibleDateBorn() {
        mBinding.bornLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAliasesCharacter(String aliases) {
        mBinding.aliasesLayout.setVisibility(View.VISIBLE);
        mBinding.aliasesCharacterTextView.setText(aliases);
    }

    @Override
    public void setInvisibleAliases() {
        mBinding.aliasesLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTitlesCharacter(String titles) {
        mBinding.titlesLayout.setVisibility(View.VISIBLE);
        mBinding.titlesCharacterTextView.setText(titles);
    }

    @Override
    public void setInvisibleTitles() {
        mBinding.titlesLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setWoodsCharacter(String woods) {
        if (woods.isEmpty()) {
            woods = getString(R.string.wood_not_found);
        }
        mBinding.wordsCharacterTextView.setText(woods);
    }

    @Override
    public void showSeasonDiedCharacter(String nameCharacter, String season, String characterDateDied) {
        String seasonDied = " on: ";
        String dateDied = " ";
        if (season.equals("")) {
            seasonDied += getString(R.string.unknown_season);
        } else {
            seasonDied += season;
        }
        if (characterDateDied.contains(ConstantManager.WORD_IN_DIED_CHARACTER)) {
            dateDied += characterDateDied;
        } else {
            dateDied += getString(R.string.word_in) + characterDateDied;
        }
        String diedCharacterMessage = nameCharacter + " " + getString(R.string.died) + seasonDied + dateDied;
        SnackBarUtils.show(mBinding.coordinatorLayoutCharacter, diedCharacterMessage);
    }

    @Override
    public void setInvisibleFather() {
        mBinding.fatherLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setInvisibleMother() {
        mBinding.motherLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setFatherName(String nameFather) {
        mBinding.fatherLayout.setVisibility(View.VISIBLE);
        mBinding.fatherCharacterButton.setText(nameFather);
    }

    @Override
    public void setMotherName(String nameMother) {
        mBinding.motherLayout.setVisibility(View.VISIBLE);
        mBinding.motherCharacterButton.setText(nameMother);
    }

    @Override
    public void openParentOfCharacter(CharacterDTO parentOfCharacter) {
        Intent intent = CharacterActivity.newIntent(this, parentOfCharacter);
        startActivity(intent);
    }
    //endregion
}
