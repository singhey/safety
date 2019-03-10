package com.singhey.womenux.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.singhey.womenux.smsSenders.SendSms;

import static android.content.Intent.ACTION_BATTERY_LOW;

public class BatteryLevelReceiver extends BroadcastReceiver {
    private static String TAG = BatteryLevelReceiver.class.getSimpleName();
    private static String BATTERY_LOW = "battery_low";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Battery LOw intent");
        if(intent.getAction().equals(ACTION_BATTERY_LOW)) {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int percent = (level*100)/scale;

            Log.v(TAG, "Battery Percentage: "+percent+" level: "+level+" Scale: "+scale);

            SendSms smsSender = new SendSms(context);
            smsSender.sendMessage("", "", BATTERY_LOW);

        }
    }
}
