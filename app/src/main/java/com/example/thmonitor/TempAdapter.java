package com.example.thmonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thmonitor.db.Temperature;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */


public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder> {

    private List<Temperature> temperatureList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Temperature temperature = temperatureList.get(position);
        String timeStr = temperature.getYear() + "/" + temperature.getMonth() + "/"
                + temperature.getDay() + " " + temperature.getHour() + ":" + temperature.getMinute();
        holder.timeText.setText(timeStr);
        holder.tempText.setText("" + temperature.getTemp() + "℃");
    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    public TempAdapter(List<Temperature> temperatureList) {
        this.temperatureList = temperatureList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeText;
        TextView tempText;

        public ViewHolder(View view) {
            super(view);
            timeText = (TextView)view.findViewById(R.id.recycler_view_time);
            tempText = (TextView)view.findViewById(R.id.recycler_view_temp);
        }

    }

}
