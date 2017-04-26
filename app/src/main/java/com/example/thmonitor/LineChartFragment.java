package com.example.thmonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thmonitor.config.LineChartConfig;
import com.example.thmonitor.util.TempStatistics;
import com.github.mikephil.charting.charts.LineChart;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/4/5.
 */

public class LineChartFragment extends Fragment {

    private TextView maxTempText;
    private TextView minTempText;
    private TextView maxTempTimeText;
    private TextView minTempTimeText;

    private SwipeRefreshLayout swipeRefreshLayout;

    private LineChart lineChart;
    private boolean flag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final String[] dialog = {"下次不再显示"};
        View view = inflater.inflate(R.layout.linechart_fragment, container, false);
        flag = getActivity().getIntent().getExtras().getBoolean("visit");

        lineChart = (LineChart)view.findViewById(R.id.line_chart);
        maxTempText = (TextView)view.findViewById(R.id.max_temp);
        maxTempTimeText = (TextView)view.findViewById(R.id.max_temp_time);
        minTempText = (TextView)view.findViewById(R.id.min_temp);
        minTempTimeText = (TextView)view.findViewById(R.id.min_temp_time);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("Suggestion", Context.MODE_PRIVATE);
                boolean suggestFlag = pref.getBoolean("flag", true);
                if(suggestFlag) {
                    new AlertDialog.Builder(getActivity()).setTitle("为了更好的体验，将切换为横屏")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getContext().getSharedPreferences("Suggestion", Context.MODE_PRIVATE)
                                            .edit();
                                    SharedPreferences pref = getContext().getSharedPreferences("Suggestion", Context.MODE_PRIVATE);
                                    if(pref.getBoolean("NoSuggestion", false)) {
                                        editor.putBoolean("flag", false);   //false表示不显示
                                        editor.apply();
                                    }
                                    Intent intent = new Intent(getActivity(), LineChartActivity.class);
                                    intent.putExtra("visit", flag);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setSingleChoiceItems(dialog, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getContext().getSharedPreferences("Suggestion", Context.MODE_PRIVATE)
                                            .edit();
                                    editor.putBoolean("NoSuggestion", true);
                                    editor.apply();
                                }
                            }).show();
                } else {
                    Intent intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("visit", flag);
                    startActivity(intent);
                }
            }
        });

        refreshTempStatistics();

        final LineChartConfig lineChartConfig = new LineChartConfig(lineChart);
        lineChartConfig.setFlag(flag);
        lineChartConfig.lineChartInit(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lineChartConfig.lineChartInit(false);
                refreshTempStatistics();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return  view;
    }

    private void refreshTempStatistics() {
        if(flag) {
            minTempTimeText.setText("无");
            maxTempTimeText.setText("无");
            minTempText.setText("无");
            maxTempText.setText("无");
        } else {
            if(TempStatistics.setTemp(getContext())) {

                SharedPreferences prefs = getContext().getSharedPreferences("temp_statistics", Context.MODE_PRIVATE);

                float maxTemp = prefs.getFloat("max_temp", -100);
                String maxStr = prefs.getString("max_temp_time", null);
                float minTemp = prefs.getFloat("min_temp", -100);
                String minStr = prefs.getString("min_temp_time", null);

                minTempTimeText.setText(minStr);
                maxTempTimeText.setText(maxStr);
                minTempText.setText(minTemp + "℃");
                maxTempText.setText(maxTemp + "℃");

            } else {
                minTempTimeText.setText("无");
                maxTempTimeText.setText("无");
                minTempText.setText("无");
                maxTempText.setText("无");
            }
        }
    }

}
