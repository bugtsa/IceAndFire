package com.bugtsa.iceandfire.ui.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.bugtsa.iceandfire.databinding.FragmentHousesBinding;
import com.bugtsa.iceandfire.mvp.presenters.HousePresenter;
import com.bugtsa.iceandfire.mvp.views.IHouseView;
import com.bugtsa.iceandfire.ui.adapters.CharactersAdapter;
import com.bugtsa.iceandfire.ui.routers.CharactersRouter;

import java.util.List;

import static com.bugtsa.iceandfire.utils.ConstantManager.KEY_HOUSE_INDEX;
import static com.bugtsa.iceandfire.utils.ConstantManager.LANNISTER_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.STARK_PAGE_ID;
import static com.bugtsa.iceandfire.utils.ConstantManager.TARGARIEN_PAGE_ID;

public class HouseFragment extends Fragment implements IHouseView {

    private FragmentHousesBinding mBinding;

    private HousePresenter mHousePresenter;

    private CharactersRouter mRouter;

    private CharactersAdapter mAdapter;

    private List<CharacterOfHouse> mCharacterList;

    private int mHousePageId;

    //region Life cycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mHousePageId = getArguments().getInt(KEY_HOUSE_INDEX);

        mHousePresenter = HousePresenter.getInstance();

        mRouter = new CharactersRouter(getActivity());

        mHousePresenter.takeView(this);
        mHousePresenter.initView();
        mHousePresenter.loadCharactersOfHouseFromDb(mHousePageId);
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
    //endregion

    //region Show ui components
    private int getIdDrawableIconHouse() {
        int idDrawable;
        switch (mHousePageId) {
            case STARK_PAGE_ID:
                idDrawable = R.drawable.ic_stark;
                break;
            case LANNISTER_PAGE_ID:
                idDrawable = R.drawable.ic_lanister;
                break;
            case TARGARIEN_PAGE_ID:
                idDrawable = R.drawable.ic_targarien;
                break;
            default:
                idDrawable = R.drawable.ic_item;
        }
        return idDrawable;
    }

    private Drawable getDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(getIdDrawableIconHouse(), DataManager.getInstance().getContext().getTheme());
        } else {
            return getResources().getDrawable(getIdDrawableIconHouse());
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(false);
        mBinding.characters.setLayoutManager(layoutManager);
        mAdapter = new CharactersAdapter(characterOfHouse -> mRouter.routeToCharacterDetails(characterOfHouse), getDrawable());
        mBinding.characters.setAdapter(mAdapter);
        mAdapter.setCharacter(mCharacterList);
    }
    //endregion

    //region IHouseView
    @Override
    public void showCharacters(List<CharacterOfHouse> characterList) {
        mCharacterList = characterList;
    }
    //endregion
}
