package com.example.myapplication;

import com.google.gson.JsonObject;

public class eventRowModel {
    String eventName;
    String venue;
    String genre;
    String date;
    String time;
    String imageUrl;
    JsonObject event_search;



    public eventRowModel(JsonObject event_search) {
        this.event_search = event_search;
    }

    public String getEventName() {

        return eventName;
    }

    public String getVenue() {
        return venue;
    }

    public String getGenre() {
        return genre;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
