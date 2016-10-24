package com.bugtsa.iceandfire.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.databinding.FragmentHousesBinding;
import com.bugtsa.iceandfire.mvp.presenters.HousePresenter;
import com.bugtsa.iceandfire.mvp.views.IHouseView;
import com.bugtsa.iceandfire.ui.adapters.CharactersAdapter;
import com.bugtsa.iceandfire.ui.routers.CharactersRouter;
import com.bugtsa.iceandfire.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_KEY;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_KEY;

public class HouseFragment extends Fragment implements IHouseView {

    private FragmentHousesBinding mBinding;

    private DataManager mDataManager;

    private HousePresenter mHousePresenter;

    private CharactersRouter mRouter;

    private CharactersAdapter mAdapter;

    private List<CharacterOfHouse> mCharacters;

    private List<House> mHouses;

    private int mHouseKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHousePresenter = HousePresenter.getInstance();

        mRouter = new CharactersRouter(getActivity());

        mDataManager = DataManager.getInstance();

        mHouses = new ArrayList<>();
        mCharacters = new ArrayList<>();

        mHousePresenter.takeView(this);
        mHousePresenter.initView(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mHousePresenter.dropView();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses, container, false);

        mBinding = DataBindingUtil.bind(view);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHousePresenter.onResume();
//        showData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHousePresenter.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHousePresenter.onSaveInstanceState(outState);
    }

    private int getIdDrawableIconHouse() {
        int idDrawable = R.drawable.ic_item;
        switch (mHouseKey) {
            case STARK_KEY:
                idDrawable = R.drawable.ic_stark;
                break;
            case TARGARIEN_KEY:
                idDrawable = R.drawable.ic_targarien;
                break;
            case LANNISTER_KEY:
                idDrawable = R.drawable.ic_lanister;
                break;
            default:
                idDrawable = R.drawable.ic_item;
        }
        return idDrawable;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(false);
        mBinding.characters.setLayoutManager(layoutManager);
        mAdapter = new CharactersAdapter(characterOfHouse -> mRouter.routeToAccountDetails(characterOfHouse), mDataManager.getContext(), getIdDrawableIconHouse());
        mBinding.characters.setAdapter(mAdapter);
    }

    @Override
    public void showPage(int pageKey) {
//        int HouseKey;
        switch (pageKey) {
            case ConstantManager.STARK_PAGE_ID:
                mHouseKey = ConstantManager.STARK_KEY;
                break;
            case ConstantManager.TARGARIEN_PAGE_ID:
                mHouseKey = ConstantManager.TARGARIEN_KEY;
                break;
            case ConstantManager.LANNISTER_PAGE_ID:
                mHouseKey = ConstantManager.LANNISTER_KEY;
                break;
            default:
                mHouseKey = ConstantManager.STARK_KEY;
        }
        mHousePresenter.loadCharactersOfHouseFromDb(mHouseKey);
    }

    @Override
    public void showCharacters(List<CharacterOfHouse> characterList) {
        mCharacters = characterList;
        mAdapter.setCharacter(characterList);
    }
}
