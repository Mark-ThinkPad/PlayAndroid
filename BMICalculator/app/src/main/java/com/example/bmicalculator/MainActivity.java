package com.example.bmicalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private TextView tv1, tv2;
    private TextInputEditText we, he;
    private SeekBar seekBar;
    private DrawerLayout dl;
    private NavigationView nv;
    private MaterialToolbar mt;
    private View homeView, aboutView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViews();
        nv.setCheckedItem(R.id.home);

        mt.setNavigationOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dl.openDrawer(nv);
            }
        });

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        homeView.setVisibility(View.VISIBLE);
                        aboutView.setVisibility(View.GONE);
                        nv.setCheckedItem(R.id.home);
                        dl.closeDrawers();
                        return true;
                    case R.id.about:
                        homeView.setVisibility(View.GONE);
                        aboutView.setVisibility(View.VISIBLE);
                        nv.setCheckedItem(R.id.about);
                        dl.closeDrawers();
                        return true;
                    default:
                        return false;
                }
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("SeekBar", "Get touched");
                return true;
            }
        });
    }

    private void BindViews() {
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        we = findViewById(R.id.ws);
        he = findViewById(R.id.hs);
        seekBar = findViewById(R.id.seekBar);
        dl = findViewById(R.id.dl);
        nv = findViewById(R.id.nav_view);
        mt = findViewById(R.id.topAppBar);
        homeView = findViewById(R.id.homeView);
        aboutView = findViewById(R.id.aboutView);
    }

    public void button1(View v) {
        MaterialAlertDialogBuilder madb =
                new MaterialAlertDialogBuilder(MainActivity.this)
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
                tv1.setTextColor(getColor(R.color.MaterialBlueGray));
                tv2.setTextColor(getColor(R.color.MaterialBlueGray));
            } else if (BMI10 < 240) {
                tv2.setText(R.string.r2);
                tv1.setTextColor(getColor(R.color.MaterialTeal));
                tv2.setTextColor(getColor(R.color.MaterialTeal));
            } else if (BMI10 < 280) {
                tv2.setText(R.string.r3);
                tv1.setTextColor(getColor(R.color.MaterialAmber));
                tv2.setTextColor(getColor(R.color.MaterialAmber));
            } else {
                tv2.setText(R.string.r4);
                tv1.setTextColor(getColor(R.color.MaterialDeepOrange));
                tv2.setTextColor(getColor(R.color.MaterialDeepOrange));
            }

        } catch (Exception e) {
            madb.show();
        }
    }

    public void button2(View v) {
        tv1.setText(null);
        tv2.setText(null);
        seekBar.setProgress(0);
        we.setText(null);
        he.setText(null);
    }
}
