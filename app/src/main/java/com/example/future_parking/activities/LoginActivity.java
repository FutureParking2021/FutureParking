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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText login_EDT_email;
    private EditText login_EDT_password;
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

//        Glide
//                .with(this)
//                .load(R.drawable.parking)
//                .centerCrop()
//                .into(login_IMG_background);
    }






    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean checkValidLBl(EditText email, EditText password) {
        if(isEmail(email) == false || isEmpty(email))
        {
            email.setError("Enter valid email!");
            return false;
        }
        else if (isEmpty(password) || password.length() < 4 || password.length() > 10  ) {
            password.setError("between 4 and 10 alphanumeric characters");
            return false;
        }

        return  true;
    }

    private void checkUserValid() {
        if (checkValidLBl(login_EDT_email, login_EDT_password)) {
            login_BTN_login.setClickable(true);
            login_PGB_pgb.setVisibility(View.VISIBLE);
            String email = login_EDT_email.getText().toString().trim();
            String password = login_EDT_password.getText().toString().trim();
            // if data correct loginSuccess else loginFailed
        }

    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "login failed -> username or password is incorrect " , Toast.LENGTH_LONG).show();
        login_BTN_login.setEnabled(true);
        login_PGB_pgb.setVisibility(View.GONE);

    }

    private void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Connected Successfully " , Toast.LENGTH_LONG).show();
    }

    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String url = "http://192.168.1.211:8080/twins/admin/users/2021b.twins/1@gmail.com";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                Log.d("json",jsonArray.toString());
                try {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonUserId = jsonObject.getJSONObject("userId");
                        String email = jsonUserId.getString("email");
                        String role = jsonObject.getString("role");
                        String username = jsonObject.getString("username");
                        String avatar = jsonObject.getString("avatar");
                        Account c = new Account(email,role,username,avatar);
                        Log.d("stas5",c.toString());
                        accountList.add(c);
                        Log.d("account","account size " +  accountList.size());

                    }
                    checkValidUser(accountList);
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

    private void checkValidUser(ArrayList<Account> accountList) {
        for (Account c : accountList) {
            if (c.getEmail().equals(login_EDT_email.getText().toString())) {
                Intent intent = new Intent(LoginActivity.this, StartUpActivity.class);
                intent.putExtra("EMAIL", c.getEmail());
                intent.putExtra("ROLE", c.getRole());
                this.startActivity(intent);
                onLoginSuccess();
                finish();
            }
        }
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
//
//            }

//            finish();
        } else if ((view.getTag().toString().equals("register"))) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            this.startActivity(intent);
        }

    }

    private void findViews() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_register = findViewById(R.id.login_BTN_register);
        login_LBL_errorMessage = findViewById(R.id.login_LBL_errorMessage);
        login_PGB_pgb  = findViewById(R.id.login_PGB_pgb);
        login_IMG_background = findViewById(R.id.login_IMG_background);
    }


}