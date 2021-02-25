package com.google.android.gms.location.sample.geofencing.utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by venkat on 10/16/2016.
 */

public class TimeSelector implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener,View.OnClickListener{
    private int hours,minutes;
    private Context context;
    private EditText editText;
    public TimeSelector(Context context, int editTextViewID){
        Activity act = (Activity)context;
        this.editText = (EditText)act.findViewById(editTextViewID);
        this.editText.setOnFocusChangeListener(this);
        this.editText.setOnClickListener(this);
        this.context = context;
    }
    public void onTimeSet(android.widget.TimePicker view, int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        updateDisplay();
    }

    public void onFocusChange(View v,boolean focus) {
        if(focus==true) {

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(context,this,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false);
            dialog.show();
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    public void onClick(View v) {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        TimePickerDialog dialog = new TimePickerDialog(context,this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        dialog.show();


    }



    private void updateDisplay() {
        String min,hr;
        if(minutes<10){
            min="0"+minutes;
        }
        else{
            min=""+minutes;
        }
        if(hours<10){
            hr="0"+hours;
        }
        else{
            hr=""+hours;
        }
        editText.setText(new StringBuilder().append(hr).append(":").append(min).append(" "));
    }
}
