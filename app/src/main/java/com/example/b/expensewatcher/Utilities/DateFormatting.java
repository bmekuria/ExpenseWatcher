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
    public String DATE_FORMAT = "dd/MM/yyyy";
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    Date date = new Date();
    long milliseconds;

    public long formatStringtoLong(String string_date){
        try {
            date = dateFormat.parse(string_date);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public String formatLongtoString(long long_date) {
        return String.valueOf(dateFormat.format(new Date(long_date)));
    }
}
