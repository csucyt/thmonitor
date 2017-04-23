package com.example.thmonitor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.thmonitor.config.LineChartConfig;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class LineChartActivity extends AppCompatActivity implements View.OnClickListener {

    private LineChart lineChart;
    private Button dayButton;
    private Button monthButton;
    private Button weekButton;

    private LineChartConfig lineChartConfig;

    private boolean flag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        flag = getIntent().getExtras().getBoolean("visit");

        lineChart = (LineChart)findViewById(R.id.a_line_chart);
        lineChartConfig = new LineChartConfig(lineChart, flag);

        dayButton = (Button)findViewById(R.id.day_btn);
        weekButton = (Button)findViewById(R.id.week_btn);
        monthButton = (Button)findViewById(R.id.month_btn);

        dayButton.setOnClickListener(this);
        weekButton.setOnClickListener(this);
        monthButton.setOnClickListener(this);

        lineChartConfig.lineChartInit(flag);

        Toolbar toolbar = (Toolbar)findViewById(R.id.linechart_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.three);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.linechart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.line_fill: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
            }
                lineChart.invalidate();
                break;
            case R.id.line_circle: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
            }
                lineChart.invalidate();
                break;
            case R.id.line_cubic: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.getMode() == LineDataSet.Mode.CUBIC_BEZIER)
                        set.setMode(LineDataSet.Mode.LINEAR);
                    else
                        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                }
            }
                lineChart.invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.day_btn:
                lineChartConfig.setType(1);
                lineChartConfig.setFlag(flag);
                lineChartConfig.lineChartInit(true);
                break;
            case R.id.week_btn:
                lineChartConfig.setType(2);
                lineChartConfig.setFlag(flag);
                lineChartConfig.lineChartInit(true);
                break;
            case R.id.month_btn:
                lineChartConfig.setType(3);
                lineChartConfig.setFlag(flag);
                lineChartConfig.lineChartInit(true);
                break;
        }
    }
}
