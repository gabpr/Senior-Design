package com.example.loginblueprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    EditText username, password, verifypassword, firstname, lastname, lastfmusername;
    Button register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstname = (EditText) findViewById(R.id.firstName);
        lastname = (EditText) findViewById(R.id.lastName);
        lastfmusername = (EditText) findViewById(R.id.lastfm);
        username = (EditText) findViewById(R.id.usernamer);
        password = (EditText) findViewById(R.id.passwordr);
        verifypassword = (EditText) findViewById(R.id.verifypass);

        register = (Button) findViewById(R.id.registerbtn);
        DBHelper DB = new DBHelper(this);

        final TextView txtr=findViewById(R.id.curruser);
        txtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Registration.this,MainActivity.class);
                startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = firstname.getText().toString();
                String last = lastname.getText().toString();
                String lastfmuser = lastfmusername.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String verifypass = verifypassword.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(Registration.this, "Please enter all required fields"
                            , Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(verifypass)) {
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser) {
                            // username does not exist, can register username
                            Boolean insert = DB.insertData(user, pass, first, last, lastfmuser);
                            if(insert) {
                                Toast.makeText(Registration.this, "Registered successfully"
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), UserPage.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Registration.this, "Registration failed," +
                                        " try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Registration.this, "Username already exists. " +
                                    "Please sign in or attempt with a different username", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Registration.this, "Passwords do not match. Try again!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}