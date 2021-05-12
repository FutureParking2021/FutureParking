package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartUpActivity extends AppCompatActivity {
    private MaterialButton Start_BTN_bikeRide;
    private MaterialButton Start_BTN_goal;
    private MaterialButton Start_BTN_history;
    private MaterialButton Start_BTN_statistics;
    private MaterialButton Start_BTN_settings;
    private MaterialButton Start_BTN_logout;
    private String role;
    private String email;
    private ArrayList<Account> alist = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        findViews();
        Start_BTN_bikeRide.setOnClickListener(buttonClickListener);
        Start_BTN_goal.setOnClickListener(buttonClickListener);
        Start_BTN_history.setOnClickListener(buttonClickListener);
        Start_BTN_statistics.setOnClickListener(buttonClickListener);
        Start_BTN_settings.setOnClickListener(buttonClickListener);
        Start_BTN_logout.setOnClickListener(buttonClickListener);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        getRequest();
    }


    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(StartUpActivity.this);
        String url = "http://192.168.1.211:8080/twins/users/login/2021b.stanislav.krot/2@gmail.com";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                Log.d("jsonj",jsonArray.toString());

                try {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonUserId = jsonObject.getJSONObject("userId");
                        String userEmail = jsonUserId.getString("email");
                        String userRole = jsonObject.getString("role");
                        String username = jsonObject.getString("username");
                        String avatar = jsonObject.getString("avatar");
                        Account c = new Account(userEmail,userRole,username,avatar);
                        alist.add(c);
                    }
                }
                catch (Exception w)
                {
                    Log.d("stas4", "exception" + w.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas4", "exception");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if(view.getTag().toString().equals("lets ride")){
            rideActivity();
        }
        if(view.getTag().toString().equals("goal")){
            if(role.equals("MANAGER")){
                goalActivity();
            } else {
                Toast.makeText(StartUpActivity.this,"ONLY MANAGER CAN CREATE PARK",Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getTag().toString().equals("history")){
            if(role.equals("MANAGER")){
                historyActivity();
            } else {
                Toast.makeText(StartUpActivity.this,"ONLY MANAGER CAN CREATE PARK",Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getTag().toString().equals("statistics")){
            statisticsActivity();
        }
        if(view.getTag().toString().equals("settings")){
            settingsActivity();
        }
        if(view.getTag().toString().equals("logout"))
        {
            logoutFromFB();
        }
    }

    private void logoutFromFB() {
//        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        Toast.makeText(getApplicationContext(),"Sign out From Ride With Me App" , Toast.LENGTH_SHORT);
        finish();
    }

    private void settingsActivity() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        this.startActivity(intent);
    }

    private void statisticsActivity() {
//        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
//        this.startActivity(intent);
    }

    private void historyActivity() {
        Intent intent = new Intent(getApplicationContext(), UpdateItemActivity.class);
        this.startActivity(intent);
    }

    private void goalActivity() {
        Intent intent = new Intent(getApplicationContext(), NewParkingActivity.class);
        intent.putExtra("EMAIL", "1@gmail.com");
        this.startActivity(intent);
    }

    private void rideActivity() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        StartUpActivity.this.startActivity(intent);
    }

    private void findViews(){
        Start_BTN_bikeRide = findViewById(R.id.Start_BTN_bikeRide);
        Start_BTN_goal = findViewById(R.id.Start_BTN_goal);
        Start_BTN_history = findViewById(R.id.Start_BTN_history);
        Start_BTN_statistics = findViewById(R.id.Start_BTN_statistics);
        Start_BTN_settings = findViewById(R.id.Start_BTN_settings);
        Start_BTN_logout = findViewById(R.id.Start_BTN_logout);
    }
}