package com.assis.redondo.daniel.maptest.data.vo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DT on 10/9/15.
 */
public class LocationVO {

    private double latitude;
    private double longitude;
    private int driverId;
    private boolean driverAvaiable;
    private LatLng latLngPos;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean isDriverAvaiable() {
        return driverAvaiable;
    }

    public void setDriverAvaiable(boolean driverAvaiable) {
        this.driverAvaiable = driverAvaiable;
    }

    public LatLng getLatLngPos() {
        latLngPos = new LatLng(latitude,longitude);
        return latLngPos;
    }
}
