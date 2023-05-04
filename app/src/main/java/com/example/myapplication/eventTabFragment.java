package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link eventTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class eventTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public eventTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eventTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static eventTabFragment newInstance(String param1, String param2) {
        eventTabFragment fragment = new eventTabFragment();
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
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_event_tab, container, false);
        Bundle bundle = getArguments();
        String data = bundle.getString("event_data");
        System.out.println("Fragment Dt: "+data);
        setEventDetails(view,data);
        return view;
    }
    public void setEventDetails(View view,String data){
        Picasso picasso  = Picasso.get();
        Gson gson = new Gson();
        JsonObject x = gson.fromJson(data, JsonObject.class);
        eventRowModel event = new eventRowModel(x);
        TextView artist_team = view.findViewById(R.id.artist_teams);
        artist_team.setText(event.getEventName());
        TextView venue = view.findViewById(R.id.venue);
        venue.setText(event.getVenue());
        TextView date = view.findViewById(R.id.event_date);
        date.setText(event.getDates2());
        TextView time = view.findViewById(R.id.event_time);
        time.setText(event.getTime());
        TextView genre = view.findViewById(R.id.genre);
        genre.setText(event.getGenreString());
        TextView url = view.findViewById(R.id.ticket_url);
        url.setText(event.ticket_url().toString());
        TextView status = view.findViewById(R.id.ticket_status);
        status.setText(event.ticket_Status());
        ImageView seat = view.findViewById(R.id.seatMap);
        picasso.load(event.getSeatmapUrl()).resize(200,200).into(seat);
        artist_team.setSingleLine(true);
        artist_team.setSelected(true);
        url.setSingleLine(true);
        url.setSelected(true);








    }
}