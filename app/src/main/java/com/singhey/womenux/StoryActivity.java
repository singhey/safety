package com.singhey.womenux;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singhey.womenux.adapters.StoryFragmentAdapter;
import com.singhey.womenux.dataAdders.AddStory;
import com.singhey.womenux.smsSenders.SendSms;
import com.singhey.womenux.sqlite.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class StoryActivity extends AppCompatActivity {

    private String requestUrl = "http://www.singhey.com/api/story.php";
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<Story> stories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), AddStory.class);

                startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.story_recycler_view);

        try {
            fetchData(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageButton finishActivity = findViewById(R.id.finish_activity);
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fetchData(Context context) throws JSONException {
        //loading to ensure that nothing is shown while loading
        final ProgressDialog pdLoading = new ProgressDialog(context);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        //preparing for request
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://www.singhey.com/api/story.php?page_no=0";

        JSONObject obj = new JSONObject();
        obj.put("page_no", "0");
        obj.put("something", "12");

        final String requestBody = obj.toString();
        Log.v("Story Fragment", "Request: "+requestBody);
        Log.v("Story Fragments", "Request to fetch data");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.v("Story Fragment", "Response: "+response);
                parseAndAddAdapter(response);
                //parsing is completed now hide loader and display data
                pdLoading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Story Fragment", "Response: "+error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);

    }

    private void parseAndAddAdapter(String result) {

        try {

            JSONArray jArray = new JSONArray(result);

            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Story s = new Story();
                s.setHeading(json_data.getString("heading"));
                s.setId(json_data.getInt("story_id"));
                s.setImageUrl(json_data.getString("img_url"));
                s.setName(json_data.getString("writer_name"));
                s.setReadCount(json_data.getInt("read_count"));
                s.setStory(json_data.getString("story"));
                stories.add(s);
            }

        } catch (JSONException e) {
            Toast.makeText(StoryActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(StoryActivity.this));
        StoryFragmentAdapter adapter = new StoryFragmentAdapter(StoryActivity.this, recyclerView, this);

        adapter.setData(stories);


        recyclerView.setAdapter(adapter);

    }

    private int pressCount = 0;
    private String SOS = "sos";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.v("MainActivity", "Volume up was pressed and count is: "+pressCount++);
            if(pressCount ==3) {
                Log.v("MainActivity", "Request to send message");
                SendSms sms = new SendSms(StoryActivity.this);
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
