package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText login_EDT_email;
    private MaterialButton login_BTN_login;
    private MaterialButton login_BTN_register;
    private TextView login_LBL_errorMessage;
    private ProgressBar login_PGB_pgb;
    private ImageView login_IMG_background;
    private ArrayList<Account> accountList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        login_PGB_pgb.setVisibility(View.GONE);
        login_BTN_login.setOnClickListener(fillAccount);
        login_BTN_register.setOnClickListener(fillAccount);
    }

    private void onLoginSuccess(Account c, Intent intent) {
        login_PGB_pgb.setVisibility(View.VISIBLE);
        this.startActivity(intent);
        Toast.makeText(getBaseContext(), "Connected Successfully " , Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String url = "http://192.168.1.211:8010/twins/users/login/2021b.stanislav.krot/" + login_EDT_email.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject >() {
            @Override
            public void onResponse(JSONObject   response) {
                try{
                    // Loop through the array elements
                    for(int i=0;i<response.length();i++){
                        JSONObject jsonUserId = response.getJSONObject("userId");
                        String userEmail = jsonUserId.getString("email");
                        String userRole = response.getString("role");
                        String username = response.getString("username");
                        String avatar = response.getString("avatar");
                        Account c = new Account(userEmail,userRole,username,avatar);
//                        alist.add(c);

                        Log.d("ptt",c.toString());
                        Intent intent = new Intent(LoginActivity.this, StartUpActivity.class);
                        intent.putExtra("EMAIL", c.getEmail());
                        intent.putExtra("ROLE", c.getRole());
                        Log.d("ptt","role is " + c.getRole());
                        intent.putExtra("AVATAR", c.getAvatar());
                        intent.putExtra("USERNAME", c.getUsername());
                        intent.putExtra("NAME",c.getUsername());
                        onLoginSuccess(c, intent);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoginFailed();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "login failed -> EMAIL is incorrect " , Toast.LENGTH_LONG).show();
        login_PGB_pgb.setVisibility(View.GONE);
    }

    private View.OnClickListener fillAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if (view.getTag().toString().equals("login")) {
            accountList = new ArrayList<>();
            getRequest();
        } else if ((view.getTag().toString().equals("register"))) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            this.startActivity(intent);
        }
    }

    private void findViews() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_register = findViewById(R.id.login_BTN_register);
        login_LBL_errorMessage = findViewById(R.id.login_LBL_errorMessage);
        login_PGB_pgb  = findViewById(R.id.login_PGB_pgb);
        login_IMG_background = findViewById(R.id.login_IMG_background);
    }


}