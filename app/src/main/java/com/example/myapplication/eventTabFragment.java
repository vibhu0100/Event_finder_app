package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

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
    void setStatus(CardView card, TextView ticket_status,String status){
        if(status.equals( "onsale")){
            card.setCardBackgroundColor(Color.parseColor("#008000"));
            ticket_status.setText("On Sale");
        } else if (status.equals("offsale")) {
            card.setCardBackgroundColor(Color.parseColor("#ff0000"));
            ticket_status.setText("Off Sale");
        }
        else if (status.equals("postponed")) {
            card.setCardBackgroundColor(Color.parseColor("#ffa500"));
            ticket_status.setText("Postponed");

        }
        else if (status == "rescheduled") {
            card.setCardBackgroundColor(Color.parseColor("#ffa500"));
            ticket_status.setText("Rescheduled");

        }
        else if (status == "canceled") {
            card.setCardBackgroundColor(Color.parseColor("#000000"));
            ticket_status.setText("Canceled");

        }
    }
    public void setEventDetails(View view,String data){
//        Map<String, String> status = new HashMap<>();
//        status.put()

        Picasso picasso  = Picasso.get();
        Gson gson = new Gson();
        JsonObject x = gson.fromJson(data, JsonObject.class);
        eventRowModel event = new eventRowModel(x);
        TextView artist_team = view.findViewById(R.id.artist_teams);
        if(event.getEventName()!=null){
            TextView f = view.findViewById(R.id.artistq);
            f.setText("Artist/Teams");
            artist_team.setText(event.getEventName());
        }
        if(event.getVenue()!=null) {
            TextView venue = view.findViewById(R.id.venue);
            venue.setText(event.getVenue());
            TextView f = view.findViewById(R.id.venueq);
            f.setText("Venue");
        }
        if(event.getDates2()!=null) {
            TextView date = view.findViewById(R.id.event_date);
            date.setText(event.getDates2());
            TextView f = view.findViewById(R.id.dateq);
            f.setText("Date");
        }
        if(event.getTime()!=null) {
            TextView time = view.findViewById(R.id.event_time);
            time.setText(event.getTime());
            TextView f = view.findViewById(R.id.timeq);
            f.setText("Time");
        }
        if(event.getGenreString()!=null) {
            TextView genre = view.findViewById(R.id.genre);
            genre.setText(event.getGenreString());
            TextView f = view.findViewById(R.id.genreq);
            f.setText("Genre");
        }
        if(event.ticket_url()!=null) {
            Uri Temp1 = event.ticket_url();
            TextView tit8 = view.findViewById(R.id.buy_at_q);
            TextView ans8 = view.findViewById(R.id.ticket_url);
            if( Temp1!= null){

                tit8.setVisibility(View.VISIBLE);
                ans8.setText(Temp1.toString());
                ans8.setSelected(true);

                TextView textView = (TextView) view.findViewById(R.id.ticket_url);
                SpannableString content = new SpannableString(Temp1.toString());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 2);
                textView.setText(content);
                textView.setTextColor(getResources().getColor(R.color.green)); // set text color
                textView.setLinkTextColor(getResources().getColor(R.color.green)); // set link color

                ans8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(ans8.getText().toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }else{
                tit8.setVisibility(View.GONE);
            }
            TextView url = view.findViewById(R.id.ticket_url);
//            url.setText("https://www.ticketmaster.com/ed-sheeran-x-tour-inglewood-california-09-23-2023/event/0A005D3EAC76317F");
            TextView f = view.findViewById(R.id.buy_at_q);
            f.setText("But Tickets at");

        }
        if(event.ticket_Status()!=null) {
            TextView status = view.findViewById(R.id.ticket_status);
//            status.setText(event.ticket_Status());
            CardView card = view.findViewById(R.id.status_card);
            setStatus(card,status,event.ticket_Status());
            TextView f = view.findViewById(R.id.ticket_statusq);
            f.setText("Ticket Status");
        }
        if(event.getPriceRange()!=null){
            TextView fuck = view.findViewById(R.id.price_range);
            fuck.setText(event.getPriceRange());
            TextView f = view.findViewById(R.id.price_rangeq);
            f.setText("Price Range");
        }
        ImageView seat = view.findViewById(R.id.seatMap);
        TextView url = view.findViewById(R.id.ticket_url);
        picasso.load(event.getSeatmapUrl()).resize(250,250).into(seat);
        artist_team.setSingleLine(true);
        artist_team.setSelected(true);
        url.setSingleLine(true);
        url.setSelected(true);








    }
}