package com.bugtsa.iceandfire.data.events;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

public class LoadCharacterByRemoterIdEvent {

    private CharacterOfHouse mCharacter;

    public LoadCharacterByRemoterIdEvent(CharacterOfHouse character) {
        mCharacter = character;
    }

    public CharacterOfHouse getCharacter() {
        return mCharacter;
    }
}
