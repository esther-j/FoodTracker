package com.example.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewFoodItemActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.foodItemlistsql.REPLY";

    private EditText mEditFoodItemView;
    private EditText mEditDateView;
    private DatePickerDialog mDatePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);
        mEditFoodItemView = findViewById(R.id.edit_food_item);
        mEditDateView = findViewById(R.id.edit_date);

        Calendar cal = Calendar.getInstance();
        final int DAYS_IN_WEEK = 7;
        cal.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK); // jump a week forward
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH); // for zero-indexing
        int year = cal.get(Calendar.YEAR);

        mEditDateView.setText(getFormatDateFromCalendar(cal));

        mEditDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog

                mDatePicker = new DatePickerDialog(NewFoodItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                Calendar newCal = Calendar.getInstance();
                                newCal.set(year, month, day);
                                mEditDateView.setText(getFormatDateFromCalendar(newCal));
                            }
                        }, year, month, day);
                mDatePicker.show();
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditFoodItemView.getText())
                        || TextUtils.isEmpty(mEditDateView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                    Toast.makeText(
                            view.getContext(),
                            "Please fill out all fields",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    String foodItem = mEditFoodItemView.getText().toString();
                    String dateStr = mEditDateView.getText().toString();

                    replyIntent.putExtra("FOOD_ITEM", foodItem);
                    replyIntent.putExtra("DATE", dateStr);
                    //replyIntent.putExtra(EXTRA_REPLY, )
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }

    private String getFormatDateFromCalendar(Calendar cal) {
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String dayStr = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String monthStr = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        return String.format(Locale.US,"%s, %s %d %d", dayStr, monthStr, day, year);
    }
}
