package com.example.myapplication;

import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class artistModel {
    JsonObject artist_object;
    JsonObject artistObj;
    JsonObject albumObj;
    public artistModel(JsonObject artist_object){
        this.artist_object = artist_object;
        this.artistObj = artist_object.getAsJsonObject("artists");
        this.albumObj = artist_object.getAsJsonObject("albums");
    }
    public String getId(){
        return artistObj.get("id").getAsString();
    }
    public String getName(){
        try {
            return artistObj.get("name").getAsString();
        }
        catch(Exception e){
            return null;
        }

    }
    public String getFollowers(){
        try{
            return artistObj.getAsJsonObject("followers").get("total").getAsString();

        }
        catch(Exception e){
            return null;
        }
    }
    public int getPopularity(){
        try{
            return artistObj.get("popularity").getAsInt();
        }
        catch(Exception e){
            return 0;
        }
    }
    public Uri getSpotifyUrl(){
        try{
            return Uri.parse(artistObj.getAsJsonObject("external_urls").get("spotify").getAsString());
        }
        catch(Exception e){
            return null;
        }

    }
    public Uri getImage(){
        try{
            return Uri.parse(artistObj.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString());
        }
        catch(Exception e){
            return null;
        }
    }
    public ArrayList<Uri> getAlbumUrl(){
        try{
            ArrayList<Uri> urls = new ArrayList<>();
            JsonArray albm_arr = artistObj.getAsJsonArray("items");
            int size = Math.min(albm_arr.size(), 3);
            for (int i = 0; i < size; i++) {
                JsonObject x = albm_arr.get(i).getAsJsonObject();
                String a = x.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
                urls.add(Uri.parse(a));
            }
            return urls;
        }catch(Exception e){
            return null;
        }

    }


}
