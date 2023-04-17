package com.example.loginblueprint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Settings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Button logout = findViewById(R.id.LogOutbtn);
        final Button saveChanges = findViewById(R.id.savechanges);
        EditText newLastFMusername = (EditText) findViewById(R.id.updatelastfmuser);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", ""); // set the value of "username" to empty string
                editor.apply(); // apply the changes
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update last.fm user info here
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String currentUsername = sharedPreferences.getString("username", ""); // retrieve current username from shared preferences
                String newLastFM = newLastFMusername.getText().toString();
                if(!newLastFM.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "lastFMusername";

                            String[] data = new String[2];
                            data[0] = currentUsername;
                            data[1] = newLastFM;

                            PutData putData = new PutData("http://192.168.1.234/LogInRegister/updateLastFMusername.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Update Success")){
                                        Toast.makeText(Settings.this, "Last.fm username updated successfully!"
                                                , Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(getApplicationContext(), UserPage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Log.e("TAG", result);
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, UserPage.class);
                startActivity(intent);
            }
        });
    }
}
