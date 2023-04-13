package com.example.loginblueprint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.annotations.SerializedName;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class UserPage extends AppCompatActivity {
  private static final String TAG = "UserPage";
    private LastFmService lastFmService;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        lastFmService = new LastFmService();
        listView = findViewById(R.id.user);

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
    }
}
