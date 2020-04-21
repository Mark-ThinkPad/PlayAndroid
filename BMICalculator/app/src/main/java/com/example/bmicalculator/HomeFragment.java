package com.example.bmicalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View view;
    private TextView tv1, tv2;
    private TextInputEditText we, he;
    private SeekBar seekBar;
    private Button cal, reset;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home,container,false);
        bindViews();

        cal.setOnClickListener(button1);
        reset.setOnClickListener(button2);
        seekBar.setOnTouchListener(sbt);

        return view;
    }

    private void bindViews() {
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        we = view.findViewById(R.id.ws);
        he = view.findViewById(R.id.hs);
        seekBar = view.findViewById(R.id.seekBar);
        cal = view.findViewById(R.id.cal);
        reset = view.findViewById(R.id.reset);
    }

    private View.OnTouchListener sbt = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnClickListener button1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialAlertDialogBuilder madb =
                    new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                            .setTitle("Warning")
                            .setMessage("Please enter right number")
                            .setPositiveButton("accept", null);

            try {
                String ws = Objects.requireNonNull(we.getText()).toString();
                String hs = Objects.requireNonNull(he.getText()).toString();
                float weight = Float.parseFloat(ws);
                float height = Float.parseFloat(hs);

                if (weight==0 || height==0) {
                    madb.show();
                    return;
                }

                float rawResult = weight / height / height;
                int BMI10 = (int) (rawResult * 10);

                seekBar.setProgress(BMI10);
                tv1.setText(new DecimalFormat("0.0").format(rawResult));

                if (BMI10 < 185) {
                    tv2.setText(R.string.r1);
                    tv1.setTextColor(getResources().getColor(R.color.MaterialBlueGray));
                    tv2.setTextColor(getResources().getColor(R.color.MaterialBlueGray));
                } else if (BMI10 < 240) {
                    tv2.setText(R.string.r2);
                    tv1.setTextColor(getResources().getColor(R.color.MaterialTeal));
                    tv2.setTextColor(getResources().getColor(R.color.MaterialTeal));
                } else if (BMI10 < 280) {
                    tv2.setText(R.string.r3);
                    tv1.setTextColor(getResources().getColor(R.color.MaterialAmber));
                    tv2.setTextColor(getResources().getColor(R.color.MaterialAmber));
                } else {
                    tv2.setText(R.string.r4);
                    tv1.setTextColor(getResources().getColor(R.color.MaterialDeepOrange));
                    tv2.setTextColor(getResources().getColor(R.color.MaterialDeepOrange));
                }

            } catch (Exception e) {
                madb.show();
            }
        }
    };

    private View.OnClickListener button2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tv1.setText(null);
            tv2.setText(null);
            seekBar.setProgress(0);
            we.setText(null);
            he.setText(null);
        }
    };
}
