package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowDetailsActivity extends AppCompatActivity {
    private TextView details_LBL_details;
    private TextView details_LBL_title;
    private String email;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        findViews();
        email = getIntent().getStringExtra("EMAIL");
        path = getIntent().getStringExtra("URL");
        Log.d("kggg", path);
        details_LBL_title.setText(path.toUpperCase());
        getRequest();
    }

    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(ShowDetailsActivity.this);
        String url = "http://192.168.1.211:8010/twins/admin/" + path + "/2021b.stanislav.krot/" + email;
        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    details_LBL_details.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("respone", "error " + error.getMessage());
            }
        });
        requestQueue.add(JsonArrayRequest);
    }

    private void findViews() {
        details_LBL_details = findViewById(R.id.details_LBL_details);
        details_LBL_title = findViewById(R.id.details_LBL_title);
    }
}