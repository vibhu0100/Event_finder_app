package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

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
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavFragment extends Fragment implements recyclerViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
//    ArrayList<eventRowModel> favList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    event_RecyclerViewAdapter adapter;

    static Fragment current;
    public FavFragment() {
        // Required empty public constructor
        current = this;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    static public void refresh(){
        current.onResume();
    }

    ArrayList<eventRowModel> getCurrentList(){
        ArrayList favList = new ArrayList<eventRowModel>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        Gson gson = new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String value = entry.getValue().toString();
            JsonObject fav_obj = gson.fromJson(value,JsonObject.class);
            eventRowModel fav = new eventRowModel(fav_obj);
            favList.add(fav);
        }
        return favList;
    }
    @Override
    public void onResume() {
        super.onResume();
//        favList = new ArrayList<eventRowModel>();
        if(adapter!=null){
            ArrayList<eventRowModel> favList = getCurrentList();
            adapter.refreshData(favList);
            System.out.println("");
            if(favList.isEmpty()||favList == null){
//                view.findViewById(R.id.fav_cycler).setVisibility(View.GONE);
                view.findViewById(R.id.no_fav_Data).setVisibility(View.VISIBLE);
            }
            else{
//                view.findViewById(R.id.fav_cycler).setVisibility(View.VISIBLE);
                view.findViewById(R.id.no_fav_Data).setVisibility(View.GONE);
            }

        }

//        ArrayList favList = getCurrentList();
//        RecyclerView table = view.findViewById(R.id.favor_cycler);
//        event_RecyclerViewAdapter adapter = new event_RecyclerViewAdapter(getContext(),favList,this);
//        table.setAdapter(adapter);
//        table.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter.notifyDataSetChanged();

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
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        ArrayList favList = getCurrentList();
        RecyclerView table = view.findViewById(R.id.favor_cycler);
        adapter = new event_RecyclerViewAdapter(getContext(),favList,this);
        table.setAdapter(adapter);
        table.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }

    @Override
    public void onItemClick(int position) {
        ArrayList<eventRowModel> favList = getCurrentList();
        Bundle bundle  = new Bundle();
        MainActivityEvent event_frag = new MainActivityEvent();
        String event_str = favList.get(position).getEventObject();
        Intent intent = new Intent(getActivity(), MainActivityEvent.class);
        intent.putExtra("event_data",event_str);
        startActivity(intent);

    }

    @Override
    public void addFavourite(int position, View view) {
        ArrayList<eventRowModel> favList = getCurrentList();
        ImageView favoriteButton = (ImageView) view;
        String dataObject = favList.get(position).getEventObject();
        Gson gson = new Gson();
        eventRowModel current_event = favList.get(position);
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
        tableFragment.refresh();


    }
}