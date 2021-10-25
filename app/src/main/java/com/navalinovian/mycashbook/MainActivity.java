package com.navalinovian.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText username;
    EditText password;
    DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.username_edittext);
        password = (EditText) findViewById(R.id.password_edittext);

        dbHandler = new DBHandler(MainActivity.this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dbHandler.authenticate(username.getText().toString().trim(), password.getText().toString().trim())){
                    Intent i = new Intent(MainActivity.this, Home.class)
                            .putExtra("person_id", String.valueOf(dbHandler.getUserId(username.getText().toString().trim())));
                    Log.i("Cobalah mengerti", String.valueOf(dbHandler.getUserId(username.getText().toString().trim())));
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    MainActivity.this.finish();
                }else{
                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}