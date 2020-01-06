package com.example.foodtracker;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FoodItem foodItem);

    @Delete
    void delete(FoodItem foodItem);

    @Query("DELETE FROM food_item_table")
    void deleteAll();

    @Query("SELECT * from food_item_table ORDER BY food_item ASC")
    LiveData<List<FoodItem>> getAlphabetizedFoodItems();

    @Query("SELECT * from food_item_table ORDER BY expiration_date ASC")
    LiveData<List<FoodItem>> getAgeSortedFoodItems();

}
