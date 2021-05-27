package com.example.future_parking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;

import com.example.future_parking.classes.CreatedBy;
import com.example.future_parking.classes.GeoLocation;
import com.example.future_parking.classes.Parking;
import com.example.future_parking.classes.ParkingId;
import com.example.future_parking.classes.UserId;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewParkingActivity extends AppCompatActivity {

    private Button NewParking_BTN_add;
    private EditText NewParking_EDT_Location;
    private EditText NewParking_EDT_active;
    private EditText NewParking_EDT_name;
    private EditText NewParking_EDT_type;
    private EditText NewParking_EDT_parkNum;
    private EditText NewParking_EDT_pricePerQuarterHour;
    private String type;
    private String active;
    private String location;
    private  String name;
    private Parking park;
    private String email;
    private String[] loc;
    private Location currentLocation;
    private CreatedBy cb;
    private String locationStr;
//    private String email;

    private Map<String,Object> itemAtt = new HashMap<String,Object>();
    private Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_parking);
        findViews();
        email = getIntent().getStringExtra("EMAIL");
        NewParking_BTN_add.setOnClickListener(textClicked);
        NewParking_EDT_Location.setOnClickListener(textClicked);
        NewParking_EDT_active.setOnClickListener(textClicked);
        NewParking_EDT_name.setOnClickListener(textClicked);
        NewParking_EDT_type.setOnClickListener(textClicked);
        NewParking_EDT_pricePerQuarterHour.setOnClickListener(textClicked);
        NewParking_EDT_parkNum.setOnClickListener(textClicked);
    }
    private View.OnClickListener textClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().toString().equals("add")){
                location = NewParking_EDT_Location.getText().toString();
                GeoLocation geoLocation = new GeoLocation();
                geoLocation.getAddress(location, getApplicationContext());
                currentLocation = new Location(location);
                locationStr = geoLocation.getResult();
                loc = locationStr.split(",");
                currentLocation.setLatitude(Double.parseDouble(loc[0]));
                currentLocation.setLongitude(Double.parseDouble(loc[1]));
                active = NewParking_EDT_active.getText().toString();
                name = NewParking_EDT_name.getText().toString();
                type = NewParking_EDT_type.getText().toString();
                cb = new CreatedBy(new UserId("2021b.stanislav.krot",email));
                park = createPark();
                postRequest(park);

            }
        }
    };

    private Parking createPark() {
        cb = new CreatedBy(new UserId("2021b.stanislav.krot",email));
        int price, numOfPark;
        price = Integer.parseInt(NewParking_EDT_parkNum.getText().toString());
        numOfPark = Integer.parseInt(NewParking_EDT_pricePerQuarterHour.getText().toString());
        itemAtt.put("numOfParking",price);
        itemAtt.put("priceOfParking",numOfPark);

        return new Parking(new ParkingId(),"parkingLot",name,Boolean.parseBoolean(active),date,cb,currentLocation,itemAtt);

    }


    private void postRequest(Parking park) {

        String url = "http://192.168.1.211:8010/twins/items/2021b.stanislav.krot/" + email;
        JSONObject js = new JSONObject();
        JSONObject itemJs = new JSONObject();
        JSONObject createdJs = new JSONObject();
        JSONObject userIdJs = new JSONObject();
        JSONObject userDetailIdJs = new JSONObject();
        JSONObject locationJs = new JSONObject();
        JSONObject itemAttJs = new JSONObject();

        try {
            itemJs.put("space","");
            itemJs.put("id","");
            js.put("itemId", itemJs);

            js.put("type","parkingLot");
            js.put("name",park.getName());
            js.put("active",park.getActive());
            js.put("createdTimestamp",date.getTime());

            userDetailIdJs.put("space","2021b.stanislav.krot");
            userDetailIdJs.put("email", email);
            userIdJs.put("userId", userDetailIdJs);
            createdJs.put("createdBy", userIdJs);

            js.put("CreatedBy",cb);

            locationJs.put("lat",currentLocation.getLatitude());
            locationJs.put("lng", currentLocation.getLongitude());
            js.put("location",locationJs);

            for (Map.Entry<String, Object> pair : itemAtt.entrySet()) {
                itemAttJs.put(pair.getKey(),pair.getValue());
            }
            js.put("itemAttributes",itemAttJs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("stas10", response.toString() + " i am queen");
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

    private String unitSelection(String[] choose) {
        String chosen = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        return chosen;
    }

    private void findViews() {
        NewParking_BTN_add = findViewById(R.id.NewParking_BTN_add);
        NewParking_EDT_Location = findViewById(R.id.NewParking_EDT_Location);
        NewParking_EDT_active = findViewById(R.id.NewParking_EDT_active);
        NewParking_EDT_name = findViewById(R.id.NewParking_EDT_name);
        NewParking_EDT_type = findViewById(R.id.NewParking_EDT_type);
        NewParking_EDT_parkNum = findViewById(R.id.NewParking_EDT_parkNum);
        NewParking_EDT_pricePerQuarterHour = findViewById(R.id.NewParking_EDT_pricePerQuarterHour);
    }
}