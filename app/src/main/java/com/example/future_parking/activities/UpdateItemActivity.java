package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.example.future_parking.classes.CreatedBy;
import com.example.future_parking.classes.CustomJsonRequest;
import com.example.future_parking.classes.GeoLocation;
import com.example.future_parking.classes.Parking;
import com.example.future_parking.classes.ParkingId;
import com.example.future_parking.classes.UserId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UpdateItemActivity extends AppCompatActivity {
    private EditText UpdateItem_EDT_name;
    private EditText UpdateItem_EDT_active;
    private EditText UpdateItem_EDT_price;
    private EditText UpdateItem_EDT_parkNum;
    private Button UpdateItem_BTN_add;
    private TextView UpdateItem_LBL_name;
    private TextView UpdateItem_LBL_id;
    private TextView UpdateItem_LBL_park;
    private CreatedBy cb;
    private String type;
    private String active;
    private String location;
    private  String name;
    private String email;
    private Parking park;
    private String role;
    private String locationStr;
    private Location currentLocation;
    private String[] loc;
    private Map<String,Object> itemAtt = new HashMap<String,Object>();
    private Date date = new Date();
    private Map<String,JSONArray> parkList = new HashMap<>();
    private String parkName;
    private String id;
    private String parkId;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        findViews();
        UpdateItem_BTN_add.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_name.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_active.setOnClickListener(updateItemClicked);
        UpdateItem_EDT_price.setOnClickListener(updateItemClicked);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        name = getIntent().getStringExtra("NAME");
//        changeRole("PLAYER");
//        getAllParkings();
        role = "MANAGER";
        Log.d("qwww","parks " + UpdateItem_LBL_park.getText().toString());

        //Intialize it first


    }

    private View.OnClickListener updateItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if(view.getTag().toString().equals("add")){
//            location = UpdateItem_EDT_Location.getText().toString();
//            GeoLocation geoLocation = new GeoLocation();
//            geoLocation.getAddress(location, getApplicationContext());
//            currentLocation = new Location(location);
//            locationStr = geoLocation.getResult();
//            loc = locationStr.split(",");
//            currentLocation.setLatitude(Double.parseDouble(loc[0]));
//            currentLocation.setLongitude(Double.parseDouble(loc[1]));
            active = UpdateItem_EDT_active.getText().toString();
            name = UpdateItem_EDT_name.getText().toString();
            park = createPark();
//            getAllParkings("PLAYER");
//            getRequest();
//            putRequest();

        }
    }

    private void changeRole(String role) {
        Log.d("pstt", "change role" + email);
        String url = "http://192.168.1.211:8010/twins/users/2021b.stanislav.krot/" + email;
        JSONObject js = new JSONObject();
        JSONObject jsUid = new JSONObject();

        try {
            jsUid.put("space","2021b.stanislav.krot");
            jsUid.put("email",email);
            js.put("userId",jsUid);
            js.put("role",role);
            js.put("username", name);
            js.put("avatar","J");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("pstt", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("pstt", "Error: " + error.getMessage());
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
                Log.d("stas1","getting params");
//                Gson gson = new Gson();
//                String json = gson.toJson(account);
                Map<String,String> params = new HashMap<String,String>();

                Log.d("stas1","returned params");
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

    private void getAllParkings() {
        String url = "http://192.168.1.211:8010/twins/operations?page=0&size=50";
        JSONObject js = new JSONObject();
        JSONObject jsItem = new JSONObject();
        JSONObject jsItemId = new JSONObject();
        JSONObject jsInvokedBy = new JSONObject();
        JSONObject jsUserId = new JSONObject();
        JSONObject jsOperationAtt = new JSONObject();
        try {
            js.put("type", "getAllParkingLotsByUser");
            jsItemId.put("space", "2021b.stanislav.krot");
            jsItemId.put("id", "");
            jsItem.put("itemId", jsItemId);
            js.put("item", jsItem);

            jsUserId.put("space", "2021b.stanislav.krot");
            jsUserId.put("email", email);
            jsInvokedBy.put("userId", jsUserId);
            js.put("invokedBy", jsInvokedBy);

            js.put("operationAttributes", jsOperationAtt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() == 0){
                            Toast.makeText(UpdateItemActivity.this,"Doesn't have any park lots",Toast.LENGTH_SHORT).show();
                        }else{
                            UpdateItem_LBL_park.setText(response.toString());

                        }

                        Log.d("jttt", response.toString() + " i am queen");

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

    private void choosePark(JSONArray parks) {
        String[] parkChoose = new String[parks.length()];
        for (int i = 0; i<parks.length(); i++){
            try {
                Log.d("qwww","adding names");
                parkChoose[i] = (parks.getJSONObject(i).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("qwww","before selecting");
            unitSelection(parkChoose);
            for (int j = 0; j<parks.length(); j++){
                try {
                    Log.d("qwww","prk name " + parkName);
                    if(parks.getJSONObject(j).getString("name").equals(parkName)){
//                        parkList.put("item",parks.getJSONObject(j));
                        UpdateItem_LBL_park.setText(parks.getJSONObject(j).toString());
                        UpdateItem_LBL_name.setText(parks.getJSONObject(j).getString("name"));
                        UpdateItem_EDT_name.setText(parks.getJSONObject(j).getString("name"));
                        UpdateItem_EDT_active.setText(parks.getJSONObject(j).getString("active"));
                        UpdateItem_EDT_price.setText((parks.getJSONObject(j).getJSONObject("itemAttributes").getInt("priceOfParking")));
                        UpdateItem_EDT_parkNum.setText((parks.getJSONObject(j).getJSONObject("itemAttributes").getInt("numOfParking")));
                        UpdateItem_LBL_id.setText(parks.getJSONObject(j).getJSONObject("itemId").getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unitSelection(String[] parkChoose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(parkChoose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                parkName = parkChoose[which];
//                Log.d("role","role is " + name);
            }
        });
        builder.show();
    }

    private Parking createPark() {
        cb = new CreatedBy(new UserId("2021b.stanislav.krot",email));
        return new Parking(new ParkingId(),type,name,Boolean.parseBoolean(active),date,cb,null,itemAtt);
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
        UpdateItem_EDT_name = findViewById(R.id.UpdateItem_EDT_name);
        UpdateItem_EDT_active = findViewById(R.id.UpdateItem_EDT_active);
        UpdateItem_EDT_price = findViewById(R.id.UpdateItem_EDT_Price);
        UpdateItem_EDT_parkNum = findViewById(R.id.UpdateItem_EDT_parkNum);
        UpdateItem_BTN_add = findViewById(R.id.UpdateItem_BTN_add);
        UpdateItem_LBL_name = findViewById(R.id.UpdateItem_LBL_name);
        UpdateItem_LBL_park = findViewById(R.id.UpdateItem_LBL_park);
        UpdateItem_LBL_id = findViewById(R.id.UpdateItem_LBL_id);

    }

}