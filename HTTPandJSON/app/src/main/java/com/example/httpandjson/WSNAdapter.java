package com.example.httpandjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class WSNAdapter extends BaseAdapter {

    private LinkedList<WSN> data;
    private Context context;

    public WSNAdapter(LinkedList<WSN> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.wsn_list, parent, false);
            holder = new ViewHolder();
            holder.tGroupID = convertView.findViewById(R.id.groupID_d);
            holder.tXH = convertView.findViewById(R.id.xh_d);
            holder.tValue = convertView.findViewById(R.id.value_d);
            holder.tDate = convertView.findViewById(R.id.date_d);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tGroupID.setText(data.get(position).getGroupID());
        holder.tXH.setText(data.get(position).getXH());
        holder.tValue.setText(data.get(position).getValue());
        holder.tDate.setText(data.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView tGroupID;
        TextView tXH;
        TextView tValue;
        TextView tDate;
    }
}
