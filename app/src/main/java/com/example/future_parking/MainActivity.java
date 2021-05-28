package com.example.future_parking;

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
import com.example.future_parking.activities.RegisterActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private EditText login_EDT_email;
    private MaterialButton login_BTN_login;
    private MaterialButton login_BTN_register;
    private TextView login_LBL_errorMessage;
    private ProgressBar login_PGB_pgb;
    private ImageView login_IMG_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_register = findViewById(R.id.login_BTN_register);
        login_LBL_errorMessage = findViewById(R.id.login_LBL_errorMessage);
        login_PGB_pgb  = findViewById(R.id.login_PGB_pgb);
        login_IMG_background = findViewById(R.id.login_IMG_background);
    }



}