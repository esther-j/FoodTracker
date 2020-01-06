package com.example.foodtracker;

import android.view.View;

// from https://android.jlelse.eu/click-listener-for-recyclerview-adapter-2d17a6f6f6c9
public interface RecyclerViewClickListener {
    void onClick(View view, int position);
}
