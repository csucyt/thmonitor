package com.example.thmonitor.config;

import android.graphics.Color;
import android.util.FloatProperty;

import com.example.thmonitor.db.Temperature;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.StreamHandler;

/**
 * Created by Administrator on 2017/4/3.
 */

public class LineChartConfig {

    static final int TYPE_DAY = 1;
    static final int TYPE_WEEK = 2;
    static final int TYPE_MONTH = 3;

    private int year_div = 12 * 31 * 24 * 60;
    private int month_div = 31 * 24 * 60;
    private int day_div = 24 * 60;
    private int hour_div = 60;

    private LineChart lineChart;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setType(int type) {
        this.type = type;
    }

    private boolean flag;
    private int type;

    private int increase;
    private float from;
    private float now;

    public LineChartConfig(LineChart lineChart) {
        this.lineChart = lineChart;
        this.flag = true;
        this.type = TYPE_DAY;
    }

    public LineChartConfig(LineChart lineChart, boolean flag) {
        this(lineChart);
        this.flag = flag;
    }

    public LineChartConfig(LineChart lineChart, boolean flag, int type) {
        this(lineChart, flag);
        this.type = type;
    }

    public void lineChartInit(boolean mFlag) {

        fromWhatTime();
//        setFlag(flag);
        lineChart.getDescription().setEnabled(mFlag);
        lineChart.setTouchEnabled(mFlag);
        lineChart.setDragEnabled(mFlag);
        lineChart.setScaleEnabled(mFlag);
        if(flag) {

            setData(type);

        } else {
            setSocketData(type);
        }
        lineChart.invalidate();
        lineChartAxisConfig();
    }

    private void setData(int type) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        if(type == TYPE_DAY) {
            increase = 10;
        } else if(type == TYPE_WEEK) {
            increase = 60;
        } else if(type == TYPE_MONTH) {
            increase = 12 * 60;
        }

        for (float x = from; x <= now + 24 * hour_div; x += increase) {

            float y = getRandom(60, -20);
            values.add(new Entry(x, y));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);

        data.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        lineChart.invalidate();
    }


    private void setSocketData(int type) {

        LineData data = lineChart.getData();
        if(data == null) {
            lineChart.setData(new LineData());
            data = lineChart.getData();
        }

        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        String month = new SimpleDateFormat("MM").format(date);
        String day = new SimpleDateFormat("dd").format(date);
        float timeFlag = (Float.parseFloat(year) - 2017) * 535680 +
                (Float.parseFloat(month) - 1) * 44640 +
                (Float.parseFloat(day) - 1) * 1440;

        if(type == TYPE_DAY) {
            ILineDataSet dataSet = data.getDataSetByLabel("fromSocket", false);

            if(dataSet == null) {
                dataSet = createSet("fromSocket");
                data.addDataSet(dataSet);
            }
            dataSet.clear();

            String whereStr = "year == ? and month == ? and day == ?";
            List<Temperature> temperatures = DataSupport.where(whereStr, year, month, day)
                                                        .find(Temperature.class);

            if(temperatures.size() != 0) {
               // List<Entry> entries = new ArrayList<>();

                for(Temperature temperature : temperatures) {
                    float x = (Float.parseFloat(temperature.getYear())-2017) * year_div +
                            (Float.parseFloat(temperature.getMonth())-1) * month_div +
                            (Float.parseFloat(temperature.getDay()) - 1) * day_div+
                            Float.parseFloat(temperature.getHour()) * hour_div +
                            Float.parseFloat(temperature.getMinute());
                    float y = temperature.getTemp();
                    dataSet.addEntry(new Entry(x, y));
                }

                data.notifyDataChanged();
                lineChart.notifyDataSetChanged();

                lineChart.invalidate();

            }

        } else if(type == TYPE_WEEK) {
            ILineDataSet dataSet = data.getDataSetByLabel("fromSocket", false);

            if(dataSet == null) {
                dataSet = createSet("fromSocket");
                data.addDataSet(dataSet);
            }
            dataSet.clear();
            String whereStr = "timeFlag >= ? and timeFlag <= ?";
            float from = timeFlag - 7*24*60;
            List<Temperature> temperatures = DataSupport.where(whereStr, String.valueOf(from), String.valueOf(timeFlag))
                    .find(Temperature.class);

            if(temperatures.size() != 0) {
                // List<Entry> entries = new ArrayList<>();

                for(Temperature temperature : temperatures) {
                    float x = (Float.parseFloat(temperature.getYear())-2017) * year_div +
                            (Float.parseFloat(temperature.getMonth())-1) * month_div +
                            (Float.parseFloat(temperature.getDay()) - 1) * day_div+
                            Float.parseFloat(temperature.getHour()) * hour_div +
                            Float.parseFloat(temperature.getMinute());
                    float y = temperature.getTemp();
                    dataSet.addEntry(new Entry(x, y));
                }

                data.notifyDataChanged();
                lineChart.notifyDataSetChanged();

                lineChart.invalidate();

            }
        } else if(type == TYPE_MONTH) {
            ILineDataSet dataSet = data.getDataSetByLabel("fromSocket", false);

            if (dataSet == null) {
                dataSet = createSet("fromSocket");
                data.addDataSet(dataSet);
            }
            dataSet.clear();
            String whereStr = "timeFlag >= ? and timeFlag <= ?";
            float from = timeFlag - 31 * 24 * 60;
            List<Temperature> temperatures = DataSupport.where(whereStr, String.valueOf(from), String.valueOf(timeFlag))
                    .find(Temperature.class);

            if (temperatures.size() != 0) {
                // List<Entry> entries = new ArrayList<>();

                for (Temperature temperature : temperatures) {
                    float x = (Float.parseFloat(temperature.getYear()) - 2017) * year_div +
                            (Float.parseFloat(temperature.getMonth()) - 1) * month_div +
                            (Float.parseFloat(temperature.getDay()) - 1) * day_div +
                            Float.parseFloat(temperature.getHour()) * hour_div +
                            Float.parseFloat(temperature.getMinute());
                    float y = temperature.getTemp();
                    dataSet.addEntry(new Entry(x, y));
                }

                data.notifyDataChanged();
                lineChart.notifyDataSetChanged();

                lineChart.invalidate();
            }
        }
    }


    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    public void lineChartAxisConfig() {

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setGranularity((float) increase);
        xAxis.setAxisMaximum(now + 24 * hour_div);
        xAxis.setAxisMinimum(from);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String string = " ";
                if (type == TYPE_DAY) {
                    String hour = String.valueOf((long) value % year_div % month_div % day_div / hour_div);
                    String minute = String.valueOf((long) value % year_div % month_div % day_div % hour_div);
                    string = hour + ":" + minute;
                } else if(type == TYPE_WEEK | type == TYPE_MONTH) {
                    String hour = String.valueOf((long) value % year_div % month_div % day_div / hour_div);
                    String month = String.valueOf((long)value % year_div / month_div + 1);
                    String day = String.valueOf((long)value % year_div % month_div / day_div + 1);
                    string = month + "." + day + " " + hour;
                }
                return string;
            }
        });

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setTextColor(Color.BLACK);
        leftYAxis.setAxisMinimum(-20);
        leftYAxis.setAxisMaximum(40);

    }

    public LineDataSet createSet(String name) {

        LineDataSet set = new LineDataSet(null, name);
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

    private void fromWhatTime() {
        Date date = new Date();
        float year = Float.parseFloat(new SimpleDateFormat("yyyy").format(date));
        float month = Float.parseFloat(new SimpleDateFormat("MM").format(date));
        float day = Float.parseFloat(new SimpleDateFormat("dd").format(date));

        from = 0;
        now = (year - 2017) * year_div +(month - 1) * month_div + (day - 1) * day_div;

        if(type == TYPE_DAY) {
            from = now;
        } else if(type == TYPE_WEEK) {
            from = now - 7 * 24 * 60;
        } else if(type == TYPE_MONTH) {
            from = now - 31 * 24 * 60;
        }
    }

}
