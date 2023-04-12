package com.example.loginblueprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final TextView txtr=findViewById(R.id.curruser);
        txtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Registration.this,MainActivity.class);
                startActivity(intent);

            }
        });
        final Button btn = findViewById(R.id.registerbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Registration.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}