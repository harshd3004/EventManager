package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ImageButton openMenuBtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openMenuBtn = (ImageButton) findViewById(R.id.btnMenuBar);
        openMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSidebar();
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigateToFragment(item.getItemId());
                return true;
            }
        });

        // Open HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void openSidebar() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }

    private void navigateToFragment(int itemId){
        if (itemId == R.id.nav_home) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (itemId == R.id.nav_allevents) {
            loadFragment(new AllEventsFragment());
            navigationView.setCheckedItem(R.id.nav_allevents);
        }
//        else if (itemId == R.id.nav_myevents) {
//            loadFragment(new MyEventsFragment());
//            navigationView.setCheckedItem(R.id.nav_myevents);
//        }
        else if(itemId == R.id.nav_organize){
            loadFragment(new OrganizeFragment());
            navigationView.setCheckedItem(R.id.nav_organize);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}