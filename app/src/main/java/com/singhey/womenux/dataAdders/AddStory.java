package com.singhey.womenux.dataAdders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singhey.womenux.R;
import com.singhey.womenux.SettingsActivity;
import com.singhey.womenux.smsSenders.SendSms;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AddStory extends AppCompatActivity {

    private TextView heading,
            story,
            writerName,
            imageUrl;

    private String TAG = AddStory.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        heading = findViewById(R.id.story_heading);
        story = findViewById(R.id.story_edit_text);
        writerName = findViewById(R.id.writer_name);
        imageUrl = findViewById(R.id.image_url);

        Button submitButton = findViewById(R.id.submit_story);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                String URL = "http://singhey.com/api/save_story.php";

                final ProgressDialog pdLoading = new ProgressDialog(view.getContext());
                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();

                Log.v("URL:", " "+URL);

                Log.v("Story Fragments", "Request to fetch data");

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.v("Story Fragment", "Response: "+response);
                        //parsing is completed now hide loader and display data
                        pdLoading.dismiss();

                        if(response.toLowerCase().equals("success")) {
                            Toast.makeText(view.getContext(), "Story added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(view.getContext(), "Error adding story", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Story Fragment", "Response: "+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  storyObject = new HashMap<>();
                        storyObject.put("heading", heading.getText().toString());
                        storyObject.put("story", story.getText().toString());
                        storyObject.put("writer_name", writerName.getText().toString());
                        storyObject.put("image_url", imageUrl.getText().toString());

                        return storyObject;
                    }
                };

                requestQueue.add(stringRequest);

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

    private String formatStringForRequest() {

        String urlParameters = "";

        String encodedHeader = null,
                encodedStory = null,
                encodedWriterName = null,
                encodedImageUrl = null;
        try {
            encodedHeader = URLEncoder.encode(heading.getText().toString(), "UTF-8");
            encodedStory = URLEncoder.encode(story.getText().toString(), "UTF-8");
            encodedImageUrl = URLEncoder.encode(imageUrl.getText().toString(), "UTF-8");
            encodedWriterName = URLEncoder.encode(writerName.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        urlParameters = "?heading="+encodedHeader+
                "&story="+encodedStory+
                "&writer_name"+encodedWriterName+
                "&image_url="+encodedImageUrl;

        return urlParameters;
    }

    public String makeJsonObject(){
        JSONObject storyObject = new JSONObject();
        try {

            storyObject.put("heading", heading.getText().toString());
            storyObject.put("story", story.getText().toString());
            storyObject.put("writer_name", writerName.getText().toString());
            storyObject.put("image_url", imageUrl.getText().toString());

        }catch (Exception e){
            Log.v(TAG, "Error forming json object");
        }

        return storyObject.toString();
    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(AddStory.this);
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
