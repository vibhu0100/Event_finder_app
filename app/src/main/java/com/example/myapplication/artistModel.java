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
        this.albumObj = artist_object.getAsJsonObject("album");
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
            float x = Float.parseFloat(artistObj.getAsJsonObject("followers").get("total").getAsString());
            return truncateNumber(x);

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
    public String truncateNumber(float floatNumber) {
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        long number = Math.round(floatNumber);
        if ((number >= million) && (number < billion)) {
            float fraction = calculateFraction(number, million);
            return Float.toString(fraction) + "M";
        } else if ((number >= billion) && (number < trillion)) {
            float fraction = calculateFraction(number, billion);
            return Float.toString(fraction) + "B";
        }
        return Long.toString(number);
    }

    public float calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        float fraction = (float) truncate * 0.10F;
        return fraction;
    }
    public ArrayList<Uri> getAlbumUrl() {

        ArrayList<Uri> urls = new ArrayList<>();
        JsonArray albm_arr = albumObj.getAsJsonArray("items");
        System.out.println("image_url : "+albumObj.getAsJsonArray("items").size());
        int size = Math.min(albm_arr.size(), 3);
        for (int i = 0; i < size; i++) {
            JsonObject x = albm_arr.get(i).getAsJsonObject();
            String a = x.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
            urls.add(Uri.parse(a));



        }
        return urls;
    }


}
