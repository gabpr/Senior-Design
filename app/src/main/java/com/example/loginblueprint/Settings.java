package com.example.loginblueprint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Button logout = findViewById(R.id.LogOutbtn);
        final Button saveChanges = findViewById(R.id.savechanges);
        EditText newLastFMusername = (EditText) findViewById(R.id.updatelastfmuser);

        String newLastFM = newLastFMusername.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", ""); // retrieve current username from shared preferences

        Toast.makeText(Settings.this, currentUsername
                , Toast.LENGTH_SHORT).show();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update last.fm user info here
                String newLastFM = newLastFMusername.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String currentUsername = sharedPreferences.getString("username", ""); // retrieve current username from shared preferences

            }
        });
    }
}
