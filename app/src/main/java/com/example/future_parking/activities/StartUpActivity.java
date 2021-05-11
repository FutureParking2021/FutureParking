package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.future_parking.R;
import com.google.android.material.button.MaterialButton;

public class StartUpActivity extends AppCompatActivity {
    private MaterialButton Start_BTN_bikeRide;
    private MaterialButton Start_BTN_goal;
    private MaterialButton Start_BTN_history;
    private MaterialButton Start_BTN_statistics;
    private MaterialButton Start_BTN_settings;
    private MaterialButton Start_BTN_logout;

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
            goalActivity();
        }
        if(view.getTag().toString().equals("history")){
            historyActivity();
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
//        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
//        this.startActivity(intent);
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