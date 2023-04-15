package com.example.loginblueprint;
import static com.example.loginblueprint.LastFM.getMinsPlayed;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.annotations.SerializedName;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class UserPage extends AppCompatActivity {
    private static final String TAG = "UserPage";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Integer minutesPlayed = getMinsPlayed("anyapop", "7day");
        TextView minsPlayedTextView = (TextView) findViewById(R.id.mins_played_text_view);
        minsPlayedTextView.setText("Minutes played: " + minutesPlayed.toString());
        final Button button = findViewById(R.id.LogOutbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, MainActivity.class);
                startActivity(intent);

            }
        });


        //listView = findViewById(R.id.user);
        /*
        new Thread(() -> {
            try {
                LastFmService.TopTracksResponse topTracksResponse = LastFmService.getTopTracks("your_lastfm_username_here");

                List<String> tracks = new ArrayList<>();
                for (LastFmService.Track track : topTracksResponse.toptracks.track) {
                    tracks.add(Track.name + " by " + Track.artist);
                }

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(UserPage.this, android.R.layout.simple_list_item_1, tracks);
                    listView.setAdapter(adapter);
                });
            } catch (IOException e) {
                Log.e(TAG, "Error getting top tracks", e);
            }
        }).start();

         */
    }
}
