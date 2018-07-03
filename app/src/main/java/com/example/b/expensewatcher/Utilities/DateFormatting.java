package com.example.b.expensewatcher.Utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by B on 15-Apr-17.
 */

public class DateFormatting {

    //TODO: Look into saving the time as well. dd/MM/yyyy hh:mm:ss

    SimpleDateFormat dateFormat_cleanup = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    DateFormat dateFormat_final = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    DateFormat dateFormat_agg = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
    Date date = new Date();
    String string_date = new String();
    long milliseconds;

    public long formatStringtoLong(String string_date){
        try {
            date = dateFormat_final.parse(string_date);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public String formatLongtoString(long long_date) {
        return String.valueOf(dateFormat_final.format(new Date(long_date)));
    }

    public String formatDatetoString(String activity, Date date){
        try{
            if(activity.equals("model")) {
                string_date = dateFormat_agg.format(date);
            }
            else {
                string_date = dateFormat_final.format(date);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  string_date;
    }

    public Date formatStringtoDate(String activity, String string_date) {
        try{

            //Log.d("DATE CLEANUP",String.valueOf(dateFormat_cleanup.parse(string_date)));
            if(activity.equals("Transaction")) {
                date = dateFormat_final.parse(string_date);
            }
            else if(activity.equals("Category")) {
                date = dateFormat_agg.parse(string_date);
            }
            else {
                date = dateFormat_cleanup.parse(string_date);
            }

           // date = dateFormat_final.format();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
