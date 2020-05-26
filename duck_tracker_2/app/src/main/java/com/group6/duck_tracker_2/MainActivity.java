package com.group6.duck_tracker_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Integer delay = 15000;

    Button submit_data_button;
    Button facts_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit_data_button = (Button) findViewById(R.id.submit_data_button);
        facts_button = (Button) findViewById(R.id.facts_button);


/*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    my_post_request("fakepass", "fakeid","fakedate",
                            "faketime","22.22222","11.11111");
                }
                catch (Exception e){
                    e.printStackTrace();

                }
                handler.postDelayed(this, delay); //the time is in milliseconds...

            }
        }, 5000);

*/

        submit_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                my_post_request("fakepass", "fakeid","fakedate",
                        "faketime","22.22222","11.11111");
            }
        });

        //////


        facts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test_get_location();

            }
        });

        ////




}



    public void my_post_request(final String password, final String id,
                                final String date, final String time, final String latitude,
                                final String longitude){
        String url = "https://ix.cs.uoregon.edu/~cwycoff/ix_dev_testing/test_2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast post_toast = Toast.makeText(MainActivity.this,
                                "SUCCESS:\n" + response.trim(), Toast.LENGTH_LONG);
                        post_toast.setGravity(Gravity.CENTER_VERTICAL,0,-350);
                        post_toast.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pass", password);
                params.put("ID", id);
                params.put("date", date);
                params.put("time", time);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    public void test_get_location() {
        location gps = new location(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Toast.makeText(MainActivity.this, Double.toString(latitude) + Double.toString(longitude), Toast.LENGTH_LONG).show();

    }
}
