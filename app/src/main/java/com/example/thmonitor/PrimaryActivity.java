package com.example.thmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thmonitor.db.Temperature;

import org.litepal.crud.DataSupport;

public class PrimaryActivity extends AppCompatActivity {

    private Button feelButton;
    private TextView hostIPText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        SharedPreferences pref = getSharedPreferences("Temp", MODE_PRIVATE);
        float minRange = pref.getFloat("minRange", 20);
        float normRange = pref.getFloat("normRange", 30);

        float minTemp = -20 + minRange;
        float normTemp = -20 + minRange + normRange;

        Temperature temp = DataSupport.findLast(Temperature.class);


        ImageView primaryImage = (ImageView)findViewById(R.id.primary_gif);
        Glide.with(this).load(R.drawable.primary).into(primaryImage);

        feelButton = (Button)findViewById(R.id.feel_btn);
        hostIPText = (TextView)findViewById(R.id.host_ip_text);

        if(temp.getTemp() < minTemp) {
            feelButton.setText("当前温度过低");
        } else if(temp.getTemp() < normTemp) {
            feelButton.setText("当前温度舒适");
        } else {
            feelButton.setText("当前温度过高");
        }

        final boolean flag = getIntent().getExtras().getBoolean("visit");
        if(!flag) {
            hostIPText.setText("192.168.191.1");
        } else {
            hostIPText.setText("未连接主机");
        }

        feelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrimaryActivity.this, MainActivity.class);
                intent.putExtra("visit", flag);
                startActivity(intent);
            }
        });

    }
}
