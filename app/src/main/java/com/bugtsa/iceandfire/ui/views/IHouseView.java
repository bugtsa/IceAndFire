package com.bugtsa.iceandfire.ui.views;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

import java.util.List;

public interface IHouseView extends IView {
    void showCharacters(List<CharacterOfHouse> characterList);
}
