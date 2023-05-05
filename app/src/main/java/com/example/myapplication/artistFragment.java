package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link artistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class artistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Gson gson = new Gson();
    ArrayList <artistModel> artistArray = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public artistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment artistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static artistFragment newInstance(String param1, String param2) {
        artistFragment fragment = new artistFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null && bundle.containsKey("artist_data")) {
            System.out.println("in artist fragment " + bundle);
            String data = bundle.getString("artist_data");
            String arr = bundle.getString("artist_array");
            JsonObject tryme = gson.fromJson(arr, JsonObject.class);
            eventRowModel eventObj = new eventRowModel(tryme);
            ArrayList<String> a = eventObj.generateArtistsArray();

            System.out.println("Artist Arrya" + arr);
            JsonObject x = gson.fromJson(data, JsonObject.class);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa" + a);
            JsonArray artist_items = gson.fromJson(x.getAsJsonArray("items"), JsonArray.class);
            System.out.println("arrayyyyy_items" + "sixe" + artist_items.size() + x.getAsJsonArray("items"));
            Map<String, artistModel> map = new HashMap<String, artistModel>();


            JsonArray testing = new JsonArray();

            for (String item : a) {
                for (JsonElement element : artist_items) {
                    JSONArray keys;
                    JsonObject obj = element.getAsJsonObject();

                    try {
                        JSONObject obj1 = new JSONObject(obj.toString());
                        keys = obj1.names();
                        artistModel j = new artistModel(obj.getAsJsonObject(keys.getString(0)));
                        if (item.contains(j.getName())) {
                            artistArray.add(j);
                        }

                    } catch (JSONException e) {
                        System.out.println("errorrrr");
                    }
//                artistModel j = new artistModel(obj);

                }

            }

            System.out.println("Artist_Arrayyyyyyyyyyyyyyy" + artistArray);
            RecyclerView table = view.findViewById(R.id.artist_cycler);
            table.setVisibility(View.VISIBLE);
            view.findViewById(R.id.no_artist_Data).setVisibility(View.GONE);
            artist_RecyclerViewAdapter adapter = new artist_RecyclerViewAdapter(artistArray, getContext());
            table.setAdapter(adapter);
            table.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else{
            view.findViewById(R.id.no_artist_Data).setVisibility(View.VISIBLE);
            view.findViewById(R.id.artist_cycler).setVisibility(View.GONE);
        }



        return view;
    }
    public void setDetailView(View view){
        TextView event_name = view.findViewById(R.id.artist_teams);
    }
}