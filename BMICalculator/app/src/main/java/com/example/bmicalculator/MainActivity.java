package com.example.bmicalculator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar mt = findViewById(R.id.topAppBar);
        mt.setNavigationOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.textView);
                tv.setText(R.string.clicked);
            }
        });
    }
}
