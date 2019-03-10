package com.singhey.womenux.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.util.Log;

import com.singhey.womenux.sqlite.model.EmergencyContactModel;
import com.singhey.womenux.sqlite.model.SettingsDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by singh on 25-03-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //log cat tag
    private final String TAG = "DatabaseHelper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "safety";

    //Database tables
    private static final String EMERGENCY_CONTACTS = "emergency_contacts";
    private static final String SETTINGS = "settings";

    //Common column name
    private static final String ID = "key_id";
    private static final String CREATED_AT = "created_at";

    //Columns for contacts table
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String LOCATION = "location";

    //Columns for settings table
    private static final String POLICE = "police";
    private static final String BATTERY_DOWN = "battery_down";
    private static final String SEND_LOCATION = "send_location";
    private static final String POWER_FEATURE = "power_feature";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_LOCATION = "user_location";
    private static final String SOS_MESSAGE = "sos_message";
    private static final String LOCATION_MESSAGE = "location_message";
    private static final String BATTERY_DRAIN_MESSAGE = "battery_drain_message";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String PHONE_NUMBERS_TABLE = "CREATE TABLE "+EMERGENCY_CONTACTS+" ( " +
                ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PHONE+" TEXT, "+
                EMAIL+" TEXT, "+
                NAME+" TEXT, "+
                LOCATION+" TEXT, "+
                CREATED_AT+" DATETIME );";

        final String SETTINGS_TABLE = "CREATE TABLE "+SETTINGS+" ( " +
                ID+" INTEGER, "+
                POLICE+" INTEGER, "+
                SEND_LOCATION+" INTEGER, "+
                BATTERY_DOWN+" INTEGER, "+
                POWER_FEATURE+" INTEGER, "+
                USER_NAME+" TEXT, "+
                USER_EMAIL+" TEXT, "+
                USER_LOCATION+" TEXT, "+
                SOS_MESSAGE+" TEXT, "+
                LOCATION_MESSAGE+" TEXT, "+
                BATTERY_DRAIN_MESSAGE+" TEXT, "+
                CREATED_AT+" DATETIME );";

        sqLiteDatabase.execSQL(PHONE_NUMBERS_TABLE);
        sqLiteDatabase.execSQL(SETTINGS_TABLE);

        Log.v("Database", "Table was created");
    }

    private void addDefaultDataInSettings() {

        SQLiteDatabase db = this.getWritableDatabase();

        //create default data
        SettingsDataModel settings = new SettingsDataModel();
        settings.setLocation("Bangalore, Karnataka");
        settings.setEmail("help@singhey.com");
        settings.setName("Jon Doe");
        settings.setSosMessage("Help! I'm in trouble");
        settings.setLocationMessage("My Location is");
        settings.setBatteryDrainMessage("My battery is below 15% if cell switches off don not panic.");

        settings.setSendLocation(1);
        settings.setPoliceStationCall(0);
        settings.setBatteryDrain(1);
        settings.setPowerFeature(0);

        Log.v(TAG, settings.toString());

        //use content values to insert data base JAVA is lame
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, settings.getEmail());
        values.put(USER_NAME, settings.getName());
        values.put(USER_LOCATION, settings.getLocation());
        values.put(LOCATION_MESSAGE, settings.getLocationMessage());
        values.put(BATTERY_DRAIN_MESSAGE, settings.getBatteryDrainMessage());
        values.put(SOS_MESSAGE, settings.getSosMessage());

        values.put(ID, "1");

        //boolean values
        values.put(SEND_LOCATION, settings.getSendLocation());
        values.put(POLICE, settings.getPoliceStationCall());
        values.put(POWER_FEATURE, settings.getPowerFeature());
        values.put(BATTERY_DOWN, settings.getBatteryDrain());
        values.put(CREATED_AT, getDateTime());

        db.insert(SETTINGS, null, values);
        Log.v(TAG, "Default settings was added");

        //close database connection
        closeDb(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addEmergencyContact(EmergencyContactModel emergencyContactModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        Log.v(TAG, emergencyContactModel.toString());

        //use content values to insert data base JAVA is lame
        ContentValues values = new ContentValues();
        values.put(PHONE, emergencyContactModel.getNumber());
        values.put(EMAIL, emergencyContactModel.getEmail());
        values.put(NAME, emergencyContactModel.getName());
        values.put(LOCATION, emergencyContactModel.getLocation());
        values.put(CREATED_AT, getDateTime());

        db.insert(EMERGENCY_CONTACTS, null, values);
        Log.v(TAG, "Emergency contact was added");

        //close database connection
        closeDb(db);

        return true;
    }

    public void deleteEmergencyContacts(EmergencyContactModel emergencyContactModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EMERGENCY_CONTACTS, ID+" = ? ", new String[] { String.valueOf(emergencyContactModel.getId()) });

        closeDb(db);
    }

    public ArrayList<EmergencyContactModel> fetchEmergencyContact() {

        ArrayList<EmergencyContactModel> result = new ArrayList<>();

        //establish database connection
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM "+EMERGENCY_CONTACTS;

        Cursor c = db.rawQuery(query, null);
        if(c == null || c.getCount() <= 0) {
            return null;
        }

        c.moveToFirst(); // start at begining of result;

        //loop through each item and put them in an array list

        do {

            EmergencyContactModel e = new EmergencyContactModel();

            //pass attendance object;
            e.setId(c.getInt(c.getColumnIndex(ID)));
            e.setName(c.getString(c.getColumnIndex(NAME)));
            e.setNumber(c.getString(c.getColumnIndex(PHONE)));
            e.setEmail(c.getString(c.getColumnIndex(EMAIL)));
            e.setLocation(c.getString(c.getColumnIndex(LOCATION)));
            result.add(e);

        }while(c.moveToNext());

        c.close();
        closeDb(db);
        return  result;

    }

    public SettingsDataModel fetchSettings() {

        SettingsDataModel settings = new SettingsDataModel();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM "+SETTINGS;
        Cursor c = db.rawQuery(query, null);
        if(c == null || c.getCount() <= 0) {
            addDefaultDataInSettings();

            return fetchSettings();
        }

        c.moveToFirst();

        //all booleans
        settings.setPowerFeature(c.getInt(c.getColumnIndex(POWER_FEATURE)));
        settings.setSendLocation(c.getInt(c.getColumnIndex(SEND_LOCATION)));
        settings.setBatteryDrain(c.getInt(c.getColumnIndex(BATTERY_DOWN)));
        settings.setPoliceStationCall(c.getInt(c.getColumnIndex(POLICE)));

        //all strings
        settings.setName(c.getString(c.getColumnIndex(USER_NAME)));
        settings.setEmail(c.getString(c.getColumnIndex(USER_EMAIL)));
        settings.setLocation(c.getString(c.getColumnIndex(USER_LOCATION)));
        settings.setLocationMessage(c.getString(c.getColumnIndex(LOCATION_MESSAGE)));
        settings.setBatteryDrainMessage(c.getString(c.getColumnIndex(BATTERY_DRAIN_MESSAGE)));
        settings.setSosMessage(c.getString(c.getColumnIndex(SOS_MESSAGE)));

        c.close();
        closeDb(db);

        return settings;
    }

    public void closeDb(SQLiteDatabase db) {
        if(db!= null && db.isOpen()){
            db.close();
        }
    }

    public void updateSettings(SettingsDataModel settings){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.v(TAG, settings.toString());

        //use content values to insert data base JAVA is lame
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, settings.getEmail());
        values.put(USER_NAME, settings.getName());
        values.put(USER_LOCATION, settings.getLocation());
        values.put(LOCATION_MESSAGE, settings.getLocationMessage());
        values.put(BATTERY_DRAIN_MESSAGE, settings.getBatteryDrainMessage());
        values.put(SOS_MESSAGE, settings.getSosMessage());

        values.put(ID, "1");

        //boolean values
        values.put(SEND_LOCATION, settings.getSendLocation());
        values.put(POLICE, settings.getPoliceStationCall());
        values.put(POWER_FEATURE, settings.getPowerFeature());
        values.put(BATTERY_DOWN, settings.getBatteryDrain());
        values.put(CREATED_AT, getDateTime());

        db.update(SETTINGS, values, ID+ "= ?", new String[] {String.valueOf(1)});

        closeDb(db);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



}
