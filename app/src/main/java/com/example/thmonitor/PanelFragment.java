package com.example.thmonitor;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thmonitor.thread.PanelThread;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/5.
 */

public class PanelFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panel_fragment, container, false);
        MyPanel myPanel = (MyPanel)view.findViewById(R.id.my_panel);
        TextView tempText = (TextView)view.findViewById(R.id.current_temp);
        TextView timeText = (TextView)view.findViewById(R.id.current_time);
        boolean flag = getActivity().getIntent().getExtras().getBoolean("visit");
        if(flag) {
            float currentTemp = (float) (Math.random() * 80) - 20;
            myPanel.setCurrentTemp(currentTemp);
            tempText.setText((int)currentTemp + "℃");
            Date date = new Date();
            String str = new SimpleDateFormat("dd MMM HH:mm").format(date);
            timeText.setText(str);
            myPanel.invalidate();
        } else {
            Thread panelThread = new Thread(new PanelThread(view, getActivity()));
            panelThread.setDaemon(true);
            panelThread.start();
        }
        return view;
    }
}
