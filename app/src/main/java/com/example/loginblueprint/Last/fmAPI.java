package com.example.loginblueprint.Last;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface fmAPI {
        @GET("?method=user.getTopTracks&format=json")
        Call<JsonObject> getTopTracks(
                @Query("user") String user,
                @Query("api_key") String apiKey
        );
}
