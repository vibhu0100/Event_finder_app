package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class artist_RecyclerViewAdapter extends RecyclerView.Adapter<artist_RecyclerViewAdapter.MyViewHolder>{
    ArrayList<artistModel> artistArray;
    Context context;
    Picasso picasso = Picasso.get();
    public artist_RecyclerViewAdapter(ArrayList<artistModel> artistArray,Context context){
        this.context = context;
        this.artistArray = artistArray;
    }

    @NonNull
    @Override
    public artist_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.artist_card,parent,false);
        return new artist_RecyclerViewAdapter.MyViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull artist_RecyclerViewAdapter.MyViewHolder holder, int position) {
    holder.name.setText(artistArray.get(position).getName());
    holder.follower.setText(artistArray.get(position).getFollowers());
    holder.spotify_url.setText(artistArray.get(position).getSpotifyUrl().toString());
    holder.popularity.setText(Integer.toString(artistArray.get(position).getPopularity()));
    holder.progressBar.setProgress(artistArray.get(position).getPopularity());
    picasso.load(artistArray.get(position).getImage()).into(holder.artist_image);
    ArrayList<Uri> urls = artistArray.get(position).getAlbumUrl();
        System.out.println("album_url"+urls.size());

        System.out.println("url1 "+ urls);
        picasso.load(urls.get(0)).into(holder.albm1);


            picasso.load(urls.get(1)).into(holder.albm2);


            picasso.load(urls.get(2)).into(holder.albm3);






    }

    @Override
    public int getItemCount() {
        return artistArray.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView follower;
        TextView spotify_url;
        ImageView artist_image;
        ImageView albm1;
        ImageView albm2;
        ImageView albm3;
        TextView popularity;
        ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.artist_name);
            follower = itemView.findViewById(R.id.followers);
            spotify_url = itemView.findViewById(R.id.spotify_url2);
            artist_image = itemView.findViewById(R.id.artist_image);
            albm1 = itemView.findViewById(R.id.imageViewa1);
            albm2 = itemView.findViewById(R.id.imageViewa2);
            albm3 = itemView.findViewById(R.id.imageViewa3);
            popularity = itemView.findViewById(R.id.progressText);
            progressBar = itemView.findViewById(R.id.progressBarPopular);
        }
    }
}
