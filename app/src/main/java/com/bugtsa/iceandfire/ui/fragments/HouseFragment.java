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
import com.bugtsa.iceandfire.data.managers.DataManager;
import com.bugtsa.iceandfire.data.network.res.HouseRes;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseFragment extends Fragment {

    private FragmentHousesBinding mBinding;

    private static final String HOUSE_KEY = "HOUSE_KEY";

    private int mHouseKey;

    private DataManager mDataManager;

    private Context mContext;

    private ChronosConnector mConnector;

    private List<Character> mCharacters;

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

        loadHousesListFromDb();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses, container, false);
        mBinding = DataBindingUtil.bind(view);

        mHouseKey = getArguments().getInt(HOUSE_KEY);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

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
            showHouses();
        }
    }

    public void onOperationFinished(final SaveHousesListOperation.Result result) {
        loadHousesListFromDb();
    }


    /**
     * Отображет список пользователей
     */
    private void showHouses() {
//        hideSplash();
        if (mCharacters.size() == 0) {
//            SnackBarUtils.show(mBinding.coordinatorLayoutHouseList, getString(R.string.error_load_users_list));
        } else {
//            mCharactersAdapter = new CharactersAdapter(mCharacters, getApplicationContext(), new CustomClickListener() {
//                @Override
//                public void onUserItemClickListener(String action, int position) {
//                    if (action.equals(ConstantManager.START_PROFILE_ACTIVITY_KEY)) {
////                        CharacterDTO characterDTO = new CharacterDTO(mCharacters.get(position));
////
////                        Intent profileIntent = new Intent(HouseListActivity.this, CharacterActivity.class);
////                        profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, characterDTO);
////                        startActivity(profileIntent);
//                    } else if (action.equals(ConstantManager.LIKE_USER_KEY)) {
//
//                    }
//                }
//            });
//            mBinding.houseListContent.recyclerViewHouseList.swapAdapter(mCharactersAdapter, false);
        }
//        EventBus.getDefault().post(new TimeEvent(ConstantManager.END_SHOW_USERS));
    }
}
