package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private NavigationView nv;
    private MaterialToolbar mt;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment fh;
    private Home2Fragment fh2;
    private AboutFragment af;
    private HistoryFragment hisf;

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
        fh = new HomeFragment();
        ft.add(R.id.content, fh);
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
        if (fh != null) ft.hide(fh);
        if (fh2 != null) ft.hide(fh2);
        if (af != null) ft.hide(af);
        if (hisf != null) ft.hide(hisf);
    }

    public void itemClick(MenuItem item) {
        ft = fm.beginTransaction();
        hideAllFragment(ft);
        switch (item.getItemId()) {
            case R.id.home:
                if (fh == null) {
                    fh = new HomeFragment();
                    ft.add(R.id.content, fh);
                } else {
                    ft.show(fh);
                }
                nv.setCheckedItem(R.id.home);
                break;
            case R.id.home2:
                if (fh2 == null) {
                    fh2 = new Home2Fragment();
                    ft.add(R.id.content, fh2);
                } else {
                    ft.show(fh2);
                }
                nv.setCheckedItem(R.id.home2);
                break;
            case R.id.about:
                if (af == null) {
                    af = new AboutFragment();
                    ft.add(R.id.content, af);
                } else {
                    ft.show(af);
                }
                nv.setCheckedItem(R.id.about);
                break;
            case R.id.history:
                if (hisf == null) {
                    hisf = new HistoryFragment();
                    ft.add(R.id.content, hisf);
                } else {
                    ft.show(hisf);
                }
                nv.setCheckedItem(R.id.history);
                break;
            default:
                break;
        }
        dl.closeDrawers();
        ft.commit();
    }
}
