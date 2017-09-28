package com.example.weatheralarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class BroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(this.getClass().getName(), "Broadcast Received");
		Intent serviceIntent = new Intent(context, UpdateAlarm.class);
		//context.startService(intent);
		
		startWakefulService(context, serviceIntent);
		
	}

}
