package com.example.bmicalculator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout dl = findViewById(R.id.dl);
        final NavigationView nv = findViewById(R.id.nav_view);
        final MaterialToolbar mt = findViewById(R.id.topAppBar);
        final TextView tv = findViewById(R.id.textView);

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
                        tv.setText(R.string.clicked);
                    default:
                        dl.closeDrawers();
                }

                return false;
            }
        });
    }
}
