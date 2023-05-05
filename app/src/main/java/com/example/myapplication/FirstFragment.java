package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment implements TextWatcher {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }
    public void searchEvent(View view){
        Gson gson = new Gson();

        TextView textView = view.findViewById(R.id.location_label);
        textView.setText("That didn't work in fragment!");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        EditText keyword = view.findViewById(R.id.keyword);
        EditText distance = view.findViewById(R.id.distance);
        EditText location = view.findViewById(R.id.location);
        Switch auto_detect = view.findViewById(R.id.autoDetect);


//        TextView textView = view.findViewById(R.id.location_label);
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
                    String search_text = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword=usc&distance=&category=&location=los angeles&auto_detect=false&loc=34.0522342,-118.2436849";
                    StringRequest searchRequest = new StringRequest(Request.Method.GET,search_text,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //JSONObject ip_info = new JSONObject(response);

                            Log.d("search",response);

                            //textView.setText("Response is: " + response);
                            JsonObject search_result = gson.fromJson(response, JsonObject.class);
                            Intent intent = new Intent(getContext(), tableFragment.class);
                            intent.putExtra("data", response);
                            tableFragment nextFrag= new tableFragment();
                            Fragment this_one = getActivity().getSupportFragmentManager().findFragmentById(R.id.searchf);
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .hide(this_one)
                                    .commit();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .remove(FirstFragment.this)
                                    .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
//                                getView().setVisibility(View.GONE);
//                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            textView.setText("That didn't work!");
                        }
                    });
                    queue.add(searchRequest);


                    textView.setText("Response is: " + response);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        Button search = view.findViewById(R.id.search);
        Spinner category = view.findViewById(R.id.category);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(requireContext(),R.array.spinner_value,R.layout.spinner_item);
        category.setAdapter(spinner_adapter);
        Switch auto_detect = view.findViewById(R.id.autoDetect);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteKeyword);
        autoCompleteTextView.addTextChangedListener(this);
        progressBar = view.findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(getContext());
        Picasso picasso = Picasso.get();
        Uri image_uri = Uri.parse("https://s1.ticketm.net/dam/a/7d5/97b67038-f926-4676-be88-ebf94cb5c7d5_1802151_TABLET_LANDSCAPE_3_2.jpg");
//        ImageView imageView = view.findViewById(R.id.imageView);
//        picasso.load(image_uri).into(imageView);
        auto_detect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoDetect(view,auto_detect);
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });
//        search.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View v) {
////                Log.d("BUTTONS", "User tapped the Supabutton");
//////                searchEvent(v);
////            }
////        });
        return view;

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    private void populateAutoCompleteTextView(List<String> data) {
        Log.d("POPULATE", data.toString());
        adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item_layout, data);
        autoCompleteTextView.setAdapter(adapter);
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Gson gson = new Gson();
        String url = "https://node-vibhuti123.wl.r.appspot.com/suggest?keyword=" + s.toString();


        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the API response
//                        String[] data = parseApiResponse(response);
                        JsonObject suggest_response = gson.fromJson(response, JsonObject.class);
                        if(suggest_response.has("_embedded")){
                            JsonObject embeddedObj = suggest_response.get("_embedded").getAsJsonObject();
                            JsonArray attractionsArray  = embeddedObj.getAsJsonArray("attractions");
                            List<String> attractionNames = new ArrayList<>();
                            for (JsonElement element : attractionsArray) {
                                JsonObject attractionObj = element.getAsJsonObject();

                                // Extract the desired keys from the attractionObj
                                String name = attractionObj.get("name").getAsString();

                                // Add the extracted values to the respective lists
                                attractionNames.add(name);

                            }
                            populateAutoCompleteTextView(attractionNames);
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle API request error
                         progressBar.setVisibility(View.GONE);
                    }
                });

        requestQueue.add(stringRequest);

    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}