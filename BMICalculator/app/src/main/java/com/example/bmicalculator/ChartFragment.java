package com.example.bmicalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ChartFragment extends Fragment {
    private SQLiteDatabase db;
    private BMIDbHelper dbHelper;
    private ChartView chartView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);
        chartView = view.findViewById(R.id.chartView);
        dbHelper = new BMIDbHelper(getContext());
        setChart();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setChart();
        }
    }

    private void setChart() {
        db = dbHelper.getReadableDatabase();
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BMIData.BMIEntry.COLUMN_NAME_DATE + " ASC";
        Cursor cursor = db.query(
                BMIData.BMIEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder);
        List<Float> data = new LinkedList<>();
        List<String> xAxisData = new LinkedList<>();
        while (cursor.moveToNext()) {
            float itemBmi = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_BMI));
            String itemDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry.COLUMN_NAME_DATE));
            String xData = itemDate.substring(5, 10);
            xData = xData.replace('-', '/');
            xData = xData.replace("0", "");
            data.add(itemBmi);
            xAxisData.add(xData);
        }
        cursor.close();
        if (data.size() == 0 || xAxisData.size() == 0) {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                    .setTitle("Warning")
                    .setMessage("Your history record is empty.")
                    .setPositiveButton("accept", null)
                    .show();
            return;
        }
        if (data.size() == 1 || xAxisData.size() == 1) {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                    .setTitle("Warning")
                    .setMessage("Your history record is not enough.")
                    .setPositiveButton("accept", null)
                    .show();
            return;
        }
        chartView.setData(data, xAxisData);
    }
}
