package com.example.future_parking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.future_parking.R;
import com.example.future_parking.classes.Account;

public class DataActivity extends AppCompatActivity {
    private EditText data_EDT_name;
    private EditText data_EDT_password;
    private EditText data_EDT_email;
    private Button data_BTN_save;
    private Account account;
    String newName;
    String newEmail;
    String newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        findViews();
        data_BTN_save.setOnClickListener(save);
    }

    private View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            updateData();
        }
    };


    private void updateData() {
        Log.d("stas", "entered updating");
//        String userID = firebaseAuth.getCurrentUser().getUid();

        newName = data_EDT_name.getText().toString();
        newPassword = data_EDT_password.getText().toString();
        newEmail = data_EDT_email.getText().toString();;

        if (!validate()) {
            Log.d("stas", "check if validate");
            onSignupFailed();
            return;
        }
        if(!newName.isEmpty()){
//            database.getReference().child("users").child(userID).child("name").setValue(newName);

        }
        if(!newPassword.isEmpty()){
//            database.getReference().child("users").child(userID).child("password").setValue(newPassword);
        }
        if(!newEmail.isEmpty()){
//            database.getReference().child("users").child(userID).child("email").setValue(newEmail);
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

        if (!newPassword.isEmpty() && (newPassword.length() < 4 || newPassword.length() > 10)) {
            data_EDT_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            data_EDT_password.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();
        data_BTN_save.setEnabled(true);
    }

    public void onSignupSuccess() {
        data_BTN_save.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void saveAccountToFB(Account updateAccount) {
//        String userID = firebaseAuth.getCurrentUser().getUid();
//        DatabaseReference myDataRef = database.getReference("users");
//        myDataRef.child(userID).setValue(account);
//        Log.d("stas", "info CHANGED");
    }


    private void findViews() {
        data_EDT_name = findViewById(R.id.data_EDT_name);
        data_EDT_password = findViewById(R.id.data_EDT_password);
        data_EDT_email = findViewById(R.id.data_EDT_email);
        data_BTN_save = findViewById(R.id.data_BTN_save);
    }
}