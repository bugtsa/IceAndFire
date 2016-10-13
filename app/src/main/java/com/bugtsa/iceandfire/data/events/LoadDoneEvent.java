package com.bugtsa.iceandfire.data.events;

public class LoadDoneEvent {

    private Long mTimeOfEvent;

    public LoadDoneEvent(Long timeOfEvent) {
        mTimeOfEvent = timeOfEvent;
    }

    public Long getTimeOfEvent() {
        return mTimeOfEvent;
    }
}
