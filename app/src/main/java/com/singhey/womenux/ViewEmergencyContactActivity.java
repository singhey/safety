package com.singhey.womenux;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.singhey.womenux.adapters.ContactsViewAdapter;
import com.singhey.womenux.dataAdders.AddContact;
import com.singhey.womenux.smsSenders.SendSms;
import com.singhey.womenux.sqlite.helper.DatabaseHelper;
import com.singhey.womenux.sqlite.model.EmergencyContactModel;

import java.util.ArrayList;

public class ViewEmergencyContactActivity extends AppCompatActivity {

    private ContactsViewAdapter adapter;
    private String TAG = ViewEmergencyContactActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergency_contact);

        FloatingActionButton button = findViewById(R.id.add_contact_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContact = new Intent(ViewEmergencyContactActivity.this, AddContact.class);
                startActivity(addContact);
            }
        });

        RecyclerView rv = findViewById(R.id.contact_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactsViewAdapter(this, rv);

        rv.setAdapter(adapter);

        ImageButton finishActivity = findViewById(R.id.finish_activity);
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.fetchAndSetData();
    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(ViewEmergencyContactActivity.this);
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
