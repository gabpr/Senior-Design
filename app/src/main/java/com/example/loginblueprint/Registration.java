package com.example.loginblueprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Registration extends AppCompatActivity {
    EditText username, password, verifypassword, firstname, lastname, lastfmusername;
    Button register;
//    DBHelper DB;

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = String.valueOf(firstname.getText());
                String last = String.valueOf(lastname.getText());
                String lastfmuser = String.valueOf(lastfmusername.getText());
                String user = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());
                String verifypass = String.valueOf(verifypassword.getText());

                if(!first.equals("") && !last.equals("") && !lastfmuser.equals("") && !user.equals("")
                        && !pass.equals("") && !verifypass.equals("")){
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[5];
                            field[0] = "firstname";
                            field[1] = "lastname";
                            field[2] = "username";
                            field[3] = "password";
                            field[4] = "lastFMusername";
                            //Creating array for data
                            String[] data = new String[5];
                            data[0] = first;
                            data[1] = last;
                            data[2] = user;
                            data[3] = pass;
                            data[4] = lastfmuser;

                            PutData putData = new PutData("http://192.168.1.234/LogInRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(Registration.this, "Registered successfully!"
                                        , Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(getApplicationContext(), UserPage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final TextView txtr=findViewById(R.id.curruser);
        txtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Registration.this,MainActivity.class);
                startActivity(intent);

            }
        });



//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String first = firstname.getText().toString();
//                String last = lastname.getText().toString();
//                String lastfmuser = lastfmusername.getText().toString();
//                String user = username.getText().toString();
//                String pass = password.getText().toString();
//                String verifypass = verifypassword.getText().toString();
//
//                if(user.equals("") || pass.equals(""))
//                    Toast.makeText(Registration.this, "Please enter all required fields"
//                            , Toast.LENGTH_SHORT).show();
//                else{
//                    if(pass.equals(verifypass)) {
//                        Boolean checkuser = DB.checkusername(user);
//                        if(!checkuser) {
//                            // username does not exist, can register username
//                            Boolean insert = DB.insertData(user, pass, first, last, lastfmuser);
//                            if(insert) {
//                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("username", user);
//                                editor.apply();
//                                Toast.makeText(Registration.this, "Registered successfully"
//                                        , Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(), UserPage.class);
//                                startActivity(intent);
//                            }
//                            else{
//                                Toast.makeText(Registration.this, "Registration failed," +
//                                        " try again!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(Registration.this, "Username already exists. " +
//                                    "Please sign in or attempt with a different username", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(Registration.this, "Passwords do not match. Try again!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });
    }
}