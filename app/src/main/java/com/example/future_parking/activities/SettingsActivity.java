package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.future_parking.R;

public class SettingsActivity extends AppCompatActivity {
    private TextView settings_LBL_data;
    private TextView settings_LBL_units;
    private TextView settings_LBL_timer;
    private TextView settings_LBL_owner;
    private Switch settings_SWT_screenOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        settings_LBL_data.setOnClickListener(textClicked);
        settings_LBL_units.setOnClickListener(textClicked);
        settings_LBL_timer.setOnClickListener(textClicked);
        settings_LBL_owner.setOnClickListener(textClicked);
    }

    private View.OnClickListener textClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag().toString().equals("data")) {
                Log.d("stas", "data pressed");
                Intent intent = new Intent(SettingsActivity.this, DataActivity.class);
                SettingsActivity.this.startActivity(intent);
            }

            else if (view.getTag().toString().equals("units")) {
                String[] units = {"Metric (km)", "Imperial (mi)"};
                unitSelection(units);
            }
            else if (view.getTag().toString().equals("timer")) {
                String[] units = {"5", "10", "15", "20", "25", "30"};
                unitSelection(units);
            }
            else if (view.getTag().toString().equals("owners")) {
                showOwnersDialog();
            }
            else if (settings_SWT_screenOn.isActivated()) {
//                Log.d("stas", "Screen on");
//                View v = getLayoutInflater().inflate(R.layout.map_activity, null);
//                v.setKeepScreenOn(true);
//                setContentView(v);
            } else {
//                Log.d("stas", "Screen off");
//                View v = getLayoutInflater().inflate(R.layout.map_activity, null);
//                v.setKeepScreenOn(false);
//                setContentView(v);
            }
        }

    };

    private void unitSelection(String[] choose) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

    }

    private void showOwnersDialog() {
        final AlertDialog.Builder ownerDialog = new AlertDialog.Builder(this);
        ownerDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL);
        final TextView input1 = new TextView(this);
        final TextView input2 = new TextView(this);
        input1.setText("    stas.krot1996@gmail.com");
        input2.setText("    yonatani94@gmail.com");
        lila1.addView(input1);
        lila1.addView(input2);
        ownerDialog.setView(lila1);
        ownerDialog.setTitle("Owners Of The App");
        ownerDialog.show();
    }


    private void findViews() {
        settings_LBL_data = findViewById(R.id.settings_LBL_data);
        settings_SWT_screenOn = findViewById(R.id.settings_SWT_screenOn);
        settings_LBL_units = findViewById(R.id.settings_LBL_units);
        settings_LBL_timer = findViewById(R.id.settings_LBL_timer);
        settings_LBL_owner = findViewById(R.id.settings_LBL_owner);
    }
}