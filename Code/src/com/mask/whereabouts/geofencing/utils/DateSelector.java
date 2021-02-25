package com.google.android.gms.location.sample.geofencing.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by venkat on 10/14/2016.
 */

public class DateSelector implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {
    private int stDay,stMonth,stYear;
    private Context context;
    private EditText editText;
    public DateSelector(Context context, int editTextViewID){
        Activity act = (Activity)context;
        this.editText = (EditText)act.findViewById(editTextViewID);
        this.editText.setOnClickListener(this);
        this.editText.setOnFocusChangeListener(this);
        this.context = context;
    }
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        stYear = year;
        stMonth = monthOfYear;
        stDay = dayOfMonth;
        updateDisplay();
    }

    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.get(Calendar.MONTH));
        dialog.show();

    }
    public void onFocusChange(View v,boolean focus) {
        if(focus==true) {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    private void updateDisplay() {

        editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(stDay).append("/").append(stMonth + 1).append("/").append(stYear).append(" "));
    }
}
