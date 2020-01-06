package com.example.foodtracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodItemViewModel extends AndroidViewModel {
    private FoodItemRepository mFoodItemRepository;
    private LiveData<List<FoodItem>> mAllWords;

    public FoodItemViewModel(Application application) {
        super(application);
        mFoodItemRepository = new FoodItemRepository(application);
        mAllWords = mFoodItemRepository.getAllFoodItems();
    }

    LiveData<List<FoodItem>> getAllFoodItems() {
        return mAllWords;
    }

    public void insert(FoodItem foodItem) {
        mFoodItemRepository.insert(foodItem);
    }

    public void delete(FoodItem foodItem) {
        mFoodItemRepository.delete(foodItem);
    }

}
