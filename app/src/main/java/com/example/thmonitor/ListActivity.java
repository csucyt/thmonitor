package com.example.thmonitor;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thmonitor.db.Temperature;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchEditText;
    private Button searchButton;

    private TextView dataNumTextView;
    private TextView averTextView;
    private TextView hightTextView;
    private TextView lowTextView;

    private RecyclerView recyclerView;

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;

    private List<Temperature> temperatureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        searchEditText = (EditText)findViewById(R.id.search_edit_text);
        searchButton = (Button)findViewById(R.id.search_btn);
        dataNumTextView = (TextView)findViewById(R.id.data_num);
        hightTextView = (TextView)findViewById(R.id.list_high_temp);
        lowTextView = (TextView)findViewById(R.id.list_low_temp);
        averTextView = (TextView)findViewById(R.id.average_temp);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.search_btn:
                String str = searchEditText.getText().toString();
                if(!formatCheck(str)) {
                    Toast.makeText(ListActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListActivity.this, "正在查询....", Toast.LENGTH_SHORT).show();
                    setTextView();
                    TempAdapter tempAdapter = new TempAdapter(temperatureList);
                    recyclerView.setAdapter(tempAdapter);
                }
                break;
        }
    }

    private boolean formatCheck(String str) {
        int lenght = str.length();
        String whereStr = null;
        switch (lenght) {
            case 4:
                year = str.substring(0, 4);
                whereStr = "year == ?";
                temperatureList = DataSupport.where(whereStr, year).find(Temperature.class);
                break;
            case 6:
                year = str.substring(0, 4);
                month = str.substring(4, 6);
                whereStr = "year == ? and month == ?";
                temperatureList = DataSupport.where(whereStr, year, month)
                        .find(Temperature.class);
                break;
            case 8:
                year = str.substring(0, 4);
                month = str.substring(4, 6);
                day = str.substring(6, 8);
                whereStr = "year == ? and month == ? and day == ?";
                temperatureList = DataSupport.where(whereStr, year, month, day)
                        .find(Temperature.class);
                break;
            case 10:
                year = str.substring(0, 4);
                month = str.substring(4, 6);
                day = str.substring(6, 8);
                hour = str.substring(8, 10);
                whereStr = "year == ? and month == ? and day == ? and hour == ?";
                temperatureList = DataSupport.where(whereStr, year, month, day, hour)
                        .find(Temperature.class);
                break;
            case 12:
                year = str.substring(0, 4);
                month = str.substring(4, 6);
                day = str.substring(6, 8);
                hour = str.substring(8, 10);
                minute = str.substring(10, 12);
                whereStr = "year == ? and month == ? and day == ? and hour == ? and minute == ?";
                temperatureList = DataSupport.where(whereStr, year, month, day, hour, minute)
                        .find(Temperature.class);
                break;
            default:
                return false;
        }
        return true;
    }

    private void setTextView() {
        float highTemp = -100;
        float lowTemp = 100;
        float averTemp = 0;
        dataNumTextView.setText("" + temperatureList.size());

        for(Temperature temperature : temperatureList) {
            averTemp += temperature.getTemp();
            if(temperature.getTemp() > highTemp) {
                highTemp = temperature.getTemp();
            }
            if(temperature.getTemp() < lowTemp) {
                lowTemp = temperature.getTemp();
            }
        }

        averTemp /= temperatureList.size();

        hightTextView.setText("" + highTemp + "℃");
        lowTextView.setText("" + lowTemp + "℃");
        averTextView.setText("" + averTemp + "℃");
    }

}
