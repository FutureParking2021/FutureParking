package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.future_parking.R;
import com.example.future_parking.classes.Account;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
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
    private ArrayList<Integer> carNumebr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        register_PGB_pgb.setVisibility(View.GONE);

    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private Account createAccount() {

        String name=register_EDT_name.getText().toString();
        String email= register_EDT_email.getText().toString();
        String password= register_EDT_password.getText().toString();
        Account user = new Account(name,email,password,carNumebr,"admin");
        return  user;
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        register_BTN_create.setEnabled(false);
        register_PGB_pgb.setVisibility(View.VISIBLE);
        account = createAccount();
        /*
         * check on signUpSuccess and signUpFailed
         * */
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