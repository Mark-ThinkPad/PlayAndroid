package com.example.bmicalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Objects;


public class Home2Fragment extends Fragment {

    private View view;
    private TextInputEditText we2, he2, name2;
    private Button cal2, reset2;
    private ChipGroup sex2;
    private BMIDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home2, container,false);
        bindViews();
        dbHelper = new BMIDbHelper(getContext());
        cal2.setOnClickListener(button1);
        reset2.setOnClickListener(button2);
        return view;
    }

    private void bindViews() {
        we2 = view.findViewById(R.id.ws2);
        he2 = view.findViewById(R.id.hs2);
        name2 = view.findViewById(R.id.name2);
        cal2 = view.findViewById(R.id.cal2);
        reset2 = view.findViewById(R.id.reset2);
        sex2 = view.findViewById(R.id.sex2);
    }

    private View.OnClickListener button1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialAlertDialogBuilder madb =
                    new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                            .setTitle("Warning")
                            .setMessage("Please enter complete and correct data.")
                            .setPositiveButton("accept", null);

            try {
                String ws = Objects.requireNonNull(we2.getText()).toString();
                String hs = Objects.requireNonNull(he2.getText()).toString();
                float weight = Float.parseFloat(ws);
                float height = Float.parseFloat(hs);
                if (weight==0 || height==0) {
                    madb.show();
                    return;
                }

                String name_f = Objects.requireNonNull(name2.getText()).toString().trim();
                if (name_f.equals("")) {
                    madb.show();
                    return;
                }

                String sex_f = null;
                switch (sex2.getCheckedChipId()) {
                    case R.id.male:
                        sex_f = "male";
                        break;
                    case R.id.female:
                        sex_f = "female";
                        break;
                    case View.NO_ID:
                        madb.show();
                        return;
                    default:
                        break;
                }

                float rawResult = weight / height / height;
                int BMI10 = (int) (rawResult * 10);
                String res_f = new DecimalFormat("0.0").format(rawResult);

                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(BMIData.BMIEntry.COLUMN_NAME_NAME, name_f);
                values.put(BMIData.BMIEntry.COLUMN_NAME_SEX, sex_f);
                values.put(BMIData.BMIEntry.COLUMN_NAME_HEIGHT, height);
                values.put(BMIData.BMIEntry.COLUMN_NAME_WEIGHT, weight);
                values.put(BMIData.BMIEntry.COLUMN_NAME_BMI, rawResult);
                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(BMIData.BMIEntry.TABLE_NAME, null, values);
                if (newRowId == -1) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Error")
                            .setMessage("Your data cannot be saved to local history.")
                            .setPositiveButton("accept", null)
                            .show();
                }

                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("name_f", name_f);
                bundle.putString("sex_f", sex_f);
                bundle.putString("bmi_f", res_f);
                bundle.putInt("bmi10", BMI10);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            } catch (Exception e) {
                madb.show();
            }
        }
    };

    private View.OnClickListener button2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            we2.setText(null);
            he2.setText(null);
            name2.setText(null);
            sex2.clearCheck();
        }
    };

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
