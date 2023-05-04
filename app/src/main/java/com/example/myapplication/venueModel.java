package com.example.myapplication;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class venueModel {
    JsonObject venue_object;
    public venueModel(JsonObject venue_object){
        this.venue_object = venue_object;

    }
    public JsonObject getVenue_object(){
        return this.venue_object;
    }
    public String getVenueName(){
        try {
            return venue_object.get("name").getAsString();
        }
        catch(Exception e){
            return null;
        }
    }
    public String getAddress(){
       try {
           ArrayList<String> address = new ArrayList<String>();
           if (venue_object.getAsJsonObject("address").has("line1")) {
               address.add(venue_object.getAsJsonObject("address").get("line1").getAsString());
           }
           if (venue_object.has("city")) {
               address.add(venue_object.getAsJsonObject("city").get("name").getAsString());
           }
           if (venue_object.has("state")) {
               address.add(venue_object.getAsJsonObject("state").get("name").getAsString());
           }
           return String.join(", ", address);
       }
       catch (Exception e){
           return null;
       }
    }
    public String state_city(){
        try {
            ArrayList<String> address = new ArrayList<String>();

            if (venue_object.has("city")) {
                address.add(venue_object.getAsJsonObject("city").get("name").getAsString());
            }
            if (venue_object.has("state")) {
                address.add(venue_object.getAsJsonObject("state").get("name").getAsString());
            }
            return String.join(", ", address);
        }
        catch (Exception e){
            return null;
        }

    }
    public String getPhone(){
        try{
            return venue_object.getAsJsonObject("boxOfficeInfo").get("phoneNumberDetail").getAsString();
        }
        catch(Exception e){
            return null;
        }
    }
    public String openHour(){
        try{
            return venue_object.getAsJsonObject("boxOfficeInfo").get("openHoursDetail").getAsString();
        }
        catch(Exception e){
            return null;
        }
    }
    public String generalRule(){
        try{
            return venue_object.getAsJsonObject("generalInfo").get("generalRule").getAsString();
        }
        catch(Exception e){
            return null;
        }
    }
    public String childRule(){
        try{
            return venue_object.getAsJsonObject("generalInfo").get("childRule").getAsString();
        }
        catch(Exception e){
            return null;
        }
    }

}
