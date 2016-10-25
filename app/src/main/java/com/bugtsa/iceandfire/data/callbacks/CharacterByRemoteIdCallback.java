package com.bugtsa.iceandfire.data.callbacks;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;

public interface CharacterByRemoteIdCallback {

    void getCharacterByRemoteId(CharacterDTO characterDTO);
}
