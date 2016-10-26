package com.bugtsa.iceandfire.ui.routers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;
import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.ui.activities.CharacterActivity;

public class CharactersRouter extends Router<FragmentActivity> {

    public CharactersRouter(FragmentActivity activity) {
        super(activity);
    }

    public void routeToCharacterDetails(CharacterOfHouse character) {
        CharacterDTO characterDTO = new CharacterDTO(character);
        Intent intent = CharacterActivity.newIntent(getActivity(), characterDTO);
        getActivity().startActivity(intent);
    }
}
