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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;

import com.example.future_parking.LocationService;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.example.future_parking.classes.GeoLocation;
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
import com.google.maps.android.SphericalUtil;

import android.util.Log;

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

    private void saveCurrentLoc(LatLng location) {
        lastLat = location.latitude;
        lastLng = location.longitude;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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

        }else if (map_MAP_sort.getText().equals("Free Parks")){

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
        LatLng roshain = new LatLng(32.0834782, 34.9818944);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(roshain)
                .title("parking"));
        markerList.add(myMarker);

        LatLng ashdod = new LatLng(31.801447, 34.643497);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(ashdod)
                .title("parking"));
        markerList.add(myMarker);

        LatLng telaviv = new LatLng(32.109333, 34.855499);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(telaviv)
                .title("parking"));
        markerList.add(myMarker);

        LatLng ashkelon = new LatLng(31.66926, 34.57149);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(ashkelon)
                .title("parking"));
        markerList.add(myMarker);
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
        LatLng latLng = marker.getPosition();
        double dist = distanceBykm( marker.getPosition().latitude, marker.getPosition().longitude,location.latitude,location.longitude);
//        dist = Math.round((dist*100)/100);
        BigDecimal bd = BigDecimal.valueOf(dist);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        map_LBL_distance.setText(bd.doubleValue() + " km");
        return false;
    }
}