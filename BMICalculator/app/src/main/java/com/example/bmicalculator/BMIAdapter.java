package com.example.bmicalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class BMIAdapter extends BaseAdapter {
    private LinkedList<BMI> data;
    private Context context;
    private OnItemDeleteListener onItemDeleteListener;

    public BMIAdapter(LinkedList<BMI> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.history_list_item, parent,false);
            holder = new ViewHolder();
            holder.txt_name = convertView.findViewById(R.id.col1);
            holder.txt_height = convertView.findViewById(R.id.col3);
            holder.txt_weight = convertView.findViewById(R.id.col4);
            holder.txt_bmi = convertView.findViewById(R.id.col5);
            holder.txt_date = convertView.findViewById(R.id.col6);
            holder.button = convertView.findViewById(R.id.deleteItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_name.setText(data.get(position).getName());
        holder.txt_date.setText(data.get(position).getDate());
        holder.txt_height.setText(
                new DecimalFormat("0.00").format(data.get(position).getHeight()));
        holder.txt_weight.setText(
                new DecimalFormat("0.0").format(data.get(position).getWeight()));
        holder.txt_bmi.setText(
                new DecimalFormat("0.0").format(data.get(position).getBmi()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDeleteListener.onDeleteButtonClick(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView txt_name;
        TextView txt_height;
        TextView txt_weight;
        TextView txt_bmi;
        TextView txt_date;
        ImageButton button;
    }

    public interface OnItemDeleteListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnItemDeleteListener(
            OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }
}
