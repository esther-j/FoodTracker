package com.example.foodtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            holder.expDateView.setText(getExpirationString(current.getExpirationDate()));
        } else {
            // Covers the case of data not being ready yet.
            holder.foodLabelView.setText(R.string.empty_error);
            holder.expDateView.setText(R.string.empty_error);
        }
    }

    private String getExpirationString(Date date) {
        Calendar curCal = Calendar.getInstance();
        LocalDate current = new DateTime(curCal).toLocalDate();
        LocalDate expDate = new DateTime(date).toLocalDate();
        int delta = Days.daysBetween(current, expDate).getDays();

        // get appropriate date string by casing on difference in days
        String dateStr;
        final int DAYS_IN_MONTH = curCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        final int DAYS_IN_YEAR = curCal.getActualMaximum(Calendar.DAY_OF_YEAR);
        if (delta == 0) dateStr = "today";
        else if (delta == 1) dateStr = "tomorrow";
        else if (delta < Calendar.DAY_OF_WEEK) dateStr = String.format(Locale.US,"in %d days", delta);
        else if (delta < DAYS_IN_MONTH) {
            int weeks = Weeks.weeksBetween(current, expDate).getWeeks();
            dateStr = String.format(Locale.US,"in %d week%s", weeks, weeks == 1 ? "" : "s");
        } else if (delta < DAYS_IN_YEAR) {
            int months = Months.monthsBetween(current, expDate).getMonths();
            dateStr = String.format(Locale.US,"in %d month%s", months, months == 1 ? "" : "s");
        } else {
            int years = Years.yearsBetween(current, expDate).getYears();
            dateStr = String.format(Locale.US,"in %d year%s", years, years == 1 ? "" : "s");
        }

        return String.format("Expires %s", dateStr);
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


