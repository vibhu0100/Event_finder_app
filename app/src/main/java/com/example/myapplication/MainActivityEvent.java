package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivityEvent extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    Gson gson = new Gson();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        viewPager = findViewById(R.id.viewPagerEvent);
        tabLayout = findViewById(R.id.tabLayoutEvent);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new eventTabFragment());
        fragmentList.add(new artistFragment());
        fragmentList.add(new venueFragment());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(fragmentManager, getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(fragmentStateAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Event");
                    break;
                case 1:
                    tab.setText("Artist");
                    break;
                case 2:
                    tab.setText("Venue");
                    break;
            }
        });
        tabLayoutMediator.attach();
        Intent intent = getIntent();

        String data = intent.getStringExtra("event_data");
        System.out.println("Event_data"+data);
        Gson gson = new Gson();
        findViewById(R.id.eventBarC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        JsonObject x= gson.fromJson(data, JsonObject.class);
        eventRowModel event = new eventRowModel(x);
        TextView bar = findViewById(R.id.final_bar);
        bar.setText(event.getEventName());
        bar.setSelected(true);
        bar.setSingleLine(true);


        eventTabFragment artFrag = new eventTabFragment();
        artistFragment artFrag2 = new artistFragment();
        Bundle bundle = new Bundle();
        bundle.putString("event_data",data);
        artFrag.setArguments(bundle);
        fragmentList.set(0,artFrag);
        System.out.println("segment name"+event.segmentName());
        if(event.segmentName().contains("Music")){
            artist_api(event,fragmentList);
        }

//
        venue_api(event,fragmentList);
//



    }

    private void artist_api(eventRowModel event, List<Fragment> fragmentList){
        ArrayList<String> array = event.generateArtistsArray();
        System.out.println("arryaaaaaaa"+array);
        String url = "https://node-for-android-ticket.wl.r.appspot.com/callback?"+ event.getArtistReqURL();
        String test = "https://node-for-android-ticket.wl.r.appspot.com/callback?artists=P!NK,Brandi%20Carlile,Grouplove,KidCutUp";
        System.out.println("fuck url"+url);
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("venue_api "+ response);

                        JsonObject x = gson.fromJson(response,JsonObject.class);

                        JsonArray artist_items = gson.fromJson(x.getAsJsonArray("items"),JsonArray.class);

                        System.out.println("Artist_map "+artist_items.size());
                        artistFragment artTabFrag = new artistFragment();
                        bundle.putString("artist_data",response);
                        System.out.println("artist_data"+bundle);
                        bundle.putString("artist_array",event.getEventObject());
                        artTabFrag.setArguments(bundle);
                        fragmentList.set(1,artTabFrag);







//                        if(x.has("_embedded")) {
//                            JsonObject venue = x.getAsJsonObject("_embedded").getAsJsonArray("venues").get(0).getAsJsonObject();
//                            System.out.println("venue "+venue);
//                            venueModel venueObj = new venueModel(x);
//                            Bundle bundle = new Bundle();
//                            venueFragment venueFrag= new venueFragment();
//                            bundle.putString("venue_data",venue.toString());
//                            venueFrag.setArguments(bundle);
//                            fragmentList.set(2,venueFrag);
//
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
                System.out.println("artist_api error");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


// Request a string response from the provided URL.


// Add the request to the RequestQueue.
    }

    private void venue_api(eventRowModel event,List<Fragment> fragmentList){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://node-vibhuti123.wl.r.appspot.com/event_details?keyword="+event.getVenue();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JsonObject x = gson.fromJson(response,JsonObject.class);
                        if(x.has("_embedded")) {
                            JsonObject venue = x.getAsJsonObject("_embedded").getAsJsonArray("venues").get(0).getAsJsonObject();
                            System.out.println("venue "+venue);
                            venueModel venueObj = new venueModel(x);
                            Bundle bundle = new Bundle();
                            venueFragment venueFrag= new venueFragment();
                            bundle.putString("venue_data",venue.toString());
                            venueFrag.setArguments(bundle);
                            fragmentList.set(2,venueFrag);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}