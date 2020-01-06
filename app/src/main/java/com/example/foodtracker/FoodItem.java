package com.example.foodtracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "food_item_table")
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "food_item")
    private String foodItem;

    @NonNull
    @ColumnInfo(name = "expiration_date")
    private Date expirationDate;

    public int getId() { return this.id; }

    public String getFoodItem() {
        return this.foodItem;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setId(int id) { this.id = id; }

    public void setFoodItem(String foodItem) { this.foodItem = foodItem; }

    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
}
