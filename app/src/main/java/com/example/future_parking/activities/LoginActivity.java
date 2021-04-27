package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.future_parking.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private EditText login_EDT_email;
    private EditText login_EDT_password;
    private MaterialButton login_BTN_login;
    private MaterialButton login_BTN_register;
    private TextView login_LBL_errorMessage;
    private ProgressBar login_PGB_pgb;
    private ImageView login_IMG_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        login_PGB_pgb.setVisibility(View.GONE);
        login_BTN_login.setOnClickListener(fillAccount);
        login_BTN_register.setOnClickListener(fillAccount);
        Glide
                .with(this)
                .load(R.drawable.parking)
                .centerCrop()
                .into(login_IMG_background);

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

    }


    private View.OnClickListener fillAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClicked(view);
        }
    };

    private void buttonClicked(View view) {


        if (view.getTag().toString().equals("login")) {

        } else if ((view.getTag().toString().equals("register"))) {
            Intent intent = new Intent(this, RegisterActivity.class);
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