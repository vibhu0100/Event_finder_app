package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
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
    event_RecyclerViewAdapter adapter;

    static Fragment current;

    public tableFragment() {
        current = this;
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
    static public void refresh(){
        current.onResume();
    }
    public void onResume() {
        super.onResume();
//        favList = new ArrayList<eventRowModel>();
        if(adapter!=null){

//            ArrayList<eventRowModel> favList = eventsRow;
//            adapter.refreshData(favList);
            RecyclerView table = view.findViewById(R.id.event_recycler);
            adapter = new event_RecyclerViewAdapter(getContext(),eventsRow,this);
            table.setAdapter(adapter);
            table.setLayoutManager(new LinearLayoutManager(getContext()));
//            if(eventsRow.isEmpty()||eventsRow == null){
//                view.findViewById(R.id.event_recycler).setVisibility(View.GONE);
//                view.findViewById(R.id.no_event_Data).setVisibility(View.VISIBLE);
//            }
//            else{
//                view.findViewById(R.id.event_recycler).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.no_event_Data).setVisibility(View.GONE);
//            }

        }

//        ArrayList favList = getCurrentList();
//        RecyclerView table = view.findViewById(R.id.favor_cycler);
//        event_RecyclerViewAdapter adapter = new event_RecyclerViewAdapter(getContext(),favList,this);
//        table.setAdapter(adapter);
//        table.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.notifyDataSetChanged();

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
        RecyclerView table = view.findViewById(R.id.event_recycler);
        table.setVisibility(View.GONE);
        ProgressBar progressbar = view.findViewById(R.id.progressBartable);
        progressbar.setVisibility(View.VISIBLE);
        view.findViewById(R.id.no_event_Data).setVisibility(View.GONE);



       if(event_search.has("_embedded")){
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   progressbar.setVisibility(View.GONE);
                   table.setVisibility(View.VISIBLE);

               }
           },1000);
            JsonArray event_array = event_search.getAsJsonObject("_embedded").getAsJsonArray("events");
            setupEvents(event_array);
            JsonObject event0 = event_array.get(0).getAsJsonObject();
//        Log.i("asd",event_array.get(0).getAsJsonObject().get("id").getAsString());
//        Log.d("obj",event_array.get(0).getAsString());
            eventRowModel evento = new eventRowModel(event0);
           System.out.println("localTime"+evento.getTime());
//           RecyclerView table = view.findViewById(R.id.event_recycler);
//           event_RecyclerViewAdapter adapter = new event_RecyclerViewAdapter(getContext(),eventsRow,this);
//           table.setAdapter(adapter);
//           table.setLayoutManager(new LinearLayoutManager(getContext()));


//        TextView textView = view.findViewById(R.id.textView2);
//        textView.setText(data);
//            RecyclerView table = view.findViewById(R.id.event_recycler);
//            table.setVisibility(View.VISIBLE);
           view.findViewById(R.id.no_event_Data).setVisibility(View.GONE);
            adapter = new event_RecyclerViewAdapter(getContext(),eventsRow,this);
            table.setAdapter(adapter);
            table.setLayoutManager(new LinearLayoutManager(getContext()));

        }
       else{
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   progressbar.setVisibility(View.GONE);
                   view.findViewById(R.id.no_event_Data).setVisibility(View.VISIBLE);

               }
           },1000);
           view.findViewById(R.id.event_recycler).setVisibility(View.GONE);

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
    public void onStart() {
        super.onStart();
        RecyclerView table = view.findViewById(R.id.event_recycler);
        event_RecyclerViewAdapter adapter = new event_RecyclerViewAdapter(getContext(),eventsRow,this);
        table.setAdapter(adapter);
        table.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void addFavourite(int position, View view) {
        ImageView favoriteButton = (ImageView) view;
        String dataObject = eventsRow.get(position).getEventObject();
        Gson gson = new Gson();
        eventRowModel current_event = eventsRow.get(position);
        JsonObject ob = gson.fromJson(dataObject, JsonObject.class);
        Boolean isFavorites = current_event.isFavourite(getContext());
        Log.d("FAVO OBJ", dataObject);
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);

        // Save the JSON string representation of the data object

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFavorites) {
            // The button is currently selected (heart filled)
            favoriteButton.setImageResource(R.drawable.heart_outline);
            current_event.isFav = false;

            // Add your unfavorite logic here
            Snackbar.make(view, ob.get("name") + "removed from favorites", Snackbar.LENGTH_SHORT).show();
            editor.remove(ob.get("id").getAsString());
            editor.apply();


        } else {
            // The button is currently deselected (heart outline)
            favoriteButton.setImageResource(R.drawable.heart_filled);
            current_event.isFav = true;

            // Add your favorite logic here
            Snackbar.make(view, ob.get("name") + "added to favorites", Snackbar.LENGTH_SHORT).show();
            editor.putString(ob.get("id").getAsString(), current_event.getEventObject());
            System.out.println("editor === "+editor);
            editor.apply();
        }
        refresh();
        FavFragment.refresh();

    }

//    public void addFavourite(int position, View view) {
//        //Bundle bundle  = new Bundle();
//        System.out.println("Favourite Button : "+eventsRow.get(position).getEventName());
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Favourites", getContext().MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        eventRowModel event = eventsRow.get(position);
//        Boolean isFavourite = event.isFavourite(getContext());
//        ImageView favButton = view.findViewById(R.id.favourite);
//
//
//        if(isFavourite){
////            editor.putString(event.getId(),event.getEventObject());
//            favButton.setImageResource(R.drawable.heart_outline);
//            event.isFav = false;
//
//            // Add your unfavorite logic here
//            Snackbar.make(view, event.getEventName() + "removed from favorites", Snackbar.LENGTH_SHORT).show();
//            editor.remove(event.getId());
//            editor.apply();
//
//
//        }
//        else{
//            favButton.setImageResource(R.drawable.heart_filled);
//            event.isFav = true;
//
//            // Add your favorite logic here
//            Snackbar.make(view, event.getEventName() + "added to favorites", Snackbar.LENGTH_SHORT).show();
//            editor.putString(event.getId(), event.getEventObject());
//            System.out.println("editor === "+editor);
//            System.out.println("between your legs");
//            editor.apply();
//        }
//
//
//
//    }




}