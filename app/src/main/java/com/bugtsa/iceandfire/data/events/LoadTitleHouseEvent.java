package com.bugtsa.iceandfire.data.events;

public class LoadTitleHouseEvent {

    private String mTitleHouse;

    public LoadTitleHouseEvent(String titleHouse) {
        mTitleHouse = titleHouse;
    }

    public String getTitleHouse() {
        return mTitleHouse;
    }
}
