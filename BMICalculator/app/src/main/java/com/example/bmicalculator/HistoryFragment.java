package com.example.bmicalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class HistoryFragment extends Fragment {
    private ListView listView;
    private LinkedList<BMI> data = null;
    private SQLiteDatabase db;
    private BMIDbHelper dbHelper;
    private BMIAdapter bmiAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        listView = view.findViewById(R.id.listBMI);
        final Button button = view.findViewById(R.id.clearHistory);
        dbHelper = new BMIDbHelper(getContext());
        getHistory();
        setListViewHeight(listView);
        button.setOnClickListener(clearHistory);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            data.clear();
            bmiAdapter.notifyDataSetChanged();
            getHistory();
            setListViewHeight(listView);
        }
    }

    private void getHistory() {
        db = dbHelper.getReadableDatabase();
        data = new LinkedList<BMI>();
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BMIData.BMIEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = db.query(
                BMIData.BMIEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder);
        while (cursor.moveToNext()) {
            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_NAME));
            String itemSex = cursor.getString(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_SEX));
            String itemDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_DATE));
            float itemHeight = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_HEIGHT));
            float itemWeight = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_WEIGHT));
            float itemBmi = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_BMI));
            data.add(new BMI(itemName, itemSex, itemDate,
                    itemHeight, itemWeight, itemBmi));
        }
        cursor.close();
        bmiAdapter = new BMIAdapter(data, getContext());
        listView.setAdapter(bmiAdapter);
    }

    private View.OnClickListener clearHistory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db = dbHelper.getWritableDatabase();
            db.delete(BMIData.BMIEntry.TABLE_NAME, null,null);
            if (data != null) {
                data.clear();
                bmiAdapter.notifyDataSetChanged();
                setListViewHeight(listView);
            }
        }
    };

    private void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) { return; }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
        // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
