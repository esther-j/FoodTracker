package com.example.foodtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FoodItemViewModel mFoodItemViewModel;
    public static final int NEW_FOOD_ITEM_ACTIVITY_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        FoodItemListAdapter adapter = new FoodItemListAdapter(this);
        adapter.setOnItemClickListener(new FoodItemListAdapter.OnAdapterItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                FoodItem current = adapter.getFoodItemAtPosition(position);
                mFoodItemViewModel.delete(current);
                String itemName = current.getFoodItem();
                Toast.makeText(
                        recyclerView.getContext(),
                        "Deleted " + itemName,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFoodItemViewModel = new ViewModelProvider(this).get(FoodItemViewModel.class);
        mFoodItemViewModel.getAllFoodItems().observe(this, new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(@Nullable final List<FoodItem> foodItems) {
                // Update the cached copy of the words in the adapter.
                adapter.setFoodItems(foodItems);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewFoodItemActivity.class);
                startActivityForResult(intent, NEW_FOOD_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_FOOD_ITEM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundleData = data.getExtras();
            String foodStr = bundleData.getString("FOOD_ITEM");
            String dateStr = bundleData.getString("DATE");

            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy");
            Date date = new Date();
            try {
                Log.e(TAG, "trying dateStr" + dateStr);
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Date parse error");
                saveError();
                return;
            }

            FoodItem foodItem = new FoodItem();
            foodItem.setFoodItem(foodStr);
            foodItem.setExpirationDate(date);
            mFoodItemViewModel.insert(foodItem);
            Toast.makeText(
                    getApplicationContext(),
                    "Created " + foodStr,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            Log.d(TAG, "Error from else");
            saveError();
        }
    }

    private void saveError() {
        Toast.makeText(
                getApplicationContext(),
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show();
    }
}

