package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.CustomJsonRequest;
import com.example.future_parking.classes.Operations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OperationsActivity extends AppCompatActivity {

    private Button operations_BTN_exit;
    private Button operations_BTN_aSync;
    private Button operations_BTN_deleteOperations;
    private Button operations_BTN_deleteItems;
    private Button operations_BTN_deleteUsers;
    private String email;
    private String role;
    private Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        findViews();
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        operations_BTN_deleteOperations.setOnClickListener(clicked);
        operations_BTN_deleteItems.setOnClickListener(clicked);
        operations_BTN_deleteUsers.setOnClickListener(clicked);
        operations_BTN_exit.setOnClickListener(clicked);
        operations_BTN_aSync.setOnClickListener(clicked);
        visibleButtons();
    }

    private void visibleButtons() {
        if(role.equals("ADMIN")){
            operations_BTN_deleteOperations.setVisibility(View.VISIBLE);
            operations_BTN_deleteItems.setVisibility(View.VISIBLE);
            operations_BTN_deleteUsers.setVisibility(View.VISIBLE);
            operations_BTN_exit.setVisibility(View.INVISIBLE);
            operations_BTN_aSync.setVisibility(View.INVISIBLE);
        }else if(role.equals("PLAYER")){
            operations_BTN_deleteOperations.setVisibility(View.INVISIBLE);
            operations_BTN_deleteItems.setVisibility(View.INVISIBLE);
            operations_BTN_deleteUsers.setVisibility(View.INVISIBLE);
            operations_BTN_exit.setVisibility(View.VISIBLE);
            operations_BTN_aSync.setVisibility(View.VISIBLE);
        }
    }


    private View.OnClickListener clicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().toString().equals("exit")){
                exitRequest();
            } else if (view.getTag().toString().equals("aSync")){

            }else if (view.getTag().toString().equals("deleteUsers")){
                deleteRequest("users");
            }else if (view.getTag().toString().equals("deleteItems")){
                deleteRequest("items");

            }else if (view.getTag().toString().equals("deleteOperations")){
                deleteRequest("operations");

            }
        }
    };

    private void deleteRequest(String delete) {
        String url = "http://192.168.1.211:8010/twins/admin/" + delete + "/2021b.stanislav.krot/" + email;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("httt", "response " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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

        Volley.newRequestQueue(this).add(request);
    }
            private void exitRequest() {
                String url = "http://192.168.1.211:8010/twins/operations?page=0&size=1";
                JSONObject js = new JSONObject();
                JSONObject jsItem = new JSONObject();
                JSONObject jsItemId = new JSONObject();
                JSONObject jsInvokedBy = new JSONObject();
                JSONObject jsUserId = new JSONObject();
                JSONObject jsOperationAtt = new JSONObject();
                try {
                    js.put("type", "exitparking");
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
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("jttt", response.toString() + " i am queen");
                                try {
                                    Toast.makeText(OperationsActivity.this, "Exit Parking lot successfully and payed  " + response.getDouble("Price"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
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

                Volley.newRequestQueue(this).add(jsonObjReq);
            }



        private void findViews() {
            operations_BTN_exit = findViewById(R.id.operations_BTN_exit);
            operations_BTN_aSync = findViewById(R.id.operations_BTN_aSync);
            operations_BTN_deleteOperations = findViewById(R.id.operations_BTN_deleteOperations);
            operations_BTN_deleteItems = findViewById(R.id.operations_BTN_deleteItems);
            operations_BTN_deleteUsers = findViewById(R.id.operations_BTN_deleteUsers);
        }
    }