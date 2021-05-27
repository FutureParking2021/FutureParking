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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartUpActivity extends AppCompatActivity {
    private MaterialButton Start_BTN_bikeRide;
    private MaterialButton Start_BTN_settings;
    private MaterialButton Start_BTN_logout;


  private MaterialButton Start_BTN_makePark;
  private MaterialButton Start_BTN_updatePark;
  private MaterialButton Start_BTN_operations;
    private String role;
    private String email;
    private String username;
    private String avatar;
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
        Start_BTN_makePark.setOnClickListener(buttonClickListener);
        Start_BTN_updatePark.setOnClickListener(buttonClickListener);
        Start_BTN_operations.setOnClickListener(buttonClickListener);
        Start_BTN_settings.setOnClickListener(buttonClickListener);
        Start_BTN_logout.setOnClickListener(buttonClickListener);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        username = getIntent().getStringExtra("USERNAME");
        avatar = getIntent().getStringExtra("AVATAR");

//        getRequest();
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
        if(view.getTag().toString().equals("make")){
            if(role.equals("MANAGER")){
                makeParkingActivity();
            } else {
                Toast.makeText(StartUpActivity.this,"ONLY MANAGER CAN CREATE PARK",Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getTag().toString().equals("update")){
            if(role.equals("MANAGER")){
                updateActivity();
            } else {
                Toast.makeText(StartUpActivity.this,"ONLY MANAGER CAN CREATE PARK",Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getTag().toString().equals("operations")){
            operationsActivity();
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
        intent.putExtra("USERNAME", username);
        intent.putExtra("ROLE", role);
        intent.putExtra("AVATAR", avatar);
        intent.putExtra("EMAIL", email);
        this.startActivity(intent);
    }

    private void operationsActivity() {
        Intent intent = new Intent(getApplicationContext(),OperationsActivity.class);
        intent.putExtra("EMAIL",email);
        intent.putExtra("ROLE", role);
        this.startActivity(intent);
    }

    private void updateActivity() {
        Intent intent = new Intent(getApplicationContext(), UpdateItemActivity.class);
        this.startActivity(intent);
        intent.putExtra("EMAIL", email);
    }

    private void makeParkingActivity() {
        Intent intent = new Intent(getApplicationContext(), NewParkingActivity.class);
        intent.putExtra("EMAIL", email);
        this.startActivity(intent);
    }

    private void rideActivity() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("EMAIL", email);
        intent.putExtra("ROLE", role);
        this.startActivity(intent);
    }

    private void findViews(){
        Start_BTN_bikeRide = findViewById(R.id.Start_BTN_bikeRide);
        Start_BTN_makePark = findViewById(R.id.Start_BTN_makePark);
        Start_BTN_updatePark = findViewById(R.id.Start_BTN_updatePark);
        Start_BTN_operations = findViewById(R.id.Start_BTN_operations);
        Start_BTN_settings = findViewById(R.id.Start_BTN_settings);
        Start_BTN_logout = findViewById(R.id.Start_BTN_logout);
    }
}