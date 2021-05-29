package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdateItemActivity extends AppCompatActivity {



   private EditText UpdateItem_EDT_type;
   private EditText UpdateItem_EDT_name;
   private EditText UpdateItem_EDT_active;
   private EditText UpdateItem_EDT_Location;
   private EditText UpdateItem_EDT_parkNum;
   private Button UpdateItem_BTN_add;
    private CreatedBy cb;
    private String type;
    private String active;
    private String location;
    private  String name;
    private String email;
    private Parking park;
    private String locationStr;
    private Location currentLocation;
    private String[] loc;
    private Map<String,Object> itemAtt = new HashMap<String,Object>();
    private Date date = new Date();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        findViews();
        UpdateItem_BTN_add.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_type.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_name.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_active.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_Location.setOnClickListener(updateItemClicked);
    }

    private View.OnClickListener updateItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);

        }
    };

    private void buttonClicked(View view) {
        if(view.getTag().toString().equals("add")){
            location = UpdateItem_EDT_Location.getText().toString();
            GeoLocation geoLocation = new GeoLocation();
            geoLocation.getAddress(location, getApplicationContext());
            currentLocation = new Location(location);
            locationStr = geoLocation.getResult();
            loc = locationStr.split(",");
            currentLocation.setLatitude(Double.parseDouble(loc[0]));
            currentLocation.setLongitude(Double.parseDouble(loc[1]));
            active = UpdateItem_EDT_active.getText().toString();
            name = UpdateItem_EDT_name.getText().toString();
            type = UpdateItem_EDT_type.getText().toString();
            park = createPark();
            
            putRequest();

        }
    }

    private Parking createPark() {
        cb = new CreatedBy(new UserId("2021b.stanislav.krot",email));
        return new Parking(new ParkingId(),type,name,Boolean.parseBoolean(active),date,cb,currentLocation,itemAtt);
    }

    private void putRequest() {
        String url = "http://192.168.1.211:8010/twins/items/2021b.stanislav.krot/1@gmail.com/defaultName/a9f29e05-8c3a-428e-b7fe-c6ec59d5f948";
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

            js.put("type",type);
            js.put("name",name);
            js.put("active",active);
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
                Request.Method.PUT, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("stas14", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas14", "Error: " + error.getStackTrace().toString());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("stas14", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Log.d("stas14","getting params");
//                Gson gson = new Gson();
//                String json = gson.toJson(account);
                Map<String,String> params = new HashMap<String,String>();

                Log.d("stas14","returned params");
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

    private void findViews() {
        UpdateItem_EDT_type = findViewById(R.id.UpdateItem_EDT_type);
        UpdateItem_EDT_name = findViewById(R.id.UpdateItem_EDT_name);
        UpdateItem_EDT_active = findViewById(R.id.UpdateItem_EDT_active);
        UpdateItem_EDT_Location = findViewById(R.id.UpdateItem_EDT_Location);
        UpdateItem_EDT_parkNum = findViewById(R.id.UpdateItem_EDT_parkNum);
        UpdateItem_BTN_add = findViewById(R.id.UpdateItem_BTN_add);
    }

}