package com.example.d1vtask;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager2;
    private MyViewPager2Adapter myViewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNav();
        setupViewpager2();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D1V TASK");

    }

    public void setupBottomNav() {
        mBottomNavigationView = findViewById(R.id.bottom_nav);
        mBottomNavigationView.getMenu().findItem(R.id.bottom_boards).setChecked(true);
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_boards:
                        mViewPager2.setCurrentItem(0);
                        getSupportActionBar().setTitle("D1V TASK");
//                        getSupportActionBar().setIcon(R.drawable.ic_boards);
                        break;

                    case R.id.bottom_myCards:
                        mViewPager2.setCurrentItem(1);
                        getSupportActionBar().setTitle(R.string.nav_my_cards);
                        break;

                    case R.id.bottom_search:
                        mViewPager2.setCurrentItem(2);
                        getSupportActionBar().setTitle(R.string.nav_search);
                        break;

                    case R.id.bottom_notification:
                        mViewPager2.setCurrentItem(3);
                        getSupportActionBar().setTitle(R.string.nav_notification);
                        break;

                    case R.id.bottom_account:
                        mViewPager2.setCurrentItem(4);
                        getSupportActionBar().setTitle(R.string.nav_account);
                        break;
                }

                return true;
            }
        });
    }

    public void setupViewpager2() {
        mViewPager2 = findViewById(R.id.viewpage2);
        myViewPager2Adapter = new MyViewPager2Adapter(this);
        mViewPager2.setAdapter(myViewPager2Adapter);
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch ( position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_boards).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_myCards).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_search).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_notification).setChecked(true);
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_account).setChecked(true);
                        break;
                }
            }
        });
    }


}