package com.singhey.womenux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.singhey.womenux.smsSenders.SendSms;
import com.singhey.womenux.sqlite.helper.DatabaseHelper;
import com.singhey.womenux.sqlite.model.SettingsDataModel;

public class SettingsActivity extends AppCompatActivity {

    EditText name;
    EditText location;
    EditText email;
    EditText sosMessage;
    EditText editLocationMessage;
    EditText editBatteryMessage;

    Switch policeStation;
    Switch batteryDrain;
    Switch locationMessage;
    Switch powerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton finishActivity = findViewById(R.id.finish_activity);
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When submit is clicked
        Button submit = findViewById(R.id.save_settings);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });


        //get all views
        name =  findViewById(R.id.edit_user_name);
        location = findViewById(R.id.edit_user_location);
        email = findViewById(R.id.edit_user_email);
        editBatteryMessage = findViewById(R.id.edit_battery_message);
        editLocationMessage = findViewById(R.id.edit_location_message);
        sosMessage = findViewById(R.id.edit_sos_message);

        policeStation = findViewById(R.id.police_station);
        batteryDrain = findViewById(R.id.battery_down_alert);
        locationMessage = findViewById(R.id.location_switch);
        powerButton = findViewById(R.id.power_button);

        updateSettings();


    }

    private void updateSettings() {

        //update all views based on data present in data base
        DatabaseHelper db = new DatabaseHelper(SettingsActivity.this);
        SettingsDataModel settings = db.fetchSettings();
        if(settings == null){
            Log.v("Settings", "Settings is empty");
            return;
        }
        Log.v("Settings", settings.toString());

        name.setText(settings.getName());
        email.setText(settings.getEmail());
        location.setText(settings.getLocation());
        sosMessage.setText(settings.getSosMessage());
        editLocationMessage.setText(settings.getLocationMessage());
        editBatteryMessage.setText(settings.getBatteryDrainMessage());

        policeStation.setChecked(intToBool(settings.getPoliceStationCall()));
        batteryDrain.setChecked(intToBool(settings.getBatteryDrain()));
        locationMessage.setChecked(intToBool(settings.getSendLocation()));
        powerButton.setChecked(intToBool(settings.getPowerFeature()));

        TextView textUserName = findViewById(R.id.user_name);
        textUserName.setText(settings.getName());
        TextView textEmail = findViewById(R.id.user_mail);
        textEmail.setText(settings.getEmail());

        Log.v("Settings", settings.toString());
    }

    private void submitData() {


        SettingsDataModel settingsData = new SettingsDataModel();
        settingsData.setEmail(email.getText().toString());
        settingsData.setName(name.getText().toString());
        settingsData.setLocation(location.getText().toString());
        settingsData.setLocationMessage(editLocationMessage.getText().toString());
        settingsData.setBatteryDrainMessage(editBatteryMessage.getText().toString());
        settingsData.setSosMessage(sosMessage.getText().toString());

        settingsData.setSendLocation(boolToInt(locationMessage.isChecked()));
        settingsData.setBatteryDrain(boolToInt(batteryDrain.isChecked()));
        settingsData.setPoliceStationCall(boolToInt(policeStation.isChecked()));
        settingsData.setPowerFeature(boolToInt(powerButton.isChecked()));

        Log.v("Settings", settingsData.toString());

        DatabaseHelper db = new DatabaseHelper(SettingsActivity.this);
        db.updateSettings(settingsData);

        Toast.makeText(SettingsActivity.this, "Settings updated", Toast.LENGTH_SHORT).show();
        updateSettings();

    }

    private int boolToInt(boolean value) {
        if(value) {
            return 1;
        }
        return 0;
    }

    private boolean intToBool(int value) {
        if(value == 1) {
            return true;
        }
        return false;
    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(SettingsActivity.this);
                sms.sendMessage("", "", SOS);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pressCount = 0;
                }
            }).start();
        }
        return super.onKeyDown(keyCode, event);
    }
}