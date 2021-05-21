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
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.example.future_parking.LocationService;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.example.future_parking.uttils.MyLoc;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.gson.Gson;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.SphericalUtil;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //
    //////////////////////////variables////////////////
    private final double M_PI = 3.14159265358979323846;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 100;
    private final int LOCATION_PERMISSIONS_REQUEST_CODE = 125;
    private String serverKey = "AIzaSyBtpCR42KuS-R10e52uqAILezRZ7ixHQZA"; // Api Key For Google Direction API \\
    private GoogleMap mMap;
    private MarkerOptions place_for_serach;
    private FloatingActionButton map_BTN_directions, map_BTN_gps, map_BTN_start, map_BTN_stop, map_BTN_state_elite,
            map_BTN_start_timer,map_BTN_pause;
    private TextView map_MAP_sort;
    private Button map_MAP_show;
    private AutocompleteSupportFragment autocompleteFragment;
    private EditText map_EDT_place_autocomplete;
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
    //////////////////////////variables////////////////

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("plocation", "before getting location" + intent.getAction());

            if (intent.getAction().equals(BROADCAST_NEW_LOCATION_DETECTED)) {
                String json = intent.getStringExtra("EXTRA_LOCATION");
                try {
                    MyLoc lastLocation = new Gson().fromJson(json, MyLoc.class);
                    Log.d("plocation", "lat " +lastLocation.getLatitude() + " long " + lastLocation.getLongitude() );
                    location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    sourceLocation = getCompleteAddressString(lastLocation.getLatitude(), lastLocation.getLongitude());
                    avg_Speed+=lastLocation.getSpeed();
                    count_speed++;

                } catch (Exception ex) {
                    Log.d("johny", "onReceive: " + ex.toString());
                }
            }
        }
    };

//    private void checkUserChoise() {
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                    // Return to UI Thread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            map_LBL_timer.setText("timer: " + counter);
//                        }
//                    });
//            }
//        }, 0, 1000);
//    }

    private void createCustomFinishTour() {
        Toast.makeText(getApplicationContext(),"Finish The Tour",Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViews();
        askLocationPermissions();
        init();
        map_BTN_start_timer.setVisibility(View.GONE);
        map_BTN_stop.setClickable(false);
        addPIcWithGlide();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_MAP_google_map);
        mapFragment.getMapAsync(this);
//        checkUserChoise();
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
        map_BTN_directions.setOnClickListener(myViewLister);
        map_BTN_gps.setOnClickListener(myViewLister);
        map_BTN_start.setOnClickListener(myViewLister);
        map_BTN_stop.setOnClickListener(myViewLister);
        map_BTN_pause.setOnClickListener(myViewLister);
        map_BTN_state_elite.setOnClickListener(myViewLister);
        Places.initialize(getApplicationContext(), serverKey);
        map_EDT_place_autocomplete.setOnClickListener(myViewLister);
        map_IMG_zoomIn.setOnClickListener(myViewLister);
        map_IMG_zoomOut.setOnClickListener(myViewLister);

    }

    private View.OnClickListener myViewLister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if (view.getTag().toString().equals("start")) {
            startService();
        } else if (view.getTag().toString().equals("stop")) {
            stopService();
//            validateButtons();
            start_timer=false;
        } else if (view.getTag().toString().equals("pause")) {
            pauseService();
        } else if (view.getTag().toString().equals("direction")) {
            if(destination!=null)
            {
                getDestinationInfo(destination);
                map_BTN_start_timer.setVisibility(View.VISIBLE);
                start_timer=true;
            }


        } else if (view.getTag().toString().equals("elite")) {
            changeMapType();

        } else if (view.getTag().toString().equals("gps")) {
//            validateButtons();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        } else if (view.getTag().toString().equals("search")) {
            searchPlace();

        } else if (view.getTag().toString().equals("zoom_in")) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());

        } else if (view.getTag().toString().equals("zoom_out")) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());

        } else if (view.getTag().toString().equals("sort")){
            String[] units = {"Distance", "Price","free parks"};
            unitSelection(units);
            Log.d("stas", "sort by " + sortBy);
        }else if (view.getTag().toString().equals("show")){

        }

    }

    private void unitSelection(String[] choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sortBy = choose[which];

                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                builder.setTitle("Sort By " + sortBy);

// Set up the input
                final EditText input = new EditText(MapActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sortChoose = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                if(sortBy.equals("Distance")){
                    //TODO sort by distance
                }else if (sortBy.equals("Price")){
                    //TODO sort by price

                }
            }
        });
        builder.show();
    }

    private static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }
    private double distanceBetweenTwoPoints(LatLng location, LatLng destination) {

        return distanceBykm(location.latitude,location.longitude,destination.latitude,destination.longitude);
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
    private double distanceBykm(double lat1, double lon1, double lat2, double lon2) {
        double theta = abs(lon1 - lon2);
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
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

    private void searchPlace() {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(MapActivity.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                addDestPlaceInMap(data);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("johny", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addDestPlaceInMap(Intent data) {
        mMap.clear();
        Place place = Autocomplete.getPlaceFromIntent(data);
        destination = place.getLatLng();
//        String dest = getCompleteAddressString(destination.latitude, destination.longitude);
        map_EDT_place_autocomplete.setText(place.getAddress());
        place_for_serach = new MarkerOptions().position(place.getLatLng()).title("Dest");
        mMap.addMarker(place_for_serach);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
    }

    private void validateButtons() {
        if (isMyServiceRunning(LocationService.class)) {
            if (!map_BTN_stop.isClickable()) {
                map_BTN_start.setClickable(true);
                map_BTN_start.setEnabled(true);
            } else if (!map_BTN_pause.isClickable()) {
                map_BTN_start.setClickable(true);
                map_BTN_start.setEnabled(true);
            } else {
                map_BTN_pause.setEnabled(true);
                map_BTN_stop.setEnabled(true);
                map_BTN_pause.setClickable(true);
                map_BTN_stop.setClickable(true);
            }
        }
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
        map_BTN_directions = findViewById(R.id.map_BTN_directions);
        map_BTN_gps = findViewById(R.id.map_BTN_gps);
        map_BTN_start = findViewById(R.id.map_BTN_start);
        map_BTN_stop = findViewById(R.id.map_BTN_stop);
//        map_EDT_place_autocomplete = findViewById(R.id.map_EDT_place_autocomplete);
//        autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.map_EDT_place_autocomplete);
        map_LBL_distance = findViewById(R.id.map_LBL_distance);
        map_LBL_time = findViewById(R.id.map_LBL_time);
        map_BTN_state_elite = findViewById(R.id.map_BTN_state_elite);
        map_BTN_pause = findViewById(R.id.map_BTN_pause);
        map_IMG_zoomIn = findViewById(R.id.map_IMG_zoomIn);
        map_IMG_zoomOut = findViewById(R.id.map_IMG_zoomOut);
        map_BTN_start_timer= findViewById(R.id.map_BTN_start_timer);
        map_MAP_sort = findViewById(R.id.map_MAP_sort);
        map_MAP_show = findViewById(R.id.map_MAP_show);
    }



    private void getDestinationInfo(LatLng latLngDestination) {
        final LatLng origin = location;
        final LatLng destination = latLngDestination;
        //-------------Using AK Exorcist Google Direction Library---------------\\
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.BICYCLING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            distance = distanceInfo.getText();
                            duration = durationInfo.getText();
                            //------------Displaying Distance and Time-----------------\\
                            showingDistanceTime(distance, duration); // Showing distance and time to the user in the UI \\

                            //--------------Drawing Path-----------------\\
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(),
                                    directionPositionList, 5, Color.BLUE);
                            mMap.addPolyline(polylineOptions);
                            //--------------------------------------------\\

                            //-----------Zooming the map according to marker bounds-------------\\
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(destination);
                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            mMap.animateCamera(cu);
                            //------------------------------------------------------------------\\

                        } else if (status.equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(getApplicationContext(), "No routes exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error in find routes", Toast.LENGTH_SHORT).show();
                    }
                });
        //-------------------------------------------------------------------------------\\

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

    private void showingDistanceTime(String distance, String duration) {
        map_LBL_time.setText(duration);
        map_LBL_distance.setText(distance);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);


    }

    private void startService() {
        Log.d("stas", "start Service ");
        map_BTN_stop.setClickable(true);
        map_BTN_pause.setClickable(true);

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
        map_BTN_stop.setClickable(false);
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

}