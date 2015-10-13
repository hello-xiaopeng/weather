package com.weather.app.receiver;

import com.weather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//八小时之后执行此方法
		Intent i = new Intent(context,AutoUpdateService.class);
		context.startService(i);
	}

}
