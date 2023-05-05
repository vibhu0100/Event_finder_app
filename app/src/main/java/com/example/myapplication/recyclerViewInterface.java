package com.example.myapplication;

import android.view.View;

public interface recyclerViewInterface {
    void onItemClick(int position);
    void addFavourite(int position, View view);
}
