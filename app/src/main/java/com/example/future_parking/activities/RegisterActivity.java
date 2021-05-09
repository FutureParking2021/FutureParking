package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.example.future_parking.classes.UserId;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    final Handler handler = new Handler(Looper.getMainLooper());
    private int t;

    private EditText register_EDT_name;
    private EditText register_EDT_email;
    private EditText register_EDT_password;
    private MaterialButton register_BTN_create;
    private TextView register_TXT_login;
    private ProgressBar register_PGB_pgb;
    private Account account;
    private Set<Account> set=null;
    Map<String,Object> list = null;
    private String userID;
    private ArrayList<Account> alist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        register_PGB_pgb.setVisibility(View.GONE);
        register_BTN_create.setOnClickListener(createAccountClicked);
    }

    private View.OnClickListener createAccountClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {
        if(view.getTag().toString().equals("register")){
            signup();
        }

    }


    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private Account createAccount() {

        String name=register_EDT_name.getText().toString();
        String email= register_EDT_email.getText().toString();
        String password= register_EDT_password.getText().toString();
        String role = "MANAGER";
        Account user = new Account(email,role,name,"J",password);
        return  user;
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }else {

            register_BTN_create.setEnabled(false);
            register_PGB_pgb.setVisibility(View.VISIBLE);
            account = createAccount();
            HashMap<String, String> fields = new HashMap<>();
            fields.put("email", account.getEmail());
            fields.put("role", account.getRole());
            fields.put("username", account.getUsername());
            fields.put("avatar", account.getAvatar());


            postRequest();
            getRequest();
            onSignupSuccess();
        }
    }

    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
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
                        Account c = new Account(email,role,username,avatar,"asdsad");
                        Log.d("stas5",c.toString());
                        alist.add(c);
                    }
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



    private void postRequest() {
        String url = "http://192.168.1.211:8080/twins/users/";
        JSONObject js = new JSONObject();
        try {
            js.put("email",account.getEmail());
            js.put("role",account.getRole());
            js.put("username",account.getUsername());
            js.put("avatar",account.getAvatar());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("stas1", response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stas1", "Error: " + error.getMessage());
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
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> params = new HashMap<String,String>();

                params.put("Content-Type","application/json; charset=utf-8");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjReq);


    }


    public void onSignupSuccess() {
        register_BTN_create.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        register_BTN_create.setEnabled(true);
        register_PGB_pgb.setVisibility(View.GONE);
    }

    public boolean validate() {
        boolean valid = true;

        String name = register_EDT_name.getText().toString();
        String email = register_EDT_email.getText().toString();
        String password = register_EDT_password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            register_EDT_name.setError("at least 3 characters");
            valid = false;
        } else {
            register_EDT_name.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_EDT_email.setError("enter a valid email address");
            valid = false;
        } else {
            register_EDT_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            register_EDT_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            register_EDT_password.setError(null);
        }

        return valid;
    }

    private void findViews() {
        register_EDT_name=findViewById(R.id.register_EDT_name);
        register_EDT_email=findViewById(R.id.register_EDT_email);
        register_EDT_password= findViewById(R.id.register_EDT_password);
        register_BTN_create=findViewById(R.id.register_BTN_create);
        register_TXT_login =findViewById(R.id.register_TXT_login);
        register_PGB_pgb = findViewById(R.id.register_PGB_pgb);
    }
}