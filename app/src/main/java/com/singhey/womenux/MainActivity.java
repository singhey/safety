package com.singhey.womenux;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.singhey.womenux.smsSenders.SendSms;

public class MainActivity extends AbsRuntimePermission
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PERMISSION = 10;
    private final String SOS = "sos";
    private final String MY_LOCATION = "my_location";
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //ask permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions not granted app may not work properly", Toast.LENGTH_SHORT).show();
        }

        requestAppPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                R.string.msg,
                REQUEST_PERMISSION);

        //attach listener for sos
        TextView sosButton = findViewById(R.id.sos_button);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Request for SOS message", Toast.LENGTH_SHORT).show();
                SendSms smsSender = new SendSms(view.getContext());
                smsSender.sendMessage("", "", SOS);
            }
        });

        TextView sendLocation = findViewById(R.id.current_location);
        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSms smsSender = new SendSms(v.getContext());
                smsSender.sendMessage("", "", MY_LOCATION);
            }
        });


        TextView readStories = findViewById(R.id.read_stories);
        readStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StoryActivity.class);
                startActivity(i);
            }
        });

        TextView selfDefense = findViewById(R.id.self_defense);
        selfDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SkillsActivity.class);
                startActivity(i);
            }
        });

        //change side nav focus to home icon
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    protected void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_home:
                Toast.makeText(MainActivity.this, "Already on home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_skills:
                Intent i = new Intent(MainActivity.this, SkillsActivity.class);
                startActivity(i);
                break;

            case R.id.nav_stories:
                Intent stories = new Intent(MainActivity.this, StoryActivity.class);
                startActivity(stories);
                break;

            case R.id.nav_emergency_contact:
                Intent addEmergencyContact = new Intent(MainActivity.this, ViewEmergencyContactActivity.class);
                startActivity(addEmergencyContact);
                break;

            case R.id.nav_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private int pressCount = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(MainActivity.this);
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
