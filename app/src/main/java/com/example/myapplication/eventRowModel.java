package com.example.myapplication;

import android.net.Uri;
import android.widget.Switch;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class eventRowModel {
    String eventName;
    JsonArray classifications;
    JsonArray dates;
    JsonObject event_object;
    String id;
    public Boolean music_flag;
    public Boolean isFav;


    public eventRowModel(JsonObject event_object) {

        this.event_object = event_object;
        this.id = event_object.get("id").getAsString();
        this.music_flag = false;
        this.isFav=false;
    }
    public Boolean isFavourite(){

        return false;
    }



    public String getEventName() {

        return event_object.get("name").getAsString();
    }
    public String getEventObject(){
        return event_object.toString();

    }

    public JsonObject getDates() {
        return event_object.getAsJsonObject("dates").getAsJsonObject("start");

    }
    public String getDates2() {
        return event_object.getAsJsonObject("dates").getAsJsonObject("start").get("localDate").getAsString();

    }
    public String getTime() {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
            String time = event_object.getAsJsonObject("dates").getAsJsonObject("start").get("localTime").getAsString();
            Date date = inFormat.parse(time);
            String time12 = outFormat.format(date);
            return time12;
        }
        catch (Exception e)
        {return null;}
    }

    public JsonObject getClassifications() {
        return event_object.getAsJsonArray("classifications").get(0).getAsJsonObject();

    }

    public Uri getSeatmapUrl() {

        return Uri.parse(event_object.getAsJsonObject("seatmap").get("staticUrl").getAsString());
    }

    public String getVenue() {
        try{
        return event_object.getAsJsonObject("_embedded").getAsJsonArray("venues").get(0).getAsJsonObject().get("name").getAsString();
    }catch(Exception e){
            return null;
        }
    }

    public Uri imageUrl(){

        String url = event_object.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
        return Uri.parse(url);

    }
    public String getId(){
        return id;
    }
//    public String artist_array(){
//
//    }
    public String ticket_Status(){
        String status = event_object.getAsJsonObject("dates").getAsJsonObject("status").get("code").getAsString();
        return status;
    }
    public Uri ticket_url(){
        return Uri.parse(event_object.get("url").getAsString());
    }
    public String getPriceRange(){
        try {
            JsonArray jsonArray = event_object.getAsJsonArray("priceRanges");
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            String currency = jsonObject.get("currency").getAsString();
            int min = jsonObject.get("min").getAsInt();
            int max = jsonObject.get("max").getAsInt();

            return String.format("%d - %d (%s)", min, max, currency);
        }catch (Exception e) {
            return null;
        }
    }
    public Boolean getMusic_flag(){
        if(segmentName() == "Music"){
            return true;
        }
        return false;
    }
    public String segmentName(){
        return getClassifications().getAsJsonObject("segment").get("name").getAsString();
    }

    public String getGenreString(){
        try {
            JsonObject classiObj = event_object.getAsJsonArray("classifications").get(0).getAsJsonObject();

            JsonObject json = classiObj;
            String[] fields = {"segment", "genre", "subGenre", "type", "subType"};
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;

            for (String field : fields) {
                if (json.has(field)) {
                    JsonObject obj = json.getAsJsonObject(field);
                    if (obj.has("name") && !"Undefined".equals(obj.get("name").getAsString())) {
                        if (!isFirst) {
                            sb.append(" | ");
                        }
                        String tempStringName = obj.get("name").getAsString();
                        if(tempStringName == "Music"){
                            this.music_flag = true;
                        }
                        sb.append(tempStringName);
                        isFirst = false;
                    }
                }
            }

            String Genres = sb.toString();
            String oneGenre = json.getAsJsonObject("segment").get("name").getAsString();


            return Genres;
        }catch (Exception e) {
            return null;
        }
    }
    public String getArtistTeams(){
        try {
            return String.join("| ", String.join("| ", generateArtistsArray()));
        }
        catch(Exception e){
            return null;
        }
    }
    public ArrayList<String> generateArtistsArray(){
        try {
            JsonArray jsonArray = event_object.getAsJsonObject("_embedded").getAsJsonArray("attractions");

            String output = "";
            ArrayList<String> artist_array = new ArrayList<String>();
//            String[] artist_array = {};

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String name = jsonObject.get("name").toString();
                if (name != null && !name.equalsIgnoreCase("undefined")) {

                    output += name + " | ";
                    artist_array.add(name);
                }
            }

            return artist_array;
        } catch (Exception e) {
            return null;
        }
    }
    public String getArtistReqURL(){
        ArrayList<String> fuck = generateArtistsArray();
//        String[] fuck = {};
            StringBuilder sb = new StringBuilder();
            for (String artist : fuck) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append("artists=").append(artist.replaceAll("\\s+", "").toLowerCase());
            }

            String result = sb.toString();
            System.out.println("dsadaaaa"+result);
            return result;



    }




}
