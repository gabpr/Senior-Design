package com.example.loginblueprint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LastFmService {
    private static final String API_KEY = "e256c8a64c61c633ce8c2c1b0f864278";
    private static final String API_BASE_URL = "https://ws.audioscrobbler.com/2.0/";

    private OkHttpClient client;
    private Gson gson;

    public LastFmService() {

    }

    public void LastfmService() {
        this.client = new OkHttpClient();
        this.gson = new GsonBuilder().create();
    }

    public LastFmService(OkHttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public TopTracksResponse getTopTracks(String username) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("method", "user.gettoptracks");
        urlBuilder.addQueryParameter("user", username);
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("format", "json");

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return gson.fromJson(responseBody, TopTracksResponse.class);
    }

    public static class TopTracksResponse {
        public TopTracks toptracks;

        public static class TopTracks {
            public Track[] track;
        }

        public static class Track {
            public String name;
            public String artist;
            public String url;
            public String image;
        }
    }
}

