package com.example.loginblueprint;

import com.google.gson.annotations.SerializedName;

public class TopTrack {
        @SerializedName("name")
        private String name;

        @SerializedName("playcount")
        private int playCount;

        public String getTopTrack() {
                return name;
        }

        public String setTopTrack(String topName){
                name = topName;
               return name;
        }
}
