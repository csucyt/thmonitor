package com.example.thmonitor.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.thmonitor.MainActivity;
import com.example.thmonitor.db.Temperature;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/3.
 */

public class TempStatistics {

    public static void setMaxTemp(Context context, Temperature temp, TextView timeText, TextView tempText) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

        float maxTemp = prefs.getFloat("max_temp", -100);
        String str = prefs.getString("max_temp_time", null);

//        if(maxTemp == 0 && getTemp() != null) {
//            Temperature temperature = getTemp()[0];
//            maxTemp = temperature.getTemperature();
//            str = mFormat.format(new Date(temperature.getTimeMillis()));
//        } else {
//            if(temp != null) {
//                if (maxTemp < temp.getTemperature()) {
//                    maxTemp = temp.getTemperature();
//                    str = mFormat.format(new Date(temp.getTimeMillis()));
//                }
//            }
//        }

        editor.putFloat("max_temp", maxTemp);
        editor.putString("max_temp_time", str);
        editor.apply();
        timeText.setText(str);
        tempText.setText("" + maxTemp + "℃");

    }

    public static void setMinTemp(Context context, Temperature temp, TextView timeText, TextView tempText) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

        float minTemp = prefs.getFloat("min_temp", 100);
        String str = prefs.getString("min_temp_time", null);

//        if(minTemp == 0 & getTemp() != null) {
//            if(getTemp() == null) return;
//            Temperature temperature = getTemp()[1];
//            minTemp = temperature.getTemperature();
//            str = mFormat.format(new Date(temperature.getTimeMillis()));
//        } else {
//            if(temp != null) {
//                if (minTemp > temp.getTemperature()) {
//                    minTemp = temp.getTemperature();
//                    str = mFormat.format(new Date(temp.getTimeMillis()));
//                }
//            }
//        }

        editor.putFloat("min_temp", minTemp);
        editor.putString("min_temp_time", str);
        editor.apply();
        timeText.setText(str);
        tempText.setText("" + minTemp + "℃");

    }

    public static Temperature[] getTemp() {

        float maxTemp = 0;
        float minTemp = 0;
        Temperature minTemperature = new Temperature();
        Temperature maxTemperature = new Temperature();
//        List<Temperature> temperatureList = DataSupport.findAll(Temperature.class);
//        if(temperatureList.size() == 0) return null;
//        for(Temperature temperature : temperatureList) {
//            if(maxTemp < temperature.getTemperature()) {
//                maxTemp = temperature.getTemperature();
//                maxTemperature = temperature;
//            }
//            if(minTemp > temperature.getTemperature()) {
//                minTemp = temperature.getTemperature();
//                minTemperature = temperature;
//            }
//        }
        return new Temperature[]{maxTemperature, minTemperature};
    }

    public static void setTempText(Context context, TextView maxTempTimeText, TextView maxTempText,
                              TextView minTempTimeText, TextView minTempText) {

        Temperature temperature = DataSupport.findLast(Temperature.class);
        TempStatistics.setMaxTemp(context, temperature, maxTempTimeText, maxTempText);
        TempStatistics.setMinTemp(context, temperature, minTempTimeText, minTempText);

    }

}
