package com.singhey.womenux;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.singhey.womenux.smsSenders.SendSms;
import com.singhey.womenux.sqlite.model.Story;

public class StoryReadActivity extends AppCompatActivity {

    private TextView heading;
    private TextView story;
    private TextView read_count;
    private TextView name;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //all views
        this.heading = findViewById(R.id.story_heading);
        this.story = findViewById(R.id.story);
        this.read_count = findViewById(R.id.read_count);
        this.name = findViewById(R.id.writer_name);
        this.image = findViewById(R.id.writer_image);


        //assigning data
        Story s = new Story();
        s.setHeading(getIntent().getStringExtra("heading"));
        s.setName(getIntent().getStringExtra("name"));
        s.setStory(getIntent().getStringExtra("story"));
        s.setImageUrl(getIntent().getStringExtra("image_url"));
        s.setId(getIntent().getIntExtra("id", -1));
        s.setReadCount(getIntent().getIntExtra("read_count", -1));

        Log.v("StoryActivity Adapter", s.toString());

        this.heading.setText(s.getHeading());
        this.name.setText(s.getName());
        this.read_count.setText("Read: "+s.getReadCount());
        this.story.setText((s.getStory()));

        Glide.with(this).load(s.getImageUrl())
                .into(this.image);


    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(StoryReadActivity.this);
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
