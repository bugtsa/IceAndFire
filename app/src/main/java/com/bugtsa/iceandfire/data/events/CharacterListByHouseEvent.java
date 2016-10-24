package com.bugtsa.iceandfire.data.events;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

import java.util.List;

public class CharacterListByHouseEvent {

    private List<CharacterOfHouse> mCharactersList;

    public CharacterListByHouseEvent(List<CharacterOfHouse> charactersList) {
        mCharactersList = charactersList;
    }

    public List<CharacterOfHouse> getCharactersList() {
        return mCharactersList;
    }
}
