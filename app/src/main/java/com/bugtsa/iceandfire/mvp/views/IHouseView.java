package com.bugtsa.iceandfire.mvp.views;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;

import java.util.List;

public interface IHouseView {

    void showPage(int pageKey);

    void showCharacters(List<CharacterOfHouse> characterList);
}
