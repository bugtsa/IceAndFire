package com.bugtsa.iceandfire.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.TimeEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.tasks.LoadCharacterListByHouseIdOperation;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.databinding.FragmentHousesBinding;
import com.bugtsa.iceandfire.ui.adapters.CharactersAdapter;
import com.bugtsa.iceandfire.ui.routers.CharactersRouter;
import com.bugtsa.iceandfire.ui.views.IHouseView;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.redmadrobot.chronos.ChronosConnector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HouseFragment extends Fragment implements IHouseView{

    private static final String HOUSE_KEY = "HOUSE_KEY";
    private FragmentHousesBinding mBinding;
    private int mHouseKey;

    private DataManager mDataManager;

    private Context mContext;

    private ChronosConnector mConnector;

    private CharactersRouter mRouter;

    private CharactersAdapter mAdapter;

    private List<CharacterOfHouse> mCharacters;

    private List<House> mHouses;

    public static HouseFragment newInstance(int houseKey) {

        Bundle args = new Bundle();
        args.putInt(HOUSE_KEY, houseKey);
        HouseFragment fragment = new HouseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRouter = new CharactersRouter(getActivity());

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        mHouses = new ArrayList<>();
        mCharacters = new ArrayList<>();

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mHouseKey = getArguments().getInt(HOUSE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses, container, false);
        mBinding = DataBindingUtil.bind(view);

        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        mBinding.characters.setLayoutManager(layoutManager);
        mAdapter = new CharactersAdapter(characterOfHouse -> mRouter.routeToAccountDetails(characterOfHouse));
        mBinding.characters.setAdapter(mAdapter);
    }

    @Override
    public void showCharacters(List<CharacterOfHouse> characterList) {
        mAdapter.setCharacter(characterList);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnector.onResume();
        showData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mConnector.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    /**
     * Обрабатывает событие onStart жизненного цикла Activity
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Обрабатывает событие onStop жизненного цикла Activity
     */
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void showData() {
        loadHousesListFromDb();
    }

    /**
     * Загружает данные о домах из БД
     */
    private void loadHousesListFromDb() {
        try {
            mConnector.runOperation(new LoadHousesListOperation(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCharacterOfHouseFromDb() {
        try {
            mConnector.runOperation(new LoadCharacterListByHouseIdOperation(mHouseKey), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает результат операции LoadHousesListOperation
     *
     * @param result результат операции
     */
    public void onOperationFinished(final LoadHousesListOperation.Result result) {
        mHouses = result.getOutput();
        loadCharacterOfHouseFromDb();
    }

    public void onOperationFinished(final LoadCharacterListByHouseIdOperation.Result result) {
        mCharacters = result.getOutput();
        showCharacters(mCharacters);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHideSplash(TimeEvent timeEvent) {
        if (timeEvent.getTimeCode() == ConstantManager.HIDE_SPLASH) {
        }
    }
}
