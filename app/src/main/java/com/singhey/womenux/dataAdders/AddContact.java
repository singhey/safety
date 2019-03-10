package com.singhey.womenux.dataAdders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.singhey.womenux.R;
import com.singhey.womenux.SettingsActivity;
import com.singhey.womenux.smsSenders.SendSms;
import com.singhey.womenux.sqlite.helper.DatabaseHelper;
import com.singhey.womenux.sqlite.model.EmergencyContactModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContact extends AppCompatActivity {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static String TAG = AddContact.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Button submit = findViewById(R.id.submit_contact_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addContact()){
                    Toast.makeText(v.getContext(), "Contact details added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        ImageButton finishActivity = findViewById(R.id.finish_activity);
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    protected boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private boolean addContact(){
        TextView name,
                email,
                phone,
                location;

        name = findViewById(R.id.contact_name);
        email = findViewById(R.id.contact_email);
        phone = findViewById(R.id.contact_phone);
        location = findViewById(R.id.contact_location);

        String str_email = email.getText().toString(),
                str_phone = phone.getText().toString(),
                str_location = location.getText().toString(),
                str_name = name.getText().toString();

        if(!validate(str_email)) {
            Toast.makeText(this, "Email address not valid", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!(str_phone.length() == 10)){
            Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        EmergencyContactModel e = new EmergencyContactModel();
        e.setNumber(str_phone);
        e.setLocation(str_location);
        e.setName(str_name);
        e.setEmail(str_email);

        Log.v(TAG, e.toString());

        DatabaseHelper db = new DatabaseHelper(this);
        db.addEmergencyContact(e);

        return true;
    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(AddContact.this);
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
