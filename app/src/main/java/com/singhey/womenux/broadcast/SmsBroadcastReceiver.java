package com.singhey.womenux.broadcast;

/**
 * Created by singh on 25-03-2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.singhey.womenux.smsSenders.SendSms;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private final String CAB_BOOKING = "cab_booking";

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        Log.v("SMS Broadcast", "Broadcast was received");
        try {

            if (bundle == null) {
                return;
            }
            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            Log.v("Broadcast receiver", ""+pdusObj.length);

            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
            String phoneNumber = currentMessage.getDisplayOriginatingAddress();

            String senderNum = phoneNumber;
            String message = currentMessage.getDisplayMessageBody();

            Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


            //once all data of sms is received next step is to forward it
            SendSms smsSender = new SendSms(context);
            smsSender.sendMessage(senderNum, message, CAB_BOOKING );


        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
