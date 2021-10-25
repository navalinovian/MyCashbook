package com.navalinovian.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    EditText oldPassword, newPassword;
    Button submit, cancel;
    DBHandler dbHandler;
    String  person_id;
    boolean success = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        oldPassword = findViewById(R.id.old_password_edittext);
        newPassword = findViewById(R.id.new_password_edittext);
        submit = findViewById(R.id.simpan_password_button);
        cancel = findViewById(R.id.cancel_password_button);
        person_id = getIntent().getStringExtra("person_id");

        dbHandler = new DBHandler(Settings.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dbHandler.authenticate(Integer.parseInt(person_id), oldPassword.getText().toString().trim())){
                    try {
                        dbHandler.changePassword(Integer.parseInt(person_id), newPassword.getText().toString().trim());
                        success = true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (success){
                        Intent i = new Intent(Settings.this, Home.class).putExtra("person_id", person_id);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Settings.this.finish();
                    }
                }else{
                    Toast.makeText(Settings.this, "Password lama salah", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}