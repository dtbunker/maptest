package com.assis.redondo.daniel.maptest.utils;

import android.content.Intent;
import android.os.IBinder;

import com.assis.redondo.daniel.maptest.Config;
import com.assis.redondo.daniel.maptest.data.controller.DriverLocationController;
import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AlarmDispatchService extends WakefulIntentService {

	public AlarmDispatchService() {
		super("AlarmDispatchService");
	}

	public AlarmDispatchService(String name) {
		super(name);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	protected void doWakefulWork(Intent intent) {

		if(intent.getType().contentEquals(Config.RELOAD_LOCATIONS)) {
			DriverLocationController driverLocationController = DriverLocationController.getInstance(this);
			driverLocationController.getDriversLastLocationsLoader().reloadDriversLocations();
		}

	}

}


