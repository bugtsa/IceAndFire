package com.bugtsa.iceandfire.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.events.LoadDoneEvent;
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.House;
import com.bugtsa.iceandfire.data.storage.tasks.LoadHousesListOperation;
import com.bugtsa.iceandfire.data.storage.tasks.SaveHousesListOperation;
import com.bugtsa.iceandfire.databinding.FragmentHousesBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;
import com.bugtsa.iceandfire.utils.LogUtils;
import com.bugtsa.iceandfire.utils.NetworkStatusChecker;
import com.redmadrobot.chronos.ChronosConnector;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseFragment extends Fragment {

    private static final String HOUSE_KEY = "HOUSE_KEY";
    private FragmentHousesBinding mBinding;
    private int mHouseKey;

    private DataManager mDataManager;

    private Context mContext;

    private ChronosConnector mConnector;

    private List<CharacterOfHouse> mCharacters;
    private List<CharacterOfHouse> mUpdateCharacters;

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

        mDataManager = DataManager.getInstance();
        mContext = mDataManager.getContext();

        mHouses = new ArrayList<>();
        mCharacters = new ArrayList<>();
        mUpdateCharacters = new ArrayList<>();

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mHouseKey = getArguments().getInt(HOUSE_KEY);
//        loadHousesListFromDb();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses, container, false);
        mBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnector.onResume();
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

    private void loadHousesFromNetwork() {
        if (NetworkStatusChecker.isNetworkAvailable(mContext)) {
            Call<HouseRes> call = mDataManager.getHouseFromNetwork(String.valueOf(mHouseKey));
            call.enqueue(new Callback<HouseRes>() {
                @Override
                public void onResponse(Call<HouseRes> call, Response<HouseRes> response) {
                    if (response.code() == ConstantManager.RESPONSE_OK) {
                        mConnector.runOperation(new SaveHousesListOperation(response), false);
                        LogUtils.d("response ok");
                    } else {
                        LogUtils.d("response not ok");
                    }

                }

                @Override
                public void onFailure(Call<HouseRes> call, Throwable t) {
                    LogUtils.d("failed server");
                }
            });
        } else {
//            SnackBarUtils.show(mBinding.coordinatorLayoutHouseList, getString(R.string.hint_not_connection_inet));
        }
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

    /**
     * Обрабатывает результат операции LoadHousesListOperation
     *
     * @param result результат операции
     */
    public void onOperationFinished(final LoadHousesListOperation.Result result) {
        if (result.getOutput().isEmpty()) {
            loadHousesFromNetwork();
        } else {
            mHouses = result.getOutput();
            loadCharacterOfHouse();
        }
    }

    public void onOperationFinished(final SaveHousesListOperation.Result result) {
        loadHousesListFromDb();
    }



    private void showCharacters() {

    }



    /**
     * Отображет список пользователей
     */
    private void loadCharacterOfHouse() {

        EventBus.getDefault().post(new LoadDoneEvent(System.currentTimeMillis()));
    }
}
