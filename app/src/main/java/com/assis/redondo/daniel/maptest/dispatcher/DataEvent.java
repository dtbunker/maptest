package com.assis.redondo.daniel.maptest.dispatcher;

import android.content.Context;

public class DataEvent extends SimpleEvent {


    private int mData;
    public static final String DRIVERS_LAST_LOCATIONS_LOADED = "DRIVERS_LAST_LOCATIONS_LOADED";
    public static final String POPULATE_DRIVERS_LAST_LOCATIONS = "POPULATE_DRIVERS_LAST_LOCATIONS";

    public DataEvent(String type, int data) {
        super(type);
        mData = data;
    }
    public int getData() {
        return mData;
    }


}
