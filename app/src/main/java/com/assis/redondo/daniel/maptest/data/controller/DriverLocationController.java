package com.assis.redondo.daniel.maptest.data.controller;

import android.content.Context;

import com.assis.redondo.daniel.maptest.data.loader.LastLocationsLoader;
import com.assis.redondo.daniel.maptest.data.vo.LocationVO;
import com.assis.redondo.daniel.maptest.dispatcher.DataEvent;
import com.assis.redondo.daniel.maptest.dispatcher.Event;
import com.assis.redondo.daniel.maptest.dispatcher.EventDispatcher;
import com.assis.redondo.daniel.maptest.dispatcher.EventListener;

import java.util.ArrayList;

public class DriverLocationController extends EventDispatcher {

    private static final String TAG = DriverLocationController.class.getSimpleName();

    private static DriverLocationController instance;
    static final Object sLock = new Object();
    private Context context;
    private LastLocationsLoader mDriversLastLocationsLoader;

    public enum LoadStatus {
        NOT_LOADED,
        LOADED,
        ERROR,
        LOADING
    }

    public static DriverLocationController getInstance(Context ctx) {
        synchronized (sLock) {
            if (instance == null) {
                instance = new DriverLocationController(ctx.getApplicationContext());
            }
            return instance;
        }
    }

    public DriverLocationController(Context ctx) {
        context = ctx;
    }

    public LastLocationsLoader getDriversLastLocationsLoader() {
        return mDriversLastLocationsLoader;
    }

    public void getDriversLastLocations(){

            mDriversLastLocationsLoader = new LastLocationsLoader();
            mDriversLastLocationsLoader.addListener(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, lastLocationsListener);
            mDriversLastLocationsLoader.getDriversLastLocations();

        /*if (mDriversLastLocationsLoader.loadStatus == LoadStatus.LOADED) {

            dispatchEvent(new DataEvent(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, 0));
            mDriversLastLocationsLoader.removeListener(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, lastLocationsListener);

        } else if (mDriversLastLocationsLoader.loadStatus == LoadStatus.ERROR) {
            dispatchEvent(new DataEvent(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, 0));
            mDriversLastLocationsLoader.removeListener(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, lastLocationsListener);
        }
        */


    }

    private EventListener lastLocationsListener = new EventListener() {

        @Override
        public void onEvent(Event event) {
             if (mDriversLastLocationsLoader.loadStatus == LoadStatus.LOADED) {
                 dispatchEvent(new DataEvent(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, 0));
            } else if (mDriversLastLocationsLoader.loadStatus == LoadStatus.ERROR) {
                 dispatchEvent(new DataEvent(DataEvent.POPULATE_DRIVERS_LAST_LOCATIONS, 0));
            }
        }
    };

    public ArrayList<LocationVO> getDriversLoadedLocations(){
        if(mDriversLastLocationsLoader != null ){
            ArrayList<LocationVO> driversLocations = new ArrayList<>(mDriversLastLocationsLoader.getLoadedLocations());
            if(driversLocations.size() > 0) {
                return mDriversLastLocationsLoader.getLoadedLocations();
            } else {
                return null;
            }
        }
        return null;
    }

}
