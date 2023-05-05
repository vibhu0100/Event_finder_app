package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class event_RecyclerViewAdapter extends RecyclerView.Adapter<event_RecyclerViewAdapter.MyViewHolder> {
    private final recyclerViewInterface recyclerInterface;
    Context context;
    ArrayList <eventRowModel> event_array;
    Bundle event_args = new Bundle();
    public event_RecyclerViewAdapter(Context context, ArrayList<eventRowModel> event_array,recyclerViewInterface recyclerInterface){
        this.context = context;
        this.event_array = event_array;
        this.recyclerInterface = recyclerInterface;
    }
    Picasso picasso = Picasso.get();

    @NonNull
    @Override
    public event_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row,parent,false);
        return new event_RecyclerViewAdapter.MyViewHolder(view,recyclerInterface);
    }

    private void updateFavButton(ImageView favButton,Boolean isFavourite){
        if(isFavourite){
            favButton.setImageResource(R.drawable.heart_filled);
        }
        else{favButton.setImageResource(R.drawable.heart_outline);}
    }
    public void refreshData(ArrayList<eventRowModel> eventList){
        this.event_array = eventList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull event_RecyclerViewAdapter.MyViewHolder holder, int position) {
        try {
            holder.eventName.setText(event_array.get(position).getEventName());
            holder.venue.setText(event_array.get(position).getVenue());
            holder.genre.setText(event_array.get(position).getClassifications().getAsJsonObject("segment").get("name").getAsString());
            holder.date.setText(event_array.get(position).getDates().get("localDate").getAsString());
            if(event_array.get(position).getDates().has("localTime")) {
                holder.time.setText(event_array.get(position).getTime());
            }
            updateFavButton(holder.favButton,event_array.get(position).isFavourite(context));
            System.out.println("image"+event_array.get(position).imageUrl());
            picasso.load(event_array.get(position).imageUrl()).resize(100,100).centerInside().into(holder.image);
            holder.eventName.setSelected(true);
            holder.eventName.setSingleLine(true);
            holder.venue.setSelected(true);
            holder.venue .setSingleLine(true);
        }
        catch(Exception e){

        }
    }


    @Override
    public int getItemCount() {
        return event_array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView eventName;
        TextView venue;
        TextView genre;
        TextView date;
        TextView time;
        ImageView favButton;
        //String image_url;


        public MyViewHolder(@NonNull View itemView, recyclerViewInterface recyclerInterface) {
            super(itemView);
            image  = itemView.findViewById(R.id.eventImage1);
            eventName = itemView.findViewById(R.id.event_name);
            venue = itemView.findViewById(R.id.event_venue);
            genre = itemView.findViewById(R.id.event_genre);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            favButton = itemView.findViewById(R.id.favourite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
//                            if(favbutton.set)
                            recyclerInterface.onItemClick(pos);
                        }

                    }

                }
            });
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerInterface.addFavourite(pos,v);

                        }
                    }
                }
            });


        }
    }
}
