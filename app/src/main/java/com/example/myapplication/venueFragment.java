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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    venueModel globalVenue;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    double lat = 34.112;
    double lng = -118.23;
    Marker mapPin;
    GoogleMap googleMap;
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



//            geocoding(venue.getAddress());
            String geo_url = "https://maps.googleapis.com/maps/api/geocode/json?"+
                    "address="+address+
                    "&key=AIzaSyBZCcfa_W18-GfeN5Awolet_4RNaLV_Zuw";

            StringRequest geoRequest = new StringRequest(Request.Method.GET, geo_url, new Response.Listener<String>() {
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
                    Log.d("find", locationObject.get("lat").getAsString());
                    lat = Double.parseDouble(locationObject.get("lat").getAsString());
                   lng = Double.parseDouble(locationObject.get("lng").getAsString());
                   updateLatLongDynamic();



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
        globalVenue = venue;
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
            TextView f = view.findViewById(R.id.venue_name_q);
            f.setText("Name");
            name.setSelected(true);
            name.setSingleLine(true);


        }
        if(venue.getAddress() != null){
            TextView add = view.findViewById(R.id.address);
            add.setText(venue.getAddress());
            TextView f = view.findViewById(R.id.address_q);
            f.setText("Address");
            add.setSelected(true);
            add.setSingleLine(true);
        }
        if(venue.state_city() != null){
            TextView city_state = view.findViewById(R.id.city_state);
            city_state.setText(venue.state_city());
            TextView f = view.findViewById(R.id.city_q);
            f.setText("City/State");
            city_state.setSelected(true);
            city_state.setSingleLine(true);
        }
        if(venue.getPhone() != null){
            TextView phone = view.findViewById(R.id.contact);
            phone.setText(venue.getPhone());
            TextView f = view.findViewById(R.id.phone_q);
            f.setText("Contact Info");
        }

            if (venue.generalRule() != null) {
                TextView general = view.findViewById(R.id.general_rule);
                general.setText(venue.generalRule());
                TextView f = view.findViewById(R.id.generalh);
                f.setText("General Rule");
            }
            if (venue.openHour() != null) {
                TextView open_hour = view.findViewById(R.id.open_hour);
                open_hour.setText(venue.openHour());
                TextView f = view.findViewById(R.id.openh);
                f.setText("Open Hours");
            }
            if (venue.childRule() != null) {
                TextView child = view.findViewById(R.id.child_rule);
                child.setText(venue.childRule());
                TextView f = view.findViewById(R.id.childh);
                f.setText("Child Rule");
            }



    }
    private void updateLatLongDynamic() {
        if (googleMap != null) {
            LatLng newLatLng = new LatLng(lat, lng);

            if (mapPin != null) {
                // Remove the existing marker
                mapPin.remove();
            }

            // Add a marker to the new location
            MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).title("Dropped Pin");
            mapPin = googleMap.addMarker(markerOptions);

            // Move and animate the camera to the new marker
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f));
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
        googleMap = map;
//        geocoding();
//        System.out.println("lat_long"+lat_long[0] + lat_long[0]);
//        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat_long[0]), Double.parseDouble(lat_long[1]))).title("Marker"));
    updateLatLongDynamic();
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