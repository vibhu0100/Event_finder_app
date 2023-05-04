package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link venueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class venueFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    public String[] lat_long = {"0","0"};
    public venueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment venueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static venueFragment newInstance(String param1, String param2) {
        venueFragment fragment = new venueFragment();
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
    private void geocoding(String address){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Gson gson = new Gson();

        String geo_url = "https://maps.googleapis.com/maps/api/geocode/json?"+
                "address="+address+
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
                lat_long[0] = locationObject.get("lat").getAsString();
                lat_long[1] = locationObject.get("lng").getAsString();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(geoRequest);
//        return lat_long;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        Gson gson = new Gson();
        Bundle bundle = getArguments();
        String data = bundle.getString("venue_data");
        System.out.println("VenueData in "+data);
        JsonObject x = gson.fromJson(data,JsonObject.class);
        venueModel venue = new venueModel(x);
        if(venue.getAddress()!=null) {
            geocoding(venue.getAddress());
        }
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) view.findViewById(R.id.user_list_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        setUpVenueCard(view,venue);


        return view;
    }
    public void setUpVenueCard(View view,venueModel venue){
        if(venue.getVenueName() != null){
            TextView name = view.findViewById(R.id.venue_name);
            name.setText(venue.getVenueName());
            name.setSelected(true);
            name.setSingleLine(true);


        }
        if(venue.getAddress() != null){
            TextView add = view.findViewById(R.id.address);
            add.setText(venue.getAddress());
            add.setSelected(true);
            add.setSingleLine(true);
        }
        if(venue.state_city() != null){
            TextView city_state = view.findViewById(R.id.city_state);
            city_state.setText(venue.state_city());
            city_state.setSelected(true);
            city_state.setSingleLine(true);
        }
        if(venue.getPhone() != null){
            TextView phone = view.findViewById(R.id.contact);
            phone.setText(venue.getPhone());
        }
        if(venue.generalRule() != null){
            TextView general = view.findViewById(R.id.general_rule);
            general.setText(venue.generalRule());
        }
        if(venue.openHour() != null){
            TextView open_hour = view.findViewById(R.id.open_hour);
            open_hour.setText(venue.openHour());
        }
        if(venue.childRule() != null){
            TextView child = view.findViewById(R.id.child_rule);
            child.setText(venue.childRule());
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        System.out.println("lat_long"+lat_long[0] + lat_long[0]);
        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat_long[0]), Double.parseDouble(lat_long[1]))).title("Marker"));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}