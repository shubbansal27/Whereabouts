package com.google.android.gms.location.sample.geofencing.utils;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by venkat on 10/16/2016.
 */

public class ApplicationUtils {
    public static String title;
    public static String desc;
    public static String stdate;
    public static String sttime;
    public static String eddate;
    public static String edtime;
    public static LatLng latlng;

    public static int radius;
    public static long convertPoxisTime(String date,String time){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateActual = null;
        try {
            dateActual = df.parse(""+date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch =(dateActual.getTime());
        Date dat = new Date();
        long ti =  (dat.getTime());
        int tim = (int) (System.currentTimeMillis());
        System.out.println(dateActual+"epoch:-"+epoch);
        System.out.println(dat+"epoch2 :-"+ti);
        System.out.println((epoch-ti)+"epochtim :-"+tim);
        return  epoch-ti;
    }
    public static Calendar getCalender(String date,String time){
        String[] dateAttr,timeAttr;
        dateAttr = date.replaceAll("\\s","").split("/");
        timeAttr = time.replaceAll("\\s","").split(":");
        System.out.println(dateAttr[2]);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR,Integer.parseInt(dateAttr[2]));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateAttr[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateAttr[0]));

        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeAttr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeAttr[1]));
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

}
