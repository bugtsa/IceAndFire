package com.bugtsa.iceandfire.mvp.views;

import com.bugtsa.iceandfire.data.storage.models.CharacterDTO;

public interface ICharacterView {

    void setNameCharacter(String nameCharacter);

    void setImageHouse(String houseRemoteId);

    void setWoodsCharacter(String woods);

    void setDateBorn(String dateBorn);

    void showSeasonDiedCharacter(String nameCharacter, String season, String dateDied);

    void setTitlesCharacter(String titles);

    void setAliasesCharacter(String aliases);

    void setInvisibleFather();

    void setFatherName(String nameFather);

    void setInvisibleMother();

    void setMotherName(String nameMother);

    void openParentOfCharacter(CharacterDTO parentOfCharacter);

    void setInvisibleDateBorn();

    void setInvisibleTitles();

    void setInvisibleAliases();
}
