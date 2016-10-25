package com.bugtsa.iceandfire.data.callbacks;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

import java.util.List;

public interface CharacterListByHouseIdCallback {

    void getCharactersList(List<CharacterOfHouse> listCharacter);
}
