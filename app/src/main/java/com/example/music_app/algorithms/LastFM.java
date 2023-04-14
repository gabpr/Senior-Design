package com.example.music_app.algorithms;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LastFM {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String LFM_API_KEY = "e256c8a64c61c633ce8c2c1b0f864278";

    // Get last.fm response
    // Inputs:
    // payload
    // Outputs:
    // response
    private static HttpResponse lastfmGet(Map<String, String> payload) throws IOException, InterruptedException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse res = null;
        try{
            String url = "https://ws.audioscrobbler.com/2.0/?" + getParamsString(payload);
            HttpGet request = new HttpGet(url);
            res = client.execute(request);
            Integer n = 0;
        }
        catch (Exception e) {
            Integer done = 0;
        }
        return res;
    }

    // Builds api request to send
    private static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        result.append("api_key=").append(LFM_API_KEY);
        result.append("&format=json");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append("&").append(entry.getKey()).append("=").append(entry.getValue().replace(" ", "+"));
        }
        return result.toString();
    }

    // Sorts hashmap by value
    public static LinkedHashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    // Returns user's top artists for a given time period.
    // Inputs -
    // username - last.fm username
    // range - 'overall', '7day', '1month', '3month', '6month', '12month'
    // number - number of artists to fetch (put 0 to get all artists)
    // Returns -
    // topArtistPlays - A sorted list of hashmaps with key, value pairs as such:
    //                - Artist: artist name
    //                - Plays: number of plays for said artist
    public static List<Map<String, String>> getTopArtistPlays(String username, String range, int number)
            throws IOException, InterruptedException {

        List<Map<String, String>> topArtistPlays = new ArrayList<Map<String, String>>();
        Integer pageNum = 1;
        while (number >= 0) {
            Map<String, String> payload = new HashMap<>();
            payload.put("method", "user.getTopArtists");
            payload.put("user", username);
            payload.put("period", range);
            if (number > 1000) {
                payload.put("limit", Integer.toString(1000));
                number -= 1000;
                if (number == 0) {
                    number -= 1;
                }
            } else if (number == 0) {
                payload.put("limit", Integer.toString(1000));
            } else {
                payload.put("limit", Integer.toString(number));
                number = -1;
            }
            payload.put("page", Integer.toString(pageNum));
            pageNum += 1;
            HttpResponse response = lastfmGet(payload);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        String responseBody = new BasicResponseHandler().handleResponse(response);
                        JSONParser parser = new JSONParser();
                        JSONObject contents = (JSONObject) parser.parse(responseBody);
                        JSONObject artistData = (JSONObject) contents.get("topartists");
                        JSONArray artists = (JSONArray) artistData.get("artist");
                        Integer numArtists = artists.size();
                        if (numArtists == 0) {
                            break;
                        }
                        for (Integer i = 0; i < numArtists; i++) {
                            JSONObject newArtist = (JSONObject) artists.get(i);
                            String artistName = (String) newArtist.get("name");
                            String playCount = (String) newArtist.get("playcount");
                            Map<String, String> nextArtist = new HashMap<String, String>();
                            nextArtist.put("Artist", artistName);
                            nextArtist.put("Plays", playCount);
                            topArtistPlays.add(i, nextArtist);
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String error = sw.toString();
                        Map<String, String> errorMessage = new HashMap<String, String>();
                        errorMessage.put("!Error parsing JSON string!", error);
                        topArtistPlays.add(0, errorMessage);
                    }
                } else {
                    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
                    Map<String, String> errorMessage = new HashMap<String, String>();
                    errorMessage.put("!Request rejected!, Status Code -", statusCode);
                    topArtistPlays.add(0, errorMessage);
                }
            }
        }
        return topArtistPlays;
    }

    // Returns user's top tracks for a given time period.
    // Inputs -
    // username - last.fm username
    // range - 'overall', '7day', '1month', '3month', '6month', '12month'
    // number - number of tracks to fetch (put 0 to get all tracks)
    // Returns -
    // top Tracks - A list of hashmaps, ordered by plays, with key, value pairs as such:
    //            - Track: track name
    //            - Plays: number of plays for said track
    //            - Artist: artist of said track
    //            - Duration: length of track in seconds
    public static List<Map<String, String>> getTopTracks(String username, String range, int number)
            throws IOException, InterruptedException {
        Map<String, String> payload = new HashMap<>();
        Integer pageNum = 1;
        List<Map<String, String>> topTrackPlays = new ArrayList<Map<String, String>>();
        while (number >= 0) {
            payload.put("method", "user.getTopTracks");
            payload.put("user", username);
            payload.put("period", range);
            if (number > 1000){
                payload.put("limit", Integer.toString(1000));
                number -= 1000;
                if (number == 0) {
                    number -= 1;
                }
            }
            else if (number == 0){
                payload.put("limit", Integer.toString(1000));
            }
            else {
                payload.put("limit", Integer.toString(number));
                number = -1;
            }

            payload.put("page", Integer.toString(pageNum));
            pageNum += 1;
            HttpResponse response = lastfmGet(payload);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        String responseBody = new BasicResponseHandler().handleResponse(response);
                        JSONParser parser = new JSONParser();
                        JSONObject contents = (JSONObject) parser.parse(responseBody);
                        JSONObject trackData = (JSONObject) contents.get("toptracks");
                        JSONArray tracks = (JSONArray) trackData.get("track");
                        Integer numTracks = tracks.size();
                        if (numTracks == 0) {
                            break;
                        }
                        for (Integer i = 0; i < numTracks; i++) {
                            JSONObject newTrack = (JSONObject) tracks.get(i);
                            String trackName = (String) newTrack.get("name");
                            String playCount = (String) newTrack.get("playcount");
                            JSONObject artist = (JSONObject) newTrack.get("artist");
                            String artistName = (String) artist.get("name");
                            String length = (String) newTrack.get("duration");
                            Map<String, String> nextTrack = new HashMap<String, String>();
                            nextTrack.put("Track", trackName);
                            nextTrack.put("Plays", playCount);
                            nextTrack.put("Artist", artistName);
                            nextTrack.put("Duration", length);
                            topTrackPlays.add(i, nextTrack);
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String error = sw.toString();
                        Map<String, String> errorMessage = new HashMap<String, String>();
                        errorMessage.put("!Error parsing JSON string!", error);
                        topTrackPlays.add(0, errorMessage);
                    }
                } else {
                    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
                    Map<String, String> errorMessage = new HashMap<String, String>();
                    errorMessage.put("!Request rejected!, Status Code -", statusCode);
                    topTrackPlays.add(0, errorMessage);
                }
            }
        }
        return topTrackPlays;
    }

    // Returns user's top genres for a given time period.
    // Only uses top 100 tracks for genre data for time
    // Inputs -
    // username - last.fm username
    // range - 'overall', '7day', '1month', '3month', '6month', '12month'
    // number - number of genres to fetch (put 0 to get all genres)
    // Returns -
    // top genres - A hashmap sorted by plays, with key, value pairs as such:
    // - Genre: genre name
    // - Plays: number of plays for said genre
    public static LinkedHashMap<String, Integer> getTopGenres(String username, String range)
            throws IOException, InterruptedException {
        Map<String, String> payload = new HashMap<>();
        HashMap<String, Integer> topGenres = new HashMap<String, Integer>();
        List<Map<String, String>> topTracks = getTopTracks(username, range, 100);
        Integer numTracks = topTracks.size();

        for (Integer i = 0; i < numTracks; i++) {
            Map<String, String> track = topTracks.get(i);
            String artist = track.get("Artist");
            String trackName = track.get("Track");
            //System.out.println(trackName);
            String playsS = track.get("Plays");
            Integer plays = Integer.parseInt(playsS);
            payload.put("method", "track.getTopTags");
            payload.put("track", trackName);
            payload.put("artist", artist);
            HttpResponse response = lastfmGet(payload);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        String responseBody = new BasicResponseHandler().handleResponse(response);
                        JSONParser parser = new JSONParser();
                        JSONObject contents = (JSONObject) parser.parse(responseBody);
                        JSONObject tagTemp = (JSONObject) contents.get("toptags");
                        JSONArray tagData = (JSONArray) tagTemp.get("tag");
                        Integer numTags = tagData.size();
                        // Only get top five tags or less
                        Integer max = 5;
                        for (Integer j = 0; j < numTags && j < max; j++) {
                            JSONObject newTag = (JSONObject) tagData.get(j);
                            String tag = (String) newTag.get("name");
                            Long count = (Long) newTag.get("count");
                            Integer numberTagged = count.intValue();
                            if (numberTagged > 10) {
                                if (topGenres.containsKey(tag)) {
                                    topGenres.put(tag, topGenres.get(tag) + plays);
                                } else {
                                    topGenres.put(tag, plays);
                                }
                            }
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String error = sw.toString();
                        topGenres.put("!Error parsing JSON string! " + error, 0);
                    }
                } else {
                    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
                    topGenres.put("!Request rejected!, Status Code - " + statusCode, 0);
                }
            }
        }
        LinkedHashMap<String, Integer> sortedGenres = sortByValue(topGenres);
        return sortedGenres;
    }

    // Returns user's top albums for a given time period.
    // Inputs -
    // username - last.fm username
    // range - 'overall', '7day', '1month', '3month', '6month', '12month'
    // number - number of albums to fetch (put 0 to get all albums)
    // Returns -
    // top albums - A list of hashmaps, ordered by plays, with key, value pairs as
    // such:
    // - Album: album name
    // - Plays: number of plays for said track
    // - Artist: artist of said track
    public static List<Map<String, String>> getTopAlbums(String username, String range, int number)
            throws IOException, InterruptedException {
        Map<String, String> payload = new HashMap<>();
        Integer pageNum = 1;
        List<Map<String, String>> topAlbums = new ArrayList<Map<String, String>>();
        while (number >= 0) {
            payload.put("method", "user.getTopAlbums");
            payload.put("user", username);
            payload.put("period", range);
            if (number > 1000) {
                payload.put("limit", Integer.toString(1000));
                number -= 1000;
                if (number == 0) {
                    number -= 1;
                }
            } else if (number == 0) {
                payload.put("limit", Integer.toString(1000));
            } else {
                payload.put("limit", Integer.toString(number));
                number = -1;
            }

            payload.put("page", Integer.toString(pageNum));
            pageNum += 1;
            HttpResponse response = lastfmGet(payload);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    try {
                        String responseBody = new BasicResponseHandler().handleResponse(response);
                        JSONParser parser = new JSONParser();
                        JSONObject contents = (JSONObject) parser.parse(responseBody);
                        JSONObject albumData = (JSONObject) contents.get("topalbums");
                        JSONArray albums = (JSONArray) albumData.get("album");
                        Integer numAlbums = albums.size();
                        if (numAlbums == 0) {
                            break;
                        }
                        for (Integer i = 0; i < numAlbums; i++) {
                            JSONObject newAlbum = (JSONObject) albums.get(i);
                            String albumName = (String) newAlbum.get("name");
                            String playCount = (String) newAlbum.get("playcount");
                            JSONObject artist = (JSONObject) newAlbum.get("artist");
                            String artistName = (String) artist.get("name");
                            Map<String, String> nextAlbum = new HashMap<String, String>();
                            nextAlbum.put("Album", albumName);
                            nextAlbum.put("Plays", playCount);
                            nextAlbum.put("Artist", artistName);
                            topAlbums.add(i, nextAlbum);
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String error = sw.toString();
                        Map<String, String> errorMessage = new HashMap<String, String>();
                        errorMessage.put("!Error parsing JSON string!", error);
                        topAlbums.add(0, errorMessage);
                    }
                } else {
                    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
                    Map<String, String> errorMessage = new HashMap<String, String>();
                    errorMessage.put("!Request rejected!, Status Code -", statusCode);
                    topAlbums.add(0, errorMessage);
                }
            }
        }
        return topAlbums;
    }

    // Returns user's listening time.
    // Inputs -
    // username - last.fm username
    // range - 'overall', '7day', '1month', '3month', '6month', '12month'
    // Returns -
    // minutes - minutes listened to for given range
    public static Integer getMinsPlayed(String username, String range) {
        try{
            List<Map<String, String>> tracks = getTopTracks(username, range, 0);
            Integer numTracks = tracks.size();
            Integer minutes = 0;
            for (Integer i = 0; i < numTracks; i++) {
                Map<String, String> entry = tracks.get(i);
                Integer plays = Integer.parseInt(entry.get("Plays"));
                Integer duration = Integer.parseInt(entry.get("Duration"));
                minutes += plays * duration;
            }
            return minutes;
        }
        catch (Exception e) {
            return 0;
        }
    }

    // FOR TESTING ONLY! :)
    /*
    public static void main(String[] args) throws IOException, InterruptedException {
        String user = "anyapop";
        String ran = "3month";
        int num = 10;

        List<Map<String, String>> tracks = getTopTracks(user, ran, num);
        Integer numTracks = tracks.size();
        System.out.println("Track Data");
        for(Integer i = 0; i < numTracks; i++){
            Map<String, String> track = tracks.get(i);
            String name = track.get("Track");
            String plays = track.get("Plays");
            String artist = track.get("Artist");
            System.out.println( i + "- " + name + " , " + plays + " plays by " + artist);
        }
        System.out.println(" ");

        System.out.println("Artist Data");
        List<Map<String, String>> artists = getTopArtistPlays(user, ran, num);
        Integer numA = artists.size();
        for(Integer i = 0; i < numA; i++){
            Map<String, String> artist = artists.get(i);
            String name = artist.get("Artist");
            String plays = artist.get("Plays");
            System.out.println( i + "- " + name + " , " + plays + " plays");
        }
        System.out.println(" ");

        System.out.println("Album Data");
        List<Map<String, String>> albums = getTopAlbums(user, ran, num);
        Integer numAl = albums.size();
        for(Integer i = 0; i < numAl; i++){
            Map<String, String> album = albums.get(i);
            String name = album.get("Album");
            String plays = album.get("Plays");
            String artist = album.get("Artist");
            System.out.println( i + "- " + name + " , " + plays + " plays by " + artist);
        }
        System.out.println(" ");

        System.out.println("Genre Data");
        LinkedHashMap<String, Integer> genres = getTopGenres(user, ran);
        List<String> alKeys = new ArrayList<String>(genres.keySet());
        // reverse order of keys
        Collections.reverse(alKeys);
        // iterate LHM using reverse order of keys
        Integer i = 0;
        for (String strKey : alKeys) {
            i += 1;
            System.out.println("Genre : " + strKey + "\t\t"
                    + "Plays : "
                    + genres.get(strKey));
            if (i == 10){
                break;
            }
        }
        System.out.println(" ");

        System.out.println("Listening Time");
        Integer minutes = getMinsPlayed(user, ran);
        System.out.println(minutes + " minutes listened to.");
    }
    */
}