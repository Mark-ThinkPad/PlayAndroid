package com.example.httpandjson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private NavigationView nv;
    private MaterialToolbar mt;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment hf;
    private AboutFragment af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        BindViews();
        initHome();
        mt.setNavigationOnClickListener(mc);
    }

    private void BindViews() {
        dl = findViewById(R.id.dl);
        nv = findViewById(R.id.nav_view);
        mt = findViewById(R.id.topAppBar);
    }

    private void initHome() {
        ft = fm.beginTransaction();
        hf = new HomeFragment();
        ft.add(R.id.content, hf);
        ft.commit();
        nv.setCheckedItem(R.id.home);
    }

    private View.OnClickListener mc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dl.openDrawer(nv);
        }
    };

    private void hideAllFragment(FragmentTransaction ft){
        if (hf != null) ft.hide(hf);
        if (af != null) ft.hide(af);
    }

    public void itemClick(MenuItem item) {
        ft = fm.beginTransaction();
        hideAllFragment(ft);
        switch (item.getItemId()) {
            case R.id.home:
                if (hf == null) {
                    hf = new HomeFragment();
                    ft.add(R.id.content, hf);
                } else {
                    ft.show(hf);
                }
                break;
            case R.id.about:
                if (af == null) {
                    af = new AboutFragment();
                    ft.add(R.id.content, af);
                } else {
                    ft.show(af);
                }
                break;
            default:
                break;
        }
        dl.closeDrawers();
        ft.commit();
    }
}
