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
    private MaterialButton start_BTN_park;
    private MaterialButton start_BTN_settings;
    private MaterialButton start_BTN_logout;
    private MaterialButton start_BTN_makePark;
    private MaterialButton start_BTN_updatePark;
    private MaterialButton start_BTN_operations;
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
        start_BTN_park.setOnClickListener(buttonClickListener);
        start_BTN_makePark.setOnClickListener(buttonClickListener);
        start_BTN_updatePark.setOnClickListener(buttonClickListener);
        start_BTN_operations.setOnClickListener(buttonClickListener);
        start_BTN_settings.setOnClickListener(buttonClickListener);
        start_BTN_logout.setOnClickListener(buttonClickListener);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        username = getIntent().getStringExtra("USERNAME");
        avatar = getIntent().getStringExtra("AVATAR");
        checkPermissions();
    }

    private void checkPermissions() {
        if(role.equals("PLAYER")){
            start_BTN_park.setVisibility(View.VISIBLE);
            start_BTN_park.setClickable(true);
            start_BTN_operations.setVisibility(View.VISIBLE);
            start_BTN_operations.setClickable(true);
            start_BTN_makePark.setVisibility(View.INVISIBLE);
            start_BTN_makePark.setClickable(false);
            start_BTN_updatePark.setVisibility(View.INVISIBLE);
            start_BTN_updatePark.setClickable(false);
        } else if(role.equals("MANAGER")){
            start_BTN_makePark.setVisibility(View.VISIBLE);
            start_BTN_makePark.setClickable(true);
            start_BTN_updatePark.setVisibility(View.VISIBLE);
            start_BTN_updatePark.setClickable(true);
            start_BTN_park.setVisibility(View.INVISIBLE);
            start_BTN_park.setClickable(false);
            start_BTN_operations.setVisibility(View.INVISIBLE);
            start_BTN_operations.setClickable(false);
        } else if (role.equals("ADMIN")){
            start_BTN_park.setVisibility(View.INVISIBLE);
            start_BTN_park.setClickable(false);
            start_BTN_makePark.setVisibility(View.INVISIBLE);
            start_BTN_makePark.setClickable(false);
            start_BTN_updatePark.setVisibility(View.INVISIBLE);
            start_BTN_updatePark.setClickable(false);
        }

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
            Log.d("httt","in make parking");
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
            finish();
        }
        if(view.getTag().toString().equals("settings")){
            settingsActivity();
        }if(view.getTag().toString().equals("logout")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(intent);
            finish();
        }
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
        Log.d("httt","before entering activity");
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
        start_BTN_park = findViewById(R.id.Start_BTN_park);
        start_BTN_makePark = findViewById(R.id.Start_BTN_makePark);
        start_BTN_updatePark = findViewById(R.id.Start_BTN_updatePark);
        start_BTN_operations = findViewById(R.id.Start_BTN_operations);
        start_BTN_settings = findViewById(R.id.Start_BTN_settings);
        start_BTN_logout = findViewById(R.id.Start_BTN_logout);
    }
}