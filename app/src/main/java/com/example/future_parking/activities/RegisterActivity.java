package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.Volley;
import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
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
    private TextView register_LBL_role;
    private MaterialButton register_BTN_create;
    private TextView register_TXT_login;
    private ProgressBar register_PGB_pgb;
    private Account account;
    private String role;
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
        register_LBL_role.setOnClickListener(createAccountClicked);
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
        } else if(view.getTag().toString().equals("role")){
            String[] units = {"PLAYER", "MANAGER","ADMIN"};
            unitSelection(units);
        }

    }

    private void unitSelection(String[] choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = choose[which];
//                Log.d("role","role is " + name);
                register_LBL_role.setText(role);
            }
        });
        builder.show();
    }

    private Account createAccount() {
        String name=register_EDT_name.getText().toString();
        String email= register_EDT_email.getText().toString();
        Account user = new Account(email,role,name,"J");
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
            onSignupSuccess();
        }
    }

    private void postRequest() {
        String url = "http://192.168.1.211:8010/twins/users/";
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
                        Log.d("response", "response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
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
        return valid;
    }

    private void findViews() {
        register_EDT_name=findViewById(R.id.register_EDT_name);
        register_EDT_email=findViewById(R.id.register_EDT_email);
        register_LBL_role = findViewById(R.id.register_LBL_role);
        register_BTN_create=findViewById(R.id.register_BTN_create);
        register_TXT_login =findViewById(R.id.register_TXT_login);
        register_PGB_pgb = findViewById(R.id.register_PGB_pgb);
    }
}