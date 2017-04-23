package com.example.thmonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thmonitor.config.LineChartConfig;
import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by Administrator on 2017/4/5.
 */

public class LineChartFragment extends Fragment {

    private LineChart lineChart;
    private boolean flag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.linechart_fragment, container, false);
        flag = getActivity().getIntent().getExtras().getBoolean("visit");
        lineChart = (LineChart)view.findViewById(R.id.line_chart);
        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("为了更好的体验，将切换为横屏")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), LineChartActivity.class);
                                intent.putExtra("visit", flag);
                                startActivity(intent);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }
        });



        LineChartConfig lineChartConfig = new LineChartConfig(lineChart);
        lineChartConfig.setFlag(flag);
        lineChartConfig.lineChartInit(false);
        return  view;
    }
}
