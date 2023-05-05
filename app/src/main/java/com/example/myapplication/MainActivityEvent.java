package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
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
        FacebookSdk.setApplicationId("1297719784153389");
        FacebookSdk.setClientToken("2d2c089954f3073f3b15ca0ce11e5cc0");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());



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
        View customView = LayoutInflater.from(this).inflate(R.layout.event_icon, null);
        View artistView = LayoutInflater.from(this).inflate(R.layout.artist_icon, null);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
//                    tab.setText("Event");
                    tab.setText("EVENT");
//                    customView.findViewById()
                    break;
                case 1:
                    tab.setText("ARTIST");
                    break;
                case 2:
                    tab.setText("Venue");
                    break;
            }
        });
        tabLayoutMediator.attach();
        Intent intent = getIntent();

        ImageView icon = findViewById(R.id.icon2);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon3 = findViewById(R.id.icon3);

        icon1.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.green), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
        icon3.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        icon1.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.green), PorterDuff.Mode.SRC_IN);
                        icon.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        icon3.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        icon1.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        icon.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.green), PorterDuff.Mode.SRC_IN);
                        icon3.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        icon1.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        icon.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.white), PorterDuff.Mode.SRC_IN);
                        icon3.setColorFilter(ContextCompat.getColor(MainActivityEvent.this, R.color.green), PorterDuff.Mode.SRC_IN);
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String data = intent.getStringExtra("event_data");
        System.out.println("Event_data"+data);
        Gson gson = new Gson();
        TabLayout tablayout = findViewById(R.id.tabLayoutEvent);
        ProgressBar progressBar = findViewById(R.id.progress_bar_event);
        ViewPager2 viewPager = findViewById(R.id.viewPagerEvent);
        tablayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tablayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        },1000);
        findViewById(R.id.back_button_press).setOnClickListener(new View.OnClickListener() {
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


        ImageView b = findViewById(R.id.favouriteIcon);
        updateFavButton(b,event.isFavourite(this));
        ImageView f = findViewById(R.id.facebook);
        ImageView t = findViewById(R.id.twitter);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = event.ticket_url().toString(); // Replace with your desired URL

                try {
                    // Create a Twitter intent with the URL
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://tweet?text=" + Uri.encode(url)));

                    // Check if the Twitter app is installed on the device
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Open the Twitter app
                        startActivity(intent);
                    } else {
                        // If the Twitter app is not installed, open the URL in a browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=" + Uri.encode(url)));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnFacebook(event.ticket_url().toString());
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavourite(event, view);

            }
        });


//
        venue_api(event,fragmentList);
//



    }
    private void updateFavButton(ImageView favButton,Boolean isFavourite){
        if(isFavourite){
            favButton.setImageResource(R.drawable.heart_filled);
        }
        else{favButton.setImageResource(R.drawable.heart_outline);}
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

    public void addFavourite(eventRowModel event, View view) {
        ImageView favoriteButton = (ImageView) view;
//        String dataObject = eventsRow.get(position).getEventObject();
        Gson gson = new Gson();
//        eventRowModel current_event = eventsRow.get(position);
        JsonObject ob = gson.fromJson(event.getEventObject(), JsonObject.class);
        Boolean isFavorites = event.isFavourite(this);
//        Log.d("FAVO OBJ", dataObject);
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);

        // Save the JSON string representation of the data object
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFavorites) {
            // The button is currently selected (heart filled)
            favoriteButton.setImageResource(R.drawable.heart_outline);
            event.isFav = false;

            // Add your unfavorite logic here
            Snackbar.make(view, ob.get("name") + "removed from favorites", Snackbar.LENGTH_SHORT).show();
            editor.remove(ob.get("id").getAsString());
            editor.apply();


        } else {
            // The button is currently deselected (heart outline)
            favoriteButton.setImageResource(R.drawable.heart_filled);
            event.isFav = true;

            // Add your favorite logic here
            Snackbar.make(view, ob.get("name") + "added to favorites", Snackbar.LENGTH_SHORT).show();
            editor.putString(ob.get("id").getAsString(), event.getEventObject());
            System.out.println("editor === "+editor);
            System.out.println("between your legs");
            editor.apply();
        }


    }

    private void shareOnFacebook(String url) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(url))
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

    }
    public void show_more(View view) {
        System.out.println("In show more");
        TextView textView = (TextView) view;
        int maxLinesExpanded = 100;
        int maxLinesCollapsed = 3;

        if (textView.getMaxLines() == maxLinesCollapsed) {
            textView.setMaxLines(maxLinesExpanded);
        } else {
            textView.setMaxLines(maxLinesCollapsed);
        }
    }

    }