package com.assis.redondo.daniel.maptest.data.loader;


import com.assis.redondo.daniel.maptest.data.controller.DriverLocationController;
import com.assis.redondo.daniel.maptest.data.task.TaskGetLastLocations;
import com.assis.redondo.daniel.maptest.data.vo.LocationVO;
import com.assis.redondo.daniel.maptest.dispatcher.DataEvent;
import com.assis.redondo.daniel.maptest.dispatcher.EventDispatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class LastLocationsLoader extends EventDispatcher implements Serializable {

    private static final long serialVersionUID = 1L;
    public DriverLocationController.LoadStatus loadStatus;
    private ArrayList<LocationVO> mLastDriversLocations;


    public void getDriversLastLocations() {

        if (loadStatus == DriverLocationController.LoadStatus.LOADING || loadStatus == DriverLocationController.LoadStatus.LOADED)
            return;

        this.loadStatus = DriverLocationController.LoadStatus.LOADING;

        TaskGetLastLocations task = new TaskGetLastLocations();

        task.setListener(new TaskGetLastLocations.LastLocationListener() {

            @Override
            public void eventosLoaded(LocationVO[] result) {
                if (result != null && result.length > 0) {

                    mLastDriversLocations = new ArrayList<LocationVO>(Arrays.asList(result));

                    loadStatus = DriverLocationController.LoadStatus.LOADED;
                    dispatchEvent(new DataEvent(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, 0));

                } else if (result != null && result.length == 0) {

                    loadStatus = DriverLocationController.LoadStatus.LOADED;
                    dispatchEvent(new DataEvent(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, 0));

                } else {
                    loadStatus = DriverLocationController.LoadStatus.ERROR;
                    dispatchEvent(new DataEvent(DataEvent.DRIVERS_LAST_LOCATIONS_LOADED, 0));
                }

            }

        });
        task.execute();
    }

    public  ArrayList<LocationVO>  getLoadedLocations() {
        return mLastDriversLocations;
    }

    public void reloadDriversLocations() {
        loadStatus = DriverLocationController.LoadStatus.NOT_LOADED;
        getDriversLastLocations();
    }


}
