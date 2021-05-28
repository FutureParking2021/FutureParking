package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {
    private Button data_BTN_save;
    private String newName;
    private String newEmail;
    private EditText data_EDT_name;
    private EditText data_EDT_email;
    private TextView data_LBL_role;
    private String newRole;
    private String role;
    private String email;
    private String username;
    private String avatar;
    private String space = "2021b.stanislav.krot";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        findViews();
        data_BTN_save.setOnClickListener(save);
        data_LBL_role.setOnClickListener(save);
        email = getIntent().getStringExtra("EMAIL");
        role = getIntent().getStringExtra("ROLE");
        Log.d("username3", "role= " + role);
        username = getIntent().getStringExtra("USERNAME");
        avatar = getIntent().getStringExtra("USERNAME");
        data_LBL_role.setText("Current Role: " + role);
    }

    private View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getTag().toString().equals("role")){
                String[] units = {"PLAYER", "MANAGER","ADMIN"};
                unitSelection(units);
            }
            updateData();
        }
    };

    private void unitSelection(String[] choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = choose[which];
//                Log.d("role","role is " + name);
                data_LBL_role.setText(role);
            }
        });
        builder.show();
    }

    private void updateData() {

        Log.d("stas", "entered updating");
//        String userID = firebaseAuth.getCurrentUser().getUid();

        newName = data_EDT_name.getText().toString();
        newEmail = data_EDT_email.getText().toString();;

        if (!validate()) {
            Log.d("stas", "check if validate");
            onSignupFailed();
            return;
        }
        if(newName.isEmpty()){
            newName = username;
        }
        if(newEmail.isEmpty()){
            newEmail = email;
        }
        Log.d("stas", "update succssess");
        onSignupSuccess();
    }


    public boolean validate() {
        boolean valid = true;

        if (!newName.isEmpty() && newName.length() < 3) {
            data_EDT_name.setError("at least 3 characters");
            valid = false;
        } else {
            data_EDT_name.setError(null);
        }

        if (!newEmail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            data_EDT_email.setError("enter a valid email address");
            valid = false;
        } else{
            data_EDT_email.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();
        data_BTN_save.setEnabled(true);
    }

    public void onSignupSuccess() {
        data_BTN_save.setEnabled(true);
//        setResult(RESULT_OK, null);
        putRequest();
//        finish();
    }

    private void putRequest() {
        String url = "http://192.168.1.211:8010/twins/users/2021b.stanislav.krot/" + email;
        JSONObject js = new JSONObject();
        JSONObject jsUid = new JSONObject();
        Log.d("ptt","old email " + email);
        Log.d("ptt", "email " + newEmail);
        try {
            jsUid.put("space",space);
            jsUid.put("email",email);
            js.put("userId",jsUid);
            js.put("role",role);
            js.put("username", newName);
            js.put("avatar",avatar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("stas1put", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ptt", "Error: " + error.getMessage());
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



    private void findViews() {
        data_EDT_name = findViewById(R.id.data_EDT_name);
        data_EDT_email = findViewById(R.id.data_EDT_email);
        data_BTN_save = findViewById(R.id.data_BTN_save);
        data_LBL_role = findViewById(R.id.data_LBL_role);
    }
}