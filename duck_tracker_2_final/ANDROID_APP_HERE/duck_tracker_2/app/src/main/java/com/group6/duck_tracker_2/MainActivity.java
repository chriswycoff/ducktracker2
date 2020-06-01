package com.group6.duck_tracker_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

// this is the overarching class
public class MainActivity extends AppCompatActivity {

    // BEGIN declaration of global variables //

    // change this to true if having trouble connecting or sending data
    // app will print messages to the screen indicating any error codes
    // from the mysql database
    Boolean DEBUG = false;

    Integer delay = 300000; // (milliseconds) // for 5 minutes change to : 300000 milliseconds
    // TODO: for testing a more frequent incrementing see lines 91 and 92

    Integer tal = 0; // time at location
    Button submit_data_button;

    EditText latitude_text;
    EditText longitude_text;
    EditText connection_text;

    String last_latitutde; // for time at location logic
    String last_longitude; // for time at location logic

    private final long MIN_TIME = 1000; // value for location manager
    private final long MIN_DIST = 5; // value for location manager


    private LocationListener locationLister;
    private LocationManager locationManager;

    String connection_off = "NOT CONNECTED";
    String connection_on = "CONNECTED OK";

    //TODO: Change url string to <"https://ix.cs.uoregon.edu/~username/duck_tracker_server/duck_track_gate.php">
    //TODO continued: specific thing to change is "username": please see installation instructions for setting up
    //TODO continued: the directory containing the 3 server files and how to execute the configuration script
    //TODO continued: SEE SECTION:
    // just in case this is a live server
    String url = "https://ix.cs.uoregon.edu/~<YOURUSERNAMEHERE>/duck_tracker_server/duck_track_gate.php"; // change this
    // just in case this string is a live server "https://ix.cs.uoregon.edu/~dbz/duck_tracker_server/duck_track_gate.php"
    // example -> //url = "https://ix.cs.uoregon.edu/~nhenders/duck_tracker_server/duck_track_gate.php";

    // END declaration of global variables //

    // function to get the Universal ID for a user
    // makes user anonymous, but identifiable for repeated use of application
    // UUID consistent between separate uses of the application
    public String getUUID() {

        // essentially this gets a unique user ID from the hardware of the users phone
        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Use UTF 8 for the id
        UUID id_UUID = UUID.nameUUIDFromBytes(id.getBytes(Charset.forName("UTF-8")));

        return(id_UUID.toString());
    }

    // onCreate gets called when the application is first opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // uncomment the following to test every 15 seconds !!!
        // delay = 15000;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit_data_button = findViewById(R.id.submit_data_button);

        latitude_text = findViewById(R.id.latitude_string);
        longitude_text = findViewById(R.id.longitude_string);
        connection_text = findViewById(R.id.connection_status);
        connection_text.setText(connection_off);

        last_latitutde = latitude_text.getText().toString();
        last_longitude = longitude_text.getText().toString();


        final String the_id = getUUID();

        ImageView connect_img = (ImageView) findViewById(R.id.connection_image);
        connect_img.setImageResource(R.drawable.read_dot);


        // get permission to access the phones geolocation services, both fine and course grain location
        ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PackageManager.PERMISSION_GRANTED);

        locationLister = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    // changes the lat/long text boxes
                    String lat = String.format(Locale.US,"%.3f",location.getLatitude());
                    String longt = String.format(Locale.US,"%.3f",location.getLongitude());
                    latitude_text.setText(lat);
                    longitude_text.setText(longt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // could add logic here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,...
            // ... int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }   // ask phone for location
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationLister);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationLister);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        // here is the logic that repeats calling get_location function every "delay" milliseconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {

                    get_location(the_id,false);
                }
                catch (Exception e){
                    e.printStackTrace();

                }
                handler.postDelayed(this, delay); //the time is in milliseconds...

            }
        }, 5000);



        submit_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_location(the_id,true);
            }
        });

        //////
}


    // used by line 300 and
    public void my_post_request(final String password, final String id,
                                final String date, final String time, final String latitude,
                                final String longitude, final Boolean from_button){


        if (!last_latitutde.equals(latitude_text.getText().toString())){
            tal = 0;
        }
        else if (!last_latitutde.equals(latitude_text.getText().toString())){
            tal = 0;
        }
        else if(from_button){
            // pass
        }
        else{
            tal += 5;
        }



        //TODO: Change url string to <"https://ix.cs.uoregon.edu/~username/duck_tracker_server/duck_track_gate.php">
        //String url;
        //url = "https://ix.cs.uoregon.edu/~cwycoff/ix_dev_testing/test_2.php"; // just in case
        // example -> //url = "https://ix.cs.uoregon.edu/~nhenders/duck_tracker_server/duck_track_gate.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (DEBUG) {
                            Toast post_toast = Toast.makeText(MainActivity.this,
                                    "SUCCESS:\n" + response.trim(), Toast.LENGTH_LONG);
                            post_toast.setGravity(Gravity.CENTER_VERTICAL, 0, -350);
                            post_toast.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (DEBUG){
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                         }

                        ImageView connect_img = (ImageView) findViewById(R.id.connection_image);


                        connect_img.setImageResource(R.drawable.read_dot);
                        connection_text.setText(connection_off);
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
                params.put("tal", tal.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        ImageView connect_img = (ImageView) findViewById(R.id.connection_image);
        connect_img.setImageResource(R.drawable.green_dot);
        connection_text.setText(connection_on);

        last_latitutde = latitude_text.getText().toString();
        last_longitude = longitude_text.getText().toString();

    }
    // used by lines 184 and 200
    public void get_location(String the_id, Boolean from_button) {

        // get time at request
        try {
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            String timeString = dateFormat1.format(currentTime);
            String dateString = dateFormat2.format(currentTime);
            // yyyy-MM-dd HH:mm:ss" to change to one date format

            // call post request see line 209
            my_post_request("fakepass", the_id, dateString,
                    timeString, latitude_text.getText().toString(), longitude_text.getText().toString(),from_button);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //Toast.makeText(MainActivity.this, "SUCCESS DATA SENT "+latitude_text.getText().toString() +" "+ longitude_text.getText().toString(), Toast.LENGTH_LONG).show();

    }
}
