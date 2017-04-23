package com.example.thmonitor.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import com.example.thmonitor.MainActivity;
import com.example.thmonitor.config.LineChartConfig;
import com.example.thmonitor.db.Temperature;

import java.util.concurrent.TimeUnit;

public class DataUpdate extends Service {


    public DataUpdate() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                long now = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
                Temperature temperature = new Temperature();
                float temp = (float)(Math.random() * 10) + 10.0f;
//                temperature.setTemperature(temp);
//                temperature.setTimeMillis(now);
//                temperature.save();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

}
