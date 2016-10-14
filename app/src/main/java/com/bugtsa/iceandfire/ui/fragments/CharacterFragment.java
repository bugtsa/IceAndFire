package com.bugtsa.iceandfire.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.ui.activities.CharacterActivity;

import static com.bugtsa.iceandfire.utils.ConstantManager.PARCELABLE_KEY;

public class CharacterFragment extends Fragment {

    private CharacterDTO mCharacter;

    public static CharacterFragment newInstance(CharacterDTO characterRemoteId) {

        Bundle args = new Bundle();
        args.putParcelable(PARCELABLE_KEY, characterRemoteId);
        CharacterFragment fragment = new CharacterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCharacter = getArguments().getParcelable(PARCELABLE_KEY);

        Intent intent = new Intent(getActivity(), CharacterActivity.class);
        intent.putExtra(PARCELABLE_KEY, mCharacter);
        getActivity().startActivity(intent);
    }
}
