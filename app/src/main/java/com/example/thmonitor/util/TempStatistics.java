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

    public static boolean setTemp(Context context) {

        boolean maxModify = false;
        boolean minModify = false;

        SharedPreferences prefs = context.getSharedPreferences("temp_statistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = context.getSharedPreferences("temp_statistics", Context.MODE_PRIVATE).edit();

        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

        float maxTemp = prefs.getFloat("max_temp", -100);
        float minTemp = prefs.getFloat("min_temp", -100);
        Temperature max_Temp = new Temperature();
        Temperature min_Temp = new Temperature();

        List<Temperature> temperatures = DataSupport.findAll(Temperature.class);

        if(temperatures.size() == 0) {
            return false;
        } else {
            for(Temperature temperature : temperatures) {
                if(maxTemp < temperature.getTemp()) {
                    maxTemp = temperature.getTemp();
                    max_Temp = temperature;
                    maxModify = true;
                }
                if(minTemp < temperature.getTemp()) {
                    minTemp = temperature.getTemp();
                    min_Temp = temperature;
                    minModify  = true;
                }
            }

            editor.putFloat("max_temp", maxTemp);
            editor.putFloat("min_temp", minTemp);
            if(maxModify) {
                String maxStr = max_Temp.getMonth() + "." + max_Temp.getDay() + " " + max_Temp.getHour() + ":" + max_Temp.getMinute();
                editor.putString("max_temp_time", maxStr);
            }
            if(minModify) {
                String minStr = min_Temp.getMonth() + "." + min_Temp.getDay() + " " + min_Temp.getHour() + ":" + min_Temp.getMinute();
                editor.putString("min_temp_time", minStr);
            }
            editor.apply();
        }

        return true;

    }

}
