package com.singhey.womenux.smsSenders;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.singhey.womenux.sqlite.helper.DatabaseHelper;
import com.singhey.womenux.sqlite.model.EmergencyContactModel;
import com.singhey.womenux.sqlite.model.SettingsDataModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by singh on 25-03-2018.
 */

public class SendSms {

    private final String SOS = "sos";
    private final String CAB_BOOKING = "cab_booking";
    private final String BATTERY_LOW = "battery_low";
    private final String MY_LOCATION = "my_location";

    private final String TAG = SendSms.class.getSimpleName();
    Context context;
    SmsManager sms = SmsManager.getDefault();
    DatabaseHelper db;
    SettingsDataModel settings;

    private String requestType;

    public SendSms(Context context) {

        this.context = context;

    }

    public void sendMessage(String receivedFrom, String message, String requestType) {

        String formattedMessage = "";
        this.requestType = requestType;
        db = new DatabaseHelper(context);
        settings = db.fetchSettings();

        switch(requestType) {
            case CAB_BOOKING:

                if(settings.getPoliceStationCall() != 1){
                    Toast.makeText(context, "Cab forwarding message is disabled", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean validationResult = validateMessage(message);
                if (!validationResult) {
                    Toast.makeText(context, "It wasn't a cab message", Toast.LENGTH_SHORT).show();
                    return;
                }
                formattedMessage = getFormattedCabBookingMessage(receivedFrom, message);
                forwardMessage(formattedMessage);
                Toast.makeText(context, "Cab booking message will be forwarded", Toast.LENGTH_SHORT).show();
                break;


            case SOS:

                getLocation();
                break;


            case BATTERY_LOW:
                if(settings.getBatteryDrain() != 1){
                    Toast.makeText(context, "Battery drain message sending is disabled", Toast.LENGTH_SHORT).show();
                }

                forwardMessage(settings.getBatteryDrainMessage());
                break;

            case MY_LOCATION:
                if(settings.getSendLocation() != 1){
                    Toast.makeText(context, "Location forwarding is disabled", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLocation();
                break;
        }
    }

    public void forwardMessage(String message){
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<EmergencyContactModel> emergencyContactModel = db.fetchEmergencyContact();
        if(emergencyContactModel == null) {
            return;
        }

        for(EmergencyContactModel m: emergencyContactModel) {

            sms.sendTextMessage("+91"+m.getNumber(), null, message, null, null);

        }
    }

    public void setLocation(String latitude, String longitude){

        String message = "";
        if(requestType == SOS) {
            message = settings.getSosMessage()+"\n My location is https://maps.google.com/maps?q=" + latitude + "," + longitude;
        }else if(requestType == MY_LOCATION){
            message = settings.getLocationMessage()+": https://maps.google.com/maps?q=" + latitude + "," + longitude;
        }else{
            return;
        }
        Toast.makeText(context, "Obtained location, message will be forwarded to emergency contacts", Toast.LENGTH_SHORT).show();
        forwardMessage(message);

    }

    private String getFormattedCabBookingMessage(String receivedFrom, String message) {
        String formattedMessage = "Cab booked from: "+receivedFrom +
                "\nConfirmation message: "+message;
        return formattedMessage;
    }

    private boolean validateMessage(String message) {

        String[] keywords = {"cab", "booking", "taxi", "confirmed", "confirmation", "booked", "successfully"};
        int count = 0;
        for(String keyword: keywords) {
            if(Arrays.asList(message.toLowerCase().split(" ")).contains(keyword)) {
                count+=1;
            }
        }
        if(count < 2){
            return false;
        }

        return true;
    }

    private void getLocation() {
        Toast.makeText(context, "Fetching your current location", Toast.LENGTH_SHORT).show();
        LocationProviderApp locationProvider = new LocationProviderApp(context, this);
        locationProvider.getLocation();
    }


}
