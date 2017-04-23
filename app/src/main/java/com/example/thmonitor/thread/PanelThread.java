package com.example.thmonitor.thread;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.thmonitor.MainActivity;
import com.example.thmonitor.MyPanel;
import com.example.thmonitor.R;
import com.example.thmonitor.db.Temperature;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PanelThread implements Runnable {

    private View view;
    private MyPanel myPanel;
    private TextView tempText;
    private TextView timeText;
    private Activity activity;

    private void init() {
        myPanel = (MyPanel)view.findViewById(R.id.my_panel);
        tempText = (TextView)view.findViewById(R.id.current_temp);
        timeText = (TextView)view.findViewById(R.id.current_time);
    }


    public PanelThread(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        init();
    }

    public void run() {
        try {
            while(true) {
                final Temperature temp = DataSupport.findFirst(Temperature.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempText.setText(temp.getTemp() + "℃");
                        timeText.setText(temp.getYear() + "." + temp.getMonth() + "."
                                + temp.getDay() + " " + temp.getHour() + ":" + temp.getMinute());
                        myPanel.setCurrentTemp(temp.getTemp());
                        myPanel.invalidate();
                    }
                });
                Thread.sleep(2000);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
