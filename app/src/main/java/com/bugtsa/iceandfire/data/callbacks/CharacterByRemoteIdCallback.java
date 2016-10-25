package com.bugtsa.iceandfire.data.callbacks;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

public interface CharacterByRemoteIdCallback {

    void getCharacterByRemoteId(CharacterOfHouse character);
}
