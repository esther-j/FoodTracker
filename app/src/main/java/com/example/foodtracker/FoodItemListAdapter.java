package com.example.foodtracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodItemListAdapter extends RecyclerView.Adapter<FoodItemListAdapter.FoodItemViewHolder> {

    private OnAdapterItemClickListener mListener;

    public interface OnAdapterItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnAdapterItemClickListener listener) {
        mListener = listener;
    }

    class FoodItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodLabelView;
        private final TextView expDateView;
        private final ImageButton deleteButton;

        private FoodItemViewHolder(View itemView, OnAdapterItemClickListener listener) {
            super(itemView);
            foodLabelView = itemView.findViewById(R.id.food_text_label);
            expDateView = itemView.findViewById(R.id.date_label);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int curPos = getAdapterPosition();
                        if (curPos != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(curPos);
                        }
                    }
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<FoodItem> mFoodItems; // Cached copy of words

    FoodItemListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new FoodItemViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(FoodItemViewHolder holder, int position) {
        if (mFoodItems != null) {
            FoodItem current = mFoodItems.get(position);
            holder.foodLabelView.setText(current.getFoodItem());
            holder.expDateView.setText(dateToString(current.getExpirationDate()));
        } else {
            // Covers the case of data not being ready yet.
            holder.foodLabelView.setText("No food item");
            holder.expDateView.setText("Error");
        }
    }

    String dateToString(Date date) {
        Calendar curCal = Calendar.getInstance();
        DateTime current = new DateTime(curCal);

        DateTime otherDate = new DateTime(date);
        int delta = Days.daysBetween(current, otherDate).getDays();
        //SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        //return df.format(date);
        return Integer.toString(delta) + " days";
    }

    void setFoodItems(List<FoodItem> foodItems){
        mFoodItems = foodItems;
        notifyDataSetChanged();
    }

    public FoodItem getFoodItemAtPosition(int position) {
        return mFoodItems.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFoodItems != null)
            return mFoodItems.size();
        else return 0;
    }

}


