package com.example.bmicalculator;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {
    private View view;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.history, container, false);
        listView = view.findViewById(R.id.listBMI);
        getHistory();
        return view;
    }

    private void getHistory() {
        BMIDbHelper dbHelper = new BMIDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }
}
