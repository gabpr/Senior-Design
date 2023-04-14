package com.example.loginblueprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // create a table "users"
        // two columns, first column "username", second column "password"
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, firstname TEXT, lastname TEXT, lastfm_username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password, String firstname, String lastname, String lastfmuser) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Log.d("DBHelper", "Inserting data: username=" + username + ", password=" + password + ", firstname=" + firstname + ", lastname=" + lastname + ", lastfmuser=" + lastfmuser);
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("lastfm_username", lastfmuser);
        long result = MyDB.insert("users", null, contentValues);
        if(result == -1){
            // -1 indicates failure to add to DB
            return false;
        }
        else{
            return true;
        }
    }

    // check whether user exists in the DB already
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    public String[] getFullName(String username){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select firstname, lastname from users where username = ?", new String[] {username});
        String[] name = new String[2];
        if (cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex("firstname");
            int lastNameIndex = cursor.getColumnIndex("lastname");
            if (firstNameIndex >= 0) {
                name[0] = cursor.getString(firstNameIndex); // Get first name
            }
            if (lastNameIndex >= 0) {
                name[1] = cursor.getString(lastNameIndex); // Get last name
            }
        }
        cursor.close();
        return name;
    }

    public String getLastFMUsername(String username){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select lastfm_username from users where username = ?", new String[] {username});
        String lastfmusername = null;
        if (cursor.moveToFirst()) {
            int lastfmUsernameIndex = cursor.getColumnIndex("lastfm_username");
            if (lastfmUsernameIndex >= 0) {
                lastfmusername = cursor.getString(lastfmUsernameIndex);
            }
        }
        cursor.close();
        return lastfmusername;
    }

    public boolean updateLastFMUsername(String username, String newLastFMUsername) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lastfm_username", newLastFMUsername);
        int result = MyDB.update("users", contentValues, "username = ?", new String[] {username});
        return result > 0;
    }
}