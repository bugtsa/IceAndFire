package com.bugtsa.iceandfire.ui.routers;

import android.support.v4.app.FragmentActivity;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.ui.fragments.CharacterFragment;
import com.bugtsa.iceandfire.utils.FragmentUtils;

public class CharactersRouter extends Router<FragmentActivity> {

    public CharactersRouter(FragmentActivity activity) {
        super(activity);
    }

    public void routeToCharacterDetails(CharacterOfHouse character) {
        CharacterDTO characterDTO = new CharacterDTO(character);
        FragmentUtils.replaceFragment(getActivity(), R.id.fragment_container, CharacterFragment.newInstance(characterDTO), true);
    }
}
