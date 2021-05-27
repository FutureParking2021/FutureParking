package com.example.future_parking.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.ims.ImsMmTelManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.TransitDetail;
import com.akexorcist.googledirection.util.DirectionConverter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.future_parking.LocationService;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.example.future_parking.classes.CustomJsonRequest;
import com.example.future_parking.classes.CustomJsonRequest;
import com.example.future_parking.classes.GeoLocation;
import com.example.future_parking.classes.Operations;
import com.example.future_parking.classes.Parking;
import com.example.future_parking.classes.ParkingId;
import com.example.future_parking.uttils.MyLoc;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.android.material.math.MathUtils;
import com.google.gson.Gson;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.SphericalUtil;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //
    //////////////////////////variables////////////////
    private final double M_PI = 3.14159265358979323846;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 100;
    private final int LOCATION_PERMISSIONS_REQUEST_CODE = 23;
    private String serverKey = "AIzaSyDex92P6vID6eYFpOj2LPDxwI236yiuSrQ"; // Api Key For Google Direction API \\
    private GoogleMap mMap;
    private MarkerOptions place_for_serach;
    private FloatingActionButton map_BTN_gps, map_BTN_state_elite,
            map_BTN_start_timer,map_BTN_pause;
    private TextView map_MAP_sort;
    private Button map_MAP_show;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker myMarker;
    //    private EditText map_EDT_place_autocomplete;
    private TextView map_LBL_distance, map_LBL_time,map_LBL_timer;
    private LocalBroadcastManager localBroadcastManager;
    private ImageView map_IMG_zoomIn, map_IMG_zoomOut;
    public static final String BROADCAST_NEW_LOCATION_DETECTED = "com.example.future_parking.NEW_LOCATION_DETECTED";
    private LatLng location, destination;
    private String sourceLocation;
    private double avg_Speed = 0.0;
    private Account account;
    private boolean start_timer = false;
    private boolean pause_timer = false;
    private float kilometer = 0;
    private double distanceByKm = 0;
    //-----------DATE//////////////////
    private String dateFormat = "";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String distance;
    private String duration;
    private int counter=0;
    private int count_speed =0;
    private String sortBy;
    private String sortChoose;
    private double lastLat;
    private double lastLng;
    private TextView map_BTN_start_lat, map_BTN_start_lng;
    private ArrayList<Marker> markerList = new ArrayList<>();
    private String email, role;
    //////////////////////////variables////////////////

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_NEW_LOCATION_DETECTED)) {
                String json = intent.getStringExtra("EXTRA_LOCATION");
                try {
                    MyLoc lastLocation = new Gson().fromJson(json, MyLoc.class);

                    Log.d("plocation", "lat " +lastLocation.getLatitude() + " long " + lastLocation.getLongitude());
                    location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    saveCurrentLoc(location);
                    sourceLocation = getCompleteAddressString(lastLocation.getLatitude(), lastLocation.getLongitude());
                } catch (Exception ex) {
                    Log.d("johny", "onReceive: " + ex.toString());
                }
            }
        }
    };
    private Object JSONArray;

    private void saveCurrentLoc(LatLng location) {
        lastLat = location.latitude;
        lastLng = location.longitude;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        findViews();
        askLocationPermissions();
        init();
        addPIcWithGlide();
        MapFragment mapFragment  = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_MAP_google_map);
        mapFragment.getMapAsync(this);
        destination = new LatLng(32.166313, 34.843311);
//        checkUserChoise();
        startService();
    }

    private String getDateFormat()
    {
        String date;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = simpleDateFormat.format(calendar.getTime());
        Log.d("johny", "getDateFormat: date = " + date);
        return date;
    }


    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Log.d("johny", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private void init() {
        map_BTN_gps.setOnClickListener(myViewLister);
        map_BTN_state_elite.setOnClickListener(myViewLister);
        Places.initialize(getApplicationContext(), serverKey);
        map_IMG_zoomIn.setOnClickListener(myViewLister);
        map_IMG_zoomOut.setOnClickListener(myViewLister);
        map_MAP_sort.setOnClickListener(myViewLister);
        map_MAP_show.setOnClickListener(myViewLister);
    }

    private View.OnClickListener myViewLister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if (view.getTag().toString().equals("elite")) {
            changeMapType();
        } else if (view.getTag().toString().equals("gps")) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        } else if (view.getTag().toString().equals("zoom_in")) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        } else if (view.getTag().toString().equals("zoom_out")) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        } else if (view.getTag().toString().equals("sort")){
            String[] units = {"Distance", "Price", "free parks"};
            unitSelection(units);
            Log.d("stas", "sort by " + sortBy);
        }else if (view.getTag().toString().equals("show")){
            filterMarkers();


        }

    }

    private void filterMarkers() {
        if(map_MAP_sort.getText().equals("Distance")){
            for(int i = 0; i < markerList.size(); i++){
                LatLng pos = markerList.get(i).getPosition();
                if(distanceBykm(pos.latitude, pos.longitude, location.latitude,location.longitude) > 10){
                    markerList.get(i).setVisible(false);
                }else{
                    markerList.get(i).setVisible(true);
                }
            }
        }else if (map_MAP_sort.getText().equals("Price")){
            for(int i = 0; i < markerList.size(); i++) {
                if (Integer.parseInt(markerList.get(i).getTitle()) > 25) {
                    markerList.get(i).setVisible(false);
                } else {
                    markerList.get(i).setVisible(true);
                }
            }
        }else if (map_MAP_sort.getText().equals("Free Parks")){
            for(int i = 0; i < markerList.size(); i++) {
//                if (markerList. > 10) {
//                    markerList.get(i).setVisible(false);
//
//                } else {
//                    markerList.get(i).setVisible(true);
//                }
            }
        }
    }


    private void unitSelection(String[] choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ans = choose[which];
                map_MAP_sort.setText(ans);
            }
        });
        builder.show();
    }

    private void addPIcWithGlide() {
        Glide
                .with(MapActivity.this)
                .load(R.drawable.zoomin)
                .centerCrop()
                .into(map_IMG_zoomIn);

        Glide
                .with(MapActivity.this)
                .load(R.drawable.zoomout)
                .centerCrop()
                .into(map_IMG_zoomOut);



    }
    private double distanceBykm(double destLat, double destLng, double sourceLat, double sourceLng) {
        double theta = abs(destLng - sourceLng);
        double dist = Math.sin(deg2rad(destLat))
                * Math.sin(deg2rad(sourceLat))
                + Math.cos(deg2rad(destLat))
                * Math.cos(deg2rad(sourceLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist) * 1.609344; // to km
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        int counter = 0;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runs = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                counter++;
            }
        }


        if (counter > 0)
            return true;
        return false;
    }

    private void findViews() {
        map_BTN_gps = findViewById(R.id.map_BTN_gps);
        map_LBL_distance = findViewById(R.id.map_LBL_distance);
        map_BTN_state_elite = findViewById(R.id.map_BTN_state_elite);
        map_IMG_zoomIn = findViewById(R.id.map_IMG_zoomIn);
        map_IMG_zoomOut = findViewById(R.id.map_IMG_zoomOut);
        map_MAP_sort = findViewById(R.id.map_MAP_sort);
        map_MAP_show = findViewById(R.id.map_MAP_show);
        map_BTN_start_lat = findViewById(R.id.map_BTN_start_lat);
        map_BTN_start_lng = findViewById(R.id.map_BTN_start_lng);
    }

    private void changeMapType() {
        if (mMap != null) {
            int MapType = mMap.getMapType();
            if (MapType == 1) {
                map_BTN_state_elite.setImageResource(R.drawable.ic_satellite_off);
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                map_BTN_state_elite.setImageResource(R.drawable.ic_satellite_on);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(BROADCAST_NEW_LOCATION_DETECTED);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actionToService(LocationService.STOP_FOREGROUND_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        Log.d("gttt","before getting locations");
        getAllParking();
        mMap.setOnMarkerClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());


    }



    private void startService() {
        Log.d("stas", "start Service ");
        actionToService(LocationService.START_FOREGROUND_SERVICE);
//        validateButtons();
    }

    private void pauseService() {
        Log.d("stas", "pause Service");
        map_BTN_pause.setClickable(false);
        actionToService(LocationService.PAUSE_FOREGROUND_SERVICE);
        pause_timer=true;
//        validateButtons();
    }

    private void stopService() {
        Log.d("stas", "stop Service");
        actionToService(LocationService.STOP_FOREGROUND_SERVICE);
//        validateButtons();
        pause_timer=true;
    }


    private void actionToService(String action) {
        Intent startIntent = new Intent(this, LocationService.class);
        Log.d("StartSer", "String = " + action);
        startIntent.setAction(action);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("StartSer", "true = " + action);

            startForegroundService(startIntent);

        } else {
            Log.d("StartSer", "else = " + action);

            startService(startIntent);
        }
    }

    // // // // // // // // // // // // // // // // Permissions  // // // // // // // // // // // // // // //

    private void askLocationPermissions() {
        ActivityCompat.requestPermissions(MapActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        , Manifest.permission.FOREGROUND_SERVICE
                },
                LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MapActivity.this, "Result code = " + grantResults[0], Toast.LENGTH_SHORT).show();

                    // permission was granted,
                } else {

                    // permission denied
                    Toast.makeText(MapActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,"marker clicked",Toast.LENGTH_SHORT).show();
        marker.getTag();
        double dist = distanceBykm(marker.getPosition().latitude, marker.getPosition().longitude,location.latitude,location.longitude);
        BigDecimal bd = BigDecimal.valueOf(dist);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        map_LBL_distance.setText(bd.doubleValue() + " km");
        showAlertDialog(marker.getTag().toString());
        return false;
    }

    private void showAlertDialog(String lotId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MapActivity.this);
        builder1.setMessage("Are you sure you want to enter to parking?");
        builder1.setCancelable(true);
        Log.d("qttt","role is " + role);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(role.equals("PLAYER")){
                            Log.d("qttt","role is player");
//                            String spotId;
                            searchParkingSpace(lotId);
//                            enterParking(spotId);
                        }else{
                            Toast.makeText(MapActivity.this,"Only PLAYER can enter paking",Toast.LENGTH_SHORT).show();
                            Log.d("qttt","role is not PLAYER " + role);

                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void searchParkingSpace(String lotId){
        String url = "http://192.168.1.211:8010/twins/operations?page=0&size=1";
        JSONObject js = new JSONObject();
        JSONObject jsItem = new JSONObject();
        JSONObject jsItemId = new JSONObject();
        JSONObject jsInvokedBy = new JSONObject();
        JSONObject jsUserId = new JSONObject();
        JSONObject jsOperationAtt = new JSONObject();
//        Log.d("jttt","id " + lotId);
        try {
            js.put("type","searchParking");
            jsItemId.put("space","2021b.stanislav.krot");
            jsItemId.put("id",lotId);
            jsItem.put("itemId",jsItemId);
            js.put("item",jsItem);

            jsUserId.put("space","2021b.stanislav.krot");
            jsUserId.put("email",email);
            jsInvokedBy.put("userId",jsUserId);
            js.put("invokedBy",jsInvokedBy);

            js.put("operationAttributes",jsOperationAtt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, js, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("jttt", response.toString() + " i am queen");
                Log.d("jttt", "entering to park up! ");
                Toast.makeText(MapActivity.this,"Parking lot is full!",Toast.LENGTH_SHORT).show();

                if(response.length() == 0){
                    Log.d("jttt", "dont have any parks! ");
                    Toast.makeText(MapActivity.this,"Parking lot is full!",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Log.d("jttt", "entering to park! ");

                        JSONObject objectJs = response.getJSONObject(0);
                        JSONObject itemJs = objectJs.getJSONObject("itemId");
                        String parkingSpotId = itemJs.getString("id");
                        Log.d("jttt", "entering to park! " + parkingSpotId);
                        enterParking(parkingSpotId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas12", "Error: " + error.getMessage());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("stasptt", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Log.d("stas10","getting params");
                Map<String,String> params = new HashMap<String,String>();

                Log.d("stas10","returned params");
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                params.put("Content-Type","application/json; charset=utf-8");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(customJsonRequest);
    }

    private void enterParking(String parkingSpotId) {
        Log.d("qttt","enter to park");
        String url = "http://192.168.1.211:8010/twins/operations?page=0&size=1";
        JSONObject js = new JSONObject();
        JSONObject jsItem = new JSONObject();
        JSONObject jsItemId = new JSONObject();
        JSONObject jsInvokedBy = new JSONObject();
        JSONObject jsUserId = new JSONObject();
        JSONObject jsOperationAtt = new JSONObject();

        try {
            js.put("type","enterParking");
            jsItemId.put("space","2021b.stanislav.krot");
            jsItemId.put("id",parkingSpotId);
            jsItem.put("itemId",jsItemId);
            js.put("item",jsItem);

            jsUserId.put("space","2021b.stanislav.krot");
            jsUserId.put("email",email);
            jsInvokedBy.put("userId",jsUserId);
            js.put("invokedBy",jsInvokedBy);

            js.put("operationAttributes",jsOperationAtt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("jttt", response.toString() + " i am queen");
                        Toast.makeText(MapActivity.this,"Entered to park successfully!",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas12", "Error: " + error.getMessage());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("stasptt", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Log.d("stas10","getting params");
                Map<String,String> params = new HashMap<String,String>();

                Log.d("stas10","returned params");
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                params.put("Content-Type","application/json; charset=utf-8");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjReq);
    }


    private void getAllParking() {

        Log.d("gttt", "get parks");
        String url = "http://192.168.1.211:8010/twins/operations";
        JSONObject js = new JSONObject();
        JSONObject jsItem = new JSONObject();
        JSONObject jsItemId = new JSONObject();
        JSONObject jsInvokedBy = new JSONObject();
        JSONObject jsUserId = new JSONObject();
        JSONObject jsOperationAtt = new JSONObject();

        try {
            js.put("type","getAllParkingLots");
            jsItemId.put("space","2021b.stanislav.krot");
            jsItemId.put("id","");
            jsItem.put("itemId",jsItemId);
            js.put("item",jsItem);

            jsUserId.put("space","2021b.stanislav.krot");
            jsUserId.put("email",email);
            jsInvokedBy.put("userId",jsUserId);
            js.put("invokedBy",jsInvokedBy);

            js.put("operationAttributes",jsOperationAtt);

            CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, js, new Response.Listener<org.json.JSONArray>() {
                @Override
                public void onResponse(org.json.JSONArray response) {
                    Log.d("jttt","length " + response.length());
                    for(int i=0;i<response.length();i++){
                        try {
                            JSONObject js = response.getJSONObject(i);
                            JSONObject itemJs = js.getJSONObject("itemId");
                            String itemId = itemJs.getString("id");
                            JSONObject itemLocation = js.getJSONObject("location");
                            double lat = itemLocation.getDouble("lat");
                            double lng = itemLocation.getDouble("lng");
//                            JSONObject itemAtt = js.getJSONObject("operationAttributes");
//                            int price = itemAtt.getInt("priceOfParking");
                            Log.d("httt","parameters " + itemId + " lat " + lat + " lng " + lng);
                            myMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat,lng))
                                    .title("parking"));
                            myMarker.setTag(itemId);
//                            myMarker.setTitle(price+"");
                            markerList.add(myMarker);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("stas10", "response error " + error.getMessage());
                    try {
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("stasptt", new String(htmlBodyBytes), error);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Log.d("stas10","getting params");
                    Map<String,String> params = new HashMap<String,String>();

                    Log.d("stas10","returned params");
                    return params;
                }
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    params.put("Content-Type","application/json; charset=utf-8");
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(customJsonRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}