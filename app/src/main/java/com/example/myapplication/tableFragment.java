package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tableFragment extends Fragment implements recyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    public tableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static tableFragment newInstance(String param1, String param2) {
        tableFragment fragment = new tableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ArrayList<eventRowModel> eventsRow = new ArrayList<>();
    Gson gson = new Gson();
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
        Gson gson = new Gson();
        view =  inflater.inflate(R.layout.fragment_table, container, false);
        Log.d("frag","in table frag");
        String data = getArguments().getString("data");
        Log.d("data",data);
        JsonObject event_search = gson.fromJson(data,JsonObject.class);
       if(event_search.has("_embedded")){
            JsonArray event_array = event_search.getAsJsonObject("_embedded").getAsJsonArray("events");
            setupEvents(event_array);
            JsonObject event0 = event_array.get(0).getAsJsonObject();
//        Log.i("asd",event_array.get(0).getAsJsonObject().get("id").getAsString());
//        Log.d("obj",event_array.get(0).getAsString());
            eventRowModel evento = new eventRowModel(event0);
           System.out.println("localTime"+evento.getTime());


//        TextView textView = view.findViewById(R.id.textView2);
//        textView.setText(data);
            RecyclerView table = view.findViewById(R.id.event_recycler);
            event_RecyclerViewAdapter adapter = new event_RecyclerViewAdapter(getContext(),eventsRow,this);
            table.setAdapter(adapter);
            table.setLayoutManager(new LinearLayoutManager(getContext()));

        }



        return view;
    }
    private void setupEvents(JsonArray event_array){
        for (JsonElement event:event_array){
            JsonObject e = event.getAsJsonObject();
            eventsRow.add(new eventRowModel(e));

        }
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle  = new Bundle();
        MainActivityEvent event_frag = new MainActivityEvent();
        String event_str = eventsRow.get(position).getEventObject();
        Intent intent = new Intent(getActivity(), MainActivityEvent.class);
        intent.putExtra("event_data",event_str);
        startActivity(intent);


    }

    @Override
    public void addFavourite(int position) {
        //Bundle bundle  = new Bundle();
        System.out.println("Favourite Button : "+eventsRow.get(position).getEventName());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        eventRowModel event = eventsRow.get(position);


        if(event.isFav){
//            editor.putString(event.getId(),event.getEventObject());
            event.isFav = false;
            editor.remove(event.getId());


        }
        else{
            event.isFav = true;
            editor.putString(event.getId(),event.getEventObject());
        }
        editor.apply();


    }


}