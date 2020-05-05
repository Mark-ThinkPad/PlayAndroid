package com.example.bmicalculator;

import android.content.DialogInterface;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.Objects;

public class HistoryFragment extends Fragment {
    private ListView listView;
    private LinkedList<BMI> data = null;
    private SQLiteDatabase db;
    private BMIDbHelper dbHelper;
    private BMIAdapter bmiAdapter;
    private View cview;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        listView = view.findViewById(R.id.listBMI);
        cview = Objects.requireNonNull(getActivity()).findViewById(R.id.coordinator);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
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
            long itemID = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BMIData.BMIEntry._ID));
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
            data.add(new BMI(itemID, itemName, itemSex, itemDate,
                    itemHeight, itemWeight, itemBmi));
        }
        cursor.close();
        bmiAdapter = new BMIAdapter(data, getContext());
        bmiAdapter.setOnItemDeleteListener(deleteListener);
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
                Snackbar.make(cview,
                        "History has been deleted",
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };

    private BMIAdapter.OnItemDeleteListener deleteListener =
            new BMIAdapter.OnItemDeleteListener() {
        @Override
        public void onDeleteButtonClick(final int position) {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to delete this record?")
                    .setNegativeButton("cancel", null)
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BMI bmi = data.get(position);
                            db = dbHelper.getWritableDatabase();
                            // Define 'where' part of query.
                            String selection = BMIData.BMIEntry._ID + " = ?";
                            // Specify arguments in placeholder order.
                            String[] selectionArgs = { Long.toString(bmi.getID()) };
                            // Issue SQL statement.
                            int deletedRows = db.delete(BMIData.BMIEntry.TABLE_NAME,
                                    selection, selectionArgs);
                            if (deletedRows == 1) {
                                Snackbar.make(cview,
                                        "This record was deleted successfully",
                                        Snackbar.LENGTH_LONG)
                                        .show();
                            } else {
                                Snackbar.make(cview,
                                        "The record deletion failed",
                                        Snackbar.LENGTH_LONG)
                                        .show();
                            }
                            data.remove(position);
                            bmiAdapter.notifyDataSetChanged();
                            setListViewHeight(listView);
                        }
                    }).show();
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
