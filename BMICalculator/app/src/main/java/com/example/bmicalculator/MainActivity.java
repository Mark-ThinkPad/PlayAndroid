package com.example.bmicalculator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout dl = findViewById(R.id.dl);
        final NavigationView nv = findViewById(R.id.nav_view);
        final MaterialToolbar mt = findViewById(R.id.topAppBar);
        final Button button1 = findViewById(R.id.cal);
        final Button button2 = findViewById(R.id.reset);
        final TextView tv1 = findViewById(R.id.tv1);
        final TextView tv2 = findViewById(R.id.tv2);
        final SeekBar seekBar = findViewById(R.id.seekBar);

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
                    case R.id.about:
                    default:
                        dl.closeDrawers();
                }

                return false;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextInputEditText we = findViewById(R.id.weight);
                    String ws = Objects.requireNonNull(we.getText()).toString();
                    TextInputEditText he = findViewById(R.id.height);
                    String hs = Objects.requireNonNull(he.getText()).toString();
                } catch (Exception e) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Warning")
                            .setMessage("Please enter number")
                            .setPositiveButton("accept", null)
                            .show();
                    return;
                }
            }
        });
    }
}
