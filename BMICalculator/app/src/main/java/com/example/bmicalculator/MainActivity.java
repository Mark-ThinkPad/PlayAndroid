package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private HomeFragment hf;
    private FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout dl = findViewById(R.id.dl);
        final NavigationView nv = findViewById(R.id.nav_view);
        final MaterialToolbar mt = findViewById(R.id.topAppBar);
        final MenuItem home = findViewById(R.id.home);

        fManager = getSupportFragmentManager();
        nv.setCheckedItem(R.id.home);
        // 获取menu item的view
        // 参考1: https://developer.android.com/training/appbar/action-views?hl=zh-cn
        // 参考2: https://blog.csdn.net/u010607467/article/details/50354999
        // home.performClick();
        // 或者主页硬嵌入, 侧拉菜单的监听器式声明改回去, seek bar禁止滑动再试试
        // 参考: https://blog.csdn.net/mvpstevenlin/article/details/64488065

        mt.setNavigationOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dl.openDrawer(nv);
            }
        });
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(hf != null)fragmentTransaction.hide(hf);
    }

    public void button1(View v) {
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextInputEditText we = findViewById(R.id.ws);
        TextInputEditText he = findViewById(R.id.hs);
        SeekBar seekBar = findViewById(R.id.seekBar);

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
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextInputEditText we = findViewById(R.id.ws);
        TextInputEditText he = findViewById(R.id.hs);
        SeekBar seekBar = findViewById(R.id.seekBar);

        tv1.setText(null);
        tv2.setText(null);
        seekBar.setProgress(0);
        we.setText(null);
        he.setText(null);
    }

    public void MenuItemClick(MenuItem item) {
        DrawerLayout dl = findViewById(R.id.dl);
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (item.getItemId()) {
            case R.id.home:
                if (hf == null) {
                    hf = new HomeFragment();
                    fTransaction.add(R.id.fl, hf);
                } else {
                    fTransaction.show(hf);
                }
            case R.id.about:
            default:
                dl.closeDrawers();
        }
        fTransaction.commit();
    }
}
