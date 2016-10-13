package com.bugtsa.iceandfire.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

public class CharacterFragment extends Fragment {

    private static final String WORDS_KEY = "WORDS_KEY";

    private String mWords;

    public static CharacterFragment newInstance(CharacterOfHouse character) {

        Bundle args = new Bundle();
//        args.putSerializable(WORDS_KEY, character);
        CharacterFragment fragment = new CharacterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        mWords = getArguments().getString(WORDS_KEY);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
