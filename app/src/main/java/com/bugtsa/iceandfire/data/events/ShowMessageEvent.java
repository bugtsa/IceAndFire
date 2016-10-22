package com.bugtsa.iceandfire.data.events;

public class ShowMessageEvent {

    private String mMessage;

    public ShowMessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
