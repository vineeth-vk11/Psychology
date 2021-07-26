package com.psychology;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psychology.EntriesUI.ERTEntryActivity;
import com.psychology.MainUI.AccountFragment;
import com.psychology.MainUI.EntriesFragment;
import com.psychology.MainUI.HomeFragment;
import com.psychology.StatsUI.StatisticsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    EntriesFragment entriesFragment = new EntriesFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.home_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFragment).commit();
                return true;
            case R.id.entries:
                Intent intent = new Intent(getApplicationContext(), ERTEntryActivity.class);
                startActivity(intent);
                return true;
            case R.id.stats:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,statisticsFragment).commit();
                return true;
            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,accountFragment).commit();
                return true;
        }

        return false;
    }
}