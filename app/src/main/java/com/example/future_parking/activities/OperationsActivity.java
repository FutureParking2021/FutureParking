package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OperationsActivity extends AppCompatActivity {

    private Button operations_BTN_invoke;
    private Button operations_BTN_aSync;
    private String email;
    private Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        findViews();
        email = getIntent().getStringExtra("EMAIL");
        operations_BTN_invoke.setOnClickListener(clicked);
        operations_BTN_aSync.setOnClickListener(clicked);
    }

    private View.OnClickListener clicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().toString().equals("invoke")){
                invokePostRequest();
            } else if (view.getTag().toString().equals("aSync")){

            }
        }
    };

    private void invokePostRequest() {

    }





    private void findViews() {
        operations_BTN_invoke = findViewById(R.id.operations_BTN_invoke);
        operations_BTN_aSync = findViewById(R.id.operations_BTN_aSync);
    }
}