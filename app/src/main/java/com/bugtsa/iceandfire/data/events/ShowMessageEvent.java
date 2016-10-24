package com.bugtsa.iceandfire.data.events;

public class ShowMessageEvent {

    private int mMessageKey;

    public ShowMessageEvent(int messageKey) {
        mMessageKey = messageKey;
    }

    public int getMessageKey() {
        return mMessageKey;
    }
}
