package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class ResultActivity extends AppCompatActivity {

    private TextView tv_r1, tv_r2, name_r, sex_r;
    private SeekBar seekBar_r;
    private MaterialToolbar mt_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        bindViews();
        mt_r.setNavigationOnClickListener(mc2);
        setResult();
    }

    private void bindViews() {
        tv_r1 = findViewById(R.id.tv_r1);
        tv_r2 = findViewById(R.id.tv_r2);
        name_r = findViewById(R.id.name_r);
        sex_r = findViewById(R.id.sex_r);
        seekBar_r = findViewById(R.id.seekBar_r);
        mt_r = findViewById(R.id.topAppBar_r);
    }


    private View.OnClickListener mc2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void setResult() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        name_r.setText(bundle.getString("name_f"));
        sex_r.setText(bundle.getString("sex_f"));
        tv_r1.setText(bundle.getString("bmi_f"));
        int BMI10 = bundle.getInt("bmi10");
        seekBar_r.setProgress(BMI10);

        if (BMI10 < 185) {
            tv_r2.setText(R.string.r1);
            tv_r1.setTextColor(getColor(R.color.MaterialBlueGray));
            tv_r2.setTextColor(getColor(R.color.MaterialBlueGray));
        } else if (BMI10 < 240) {
            tv_r2.setText(R.string.r2);
            tv_r1.setTextColor(getColor(R.color.MaterialTeal));
            tv_r2.setTextColor(getColor(R.color.MaterialTeal));
        } else if (BMI10 < 280) {
            tv_r2.setText(R.string.r3);
            tv_r1.setTextColor(getColor(R.color.MaterialAmber));
            tv_r2.setTextColor(getColor(R.color.MaterialAmber));
        } else {
            tv_r2.setText(R.string.r4);
            tv_r1.setTextColor(getColor(R.color.MaterialDeepOrange));
            tv_r2.setTextColor(getColor(R.color.MaterialDeepOrange));
        }
    }
}
