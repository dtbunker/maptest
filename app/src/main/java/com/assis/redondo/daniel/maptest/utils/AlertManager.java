package com.assis.redondo.daniel.maptest.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.assis.redondo.daniel.maptest.Config;
import com.assis.redondo.daniel.maptest.dispatcher.EventDispatcher;

import java.util.Calendar;

/**
 * 
 * @author DT
 *
 * API utilizada para disparar os alarmes dos eventos marcados
 */

public class AlertManager extends EventDispatcher {

	private static AlertManager instance;
	static final Object sLock = new Object();
	private Context mCtx;

	public static AlertManager getInstance(Context ctx) {
		synchronized (sLock) {
			if (instance == null) {
				instance = new AlertManager(ctx.getApplicationContext());
			}
			return instance;
		}
	}

	public AlertManager(Context ctx) {
		mCtx = ctx;
	}

    public void addReloadLocationsAlarm() {

		long fixedReloadTime = 5000;
		addAlarmIntent(Config.RELOAD_LOCATIONS, fixedReloadTime);

	}

	public void addAlarmIntent(String id, long firedate){

		Intent alarmIntent = new Intent(mCtx, AlarmDispatchService.class);
		alarmIntent.setType(id);

		PendingIntent pendingIntent = PendingIntent.getService(mCtx, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) mCtx.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, firedate, pendingIntent);

	}

	public void removeAlarmIntent(String id){

		Intent alarmIntent = new Intent(mCtx, AlarmDispatchService.class);
		alarmIntent.setType(id);

		PendingIntent pendingIntent = PendingIntent.getService(mCtx, 0, alarmIntent, 0);

		AlarmManager alarmManager = (AlarmManager) mCtx.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}

	public void removeReloadAlarm() {
		
		removeAlarmIntent(Config.RELOAD_LOCATIONS);

	}

}
