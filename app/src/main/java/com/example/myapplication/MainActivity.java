package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

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
    public void searchEvent(View view){
        Gson gson = new Gson();

        TextView textView = findViewById(R.id.location_label);
        textView.setText("That didn't work beforesds!");
        RequestQueue queue = Volley.newRequestQueue(this);
        EditText keyword = findViewById(R.id.keyword);
        EditText distance = findViewById(R.id.distance);
        EditText location = findViewById(R.id.location);
        Switch auto_detect = findViewById(R.id.autoDetect);
//        TextView textView = findViewById(R.id.location_label);
        Log.d("ewws",Boolean.toString(auto_detect.isChecked()));
        if(auto_detect.isChecked()){
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://ipinfo.io?token=27876ba56b3792",new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.

                        JsonObject ip_info = gson.fromJson(response, JsonObject.class);
                        String search_url = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword="+keyword.getText().toString()+
                                "&distance=" + distance.getText().toString()+
                                "&location=" + location.getText().toString()+
                                "&auto-detect="+Boolean.toString(auto_detect.isChecked())+
                                "&loc="+ ip_info.get("loc").getAsString();
                        StringRequest searchRequest = new StringRequest(Request.Method.GET,search_url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                //JSONObject ip_info = new JSONObject(response);

                                Log.d("search",response);
                                textView.setText("Response is: " + response);
//                                JsonObject search_result = gson.fromJson(response, JsonObject.class);
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                intent.putExtra("data", response);
                                startActivity(intent);
//
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                textView.setText("That didn't work!");
                            }
                        });
                        queue.add(searchRequest);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText("That didn't work!");
                }
            });
            queue.add(stringRequest);
        }
    }
}