package com.example.foodtracker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodItemRepository {
    private FoodItemDao mFoodItemDao;
    private LiveData<List<FoodItem>> mAllFoodItems;

    FoodItemRepository(Application application) {
        FoodItemRoomDatabase db = FoodItemRoomDatabase.getDatabase(application);
        mFoodItemDao = db.foodItemDao();
        mAllFoodItems = mFoodItemDao.getAgeSortedFoodItems();
    }

    LiveData<List<FoodItem>> getAllFoodItems() {
        return mAllFoodItems;
    }

    void insert(FoodItem foodItem) {
        FoodItemRoomDatabase.databaseWriteExecutor.execute(() -> mFoodItemDao.insert(foodItem));
    }

    private static class deleteFoodItemAsyncTask extends AsyncTask<FoodItem, Void, Void> {
        private FoodItemDao mAsyncTaskDao;

        deleteFoodItemAsyncTask(FoodItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FoodItem... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void delete(FoodItem foodItem)  {
        new deleteFoodItemAsyncTask(mFoodItemDao).execute(foodItem);
    }
}
