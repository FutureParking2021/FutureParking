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
        String url = "http://192.168.1.211:8010/twins/operations";
        JSONObject js = new JSONObject();
        JSONObject jsOperationId = new JSONObject();
        JSONObject jsItem = new JSONObject();
        JSONObject jsItemId = new JSONObject();
        JSONObject jsOpItem = new JSONObject();
        JSONObject jsInvokedBy = new JSONObject();
        JSONObject jsUserId = new JSONObject();
        JSONObject jsOperationAtt = new JSONObject();


        try {
            jsOperationId.put("space",null);
            jsOperationId.put("id",null);
            js.put("operationId",jsOperationId);

            js.put("type","enterParking");
            jsItemId.put("space","2021b.stanislav.krot");
            jsItemId.put("id","e0449554-0e1e-4eba-8b7d-0d6cc6f11241");
            jsItem.put("itemId",jsItemId);
            jsOpItem.put("item",jsItem);

            js.put("createdTimestamp",date.getTime());

            jsUserId.put("space","2021b.stanislav.krot");
            jsUserId.put("email",email);
            jsInvokedBy.put("userId",jsUserId);
            js.put("invokedBy",jsInvokedBy);

            jsOperationAtt.put("KEY1", "value1");
            js.put("operationAttributes",jsOperationAtt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("stas10", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas12", "Error: " + error.getMessage());
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
        operations_BTN_invoke = findViewById(R.id.operations_BTN_invoke);
        operations_BTN_aSync = findViewById(R.id.operations_BTN_aSync);
    }
}