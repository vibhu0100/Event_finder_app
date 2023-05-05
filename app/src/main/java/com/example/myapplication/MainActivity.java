package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

//        fuck_me();
//        System.out.println(Arrays.stream(fuck).toArray()+"rtre"+fuck.toString()+"dfs"+String.join(","));
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FirstFragment());
        fragmentList.add(new FavFragment());

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
                    tab.setText("Events");
                    break;
                case 1:
                    tab.setText("Favourites");
                    break;
            }
        });
        tabLayoutMediator.attach();




        //onlick for button which is not here anymore

//        Button search = findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("BUTTONS", "User tapped the Supabutton");
//                searchEvent(v);
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume called");

        // Check for location permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 90009);
        }
    }
    public void clearThings(View view){
        TextView textView = findViewById(R.id.location_label);
        RequestQueue queue = Volley.newRequestQueue(this);
//        EditText keyword = findViewById(R.id.keyword);
        AutoCompleteTextView keyword = findViewById(R.id.autoCompleteKeyword);
        EditText distance = findViewById(R.id.distance);
        EditText location = findViewById(R.id.location);
        Switch auto_detect = findViewById(R.id.autoDetect);
        Spinner category = findViewById(R.id.category);
        keyword.setText("");
        location.setText("");
        category.setSelection(0);
        auto_detect.setChecked(false);
        distance.setText("");



    }
    private void autoDetect(View view,Switch auto_detect){
        EditText location = view.findViewById(R.id.location);
        if(auto_detect.isChecked()){
            location.setVisibility(View.GONE);
        }
        else{
            location.setVisibility(View.VISIBLE);
        }

    }
    public void back_to_search(View view){
        Log.d("info","in back");
        findViewById(R.id.test).setVisibility(View.GONE);
        findViewById(R.id.search_box).setVisibility(View.VISIBLE);
    }

    public void ticket_master(String url,RequestQueue queue){
        StringRequest searchRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                //JSONObject ip_info = new JSONObject(response);

                Log.d("search",response);

//                                JsonObject search_result = gson.fromJson(response, JsonObject.class);
//                                Intent intent = new Intent(MainActivity.this, tableFragment.class);
//                                intent.putExtra("data", response);
                tableFragment nextFrag= new tableFragment();
                Bundle args = new Bundle();
                args.putString("data",response);
                nextFrag.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                fragmentTransaction.hide(FirstFragment);
//                                fragmentTransaction.commit();
                fragmentTransaction.replace(R.id.searchf, nextFrag);
                findViewById(R.id.search_box).setVisibility(View.GONE);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

//                                getSupportFragmentManager().beginTransaction()
//                                        .add(R.id.searchf, nextFrag)
//                                        .addToBackStack(null)
//                                        .commit();
//                                Fragment this_one = getSupportFragmentManager().findFragmentById(R.id.searchf);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .hide(this_one)
//                                        .commit();
//                                startActivity(intent);
//
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(searchRequest);
    }
    public void validator(View view){
//        if(TextUtils.keyword)
    }
    String getCategoryCode(String category){
        Map<String,String> map = new HashMap<>();
        map.put("All","");
        map.put("Music","KZFzniwnSyZfZ7v7nJ");
        map.put("Sports","KZFzniwnSyZfZ7v7nE");
        map.put("Art & Theatre","KZFzniwnSyZfZ7v7na");
        map.put("Film","KZFzniwnSyZfZ7v7nn");
        map.put("Miscellaneous","KZFzniwnSyZfZ7v7n1");

        return map.get(category);

    }
    public void searchEvent(View view){
        validator(view);

        Gson gson = new Gson();

        TextView textView = findViewById(R.id.location_label);
        RequestQueue queue = Volley.newRequestQueue(this);
//        EditText keyword = findViewById(R.id.keyword);
        AutoCompleteTextView keyword = findViewById(R.id.autoCompleteKeyword);
        EditText distance = findViewById(R.id.distance);
        EditText location = findViewById(R.id.location);
        Switch auto_detect = findViewById(R.id.autoDetect);
        Spinner category = findViewById(R.id.category);

        if(TextUtils.isEmpty(keyword.getText()) || (TextUtils.isEmpty(location.getText()) && !auto_detect.isChecked())){
            Snackbar.make(view,"Please fill all fields",Snackbar.LENGTH_SHORT).show();
            return;
        }
//        TextView textView = findViewById(R.id.location_label);
        Log.d("ewws",Boolean.toString(auto_detect.isChecked()));
        if(auto_detect.isChecked()){
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://ipinfo.io?token=27876ba56b3792",new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                        JsonObject ip_info = gson.fromJson(response, JsonObject.class);
                        String search_url = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword="+keyword.getText().toString()+
                                "&distance=" + distance.getText().toString()+
                                "&location=" + location.getText().toString()+
                                "&auto-detect="+Boolean.toString(auto_detect.isChecked())+
                                "&category="+getCategoryCode(category.getSelectedItem().toString())+
                                "&loc="+ ip_info.get("loc").getAsString();
                    System.out.println("searchURL"+search_url);
                    String search_text = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword=usc&distance=&category=&location=los angeles&auto_detect=false&loc=34.0522342,-118.2436849";


                    ticket_master(search_url,queue);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(stringRequest);
        }
        else{
            Log.d("else","in else");
            String geo_url = "https://maps.googleapis.com/maps/api/geocode/json?"+
                    "address="+location.getText()+
                    "&key=AIzaSyBZCcfa_W18-GfeN5Awolet_4RNaLV_Zuw";
            StringRequest geoRequest = new StringRequest(Request.Method.GET,geo_url,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.

                    JsonObject geo_code = gson.fromJson(response, JsonObject.class);
                    JsonArray resultsArray = geo_code.getAsJsonArray("results");
                    JsonObject resultsObject = resultsArray.get(0).getAsJsonObject();
                    JsonObject geometryObject = resultsObject.getAsJsonObject("geometry");
                    JsonObject locationObject = geometryObject.getAsJsonObject("location");

//                    String loca = geo_code.getAsJsonArray("results").get(0).getAsJsonObject().
//                            getAsJsonObject("geometry").get("location").getAsString();
                    Log.d("find",locationObject.get("lat").getAsString());
                    String search_url = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword="+keyword.getText().toString()+
                            "&distance=" + distance.getText().toString()+
                            "&location=" + location.getText().toString()+
                            "&auto-detect="+Boolean.toString(auto_detect.isChecked())+
                            "&category="+getCategoryCode(category.getSelectedItem().toString())+
                            "&loc="+locationObject.get("lat").getAsString()+","+locationObject.get("lng").getAsString();
                    String search_text = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword=usc&distance=&category=&location=los angeles&auto_detect=false&loc=34.0522342,-118.2436849";


                    ticket_master(search_url,queue);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(geoRequest);
        }
    }
}