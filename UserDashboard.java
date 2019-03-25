package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class UserDashboard extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigate;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar =findViewById(R.id.toolBar);
        navigate=findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv = findViewById(R.id.toolbarText);
        tv.setText("Pewds Tourism Portal");
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                try{
                switch (menuItem.getItemId())
                {
                    case R.id.SearchPackages:
                        tv.setText("Search Packages");
                        search_package sp = new search_package();
                        loadFragment(sp);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.BuildCustomPackages:
                        tv.setText("Build Custom Package");
                        loadFragment(new CustomFrag());
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.CancelPackages:
                        tv.setText("Cancel Package");
                        loadFragment(new cancelfrag());
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.DomesticGuide:
                        tv.setText("Domestic Guide");
                        loadFragment(new guidefrag());
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.ViewBookedPackages:
                        tv.setText("Booked Packages");
                        loadFragment(new viewbookedpackages());
                        drawerLayout.closeDrawers();
                        return true;
                        }}catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;

            }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        //FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content_frame, fragment);
        //fragmentTransaction.add(R.id.firstFragment,fragment);
        fragmentTransaction.commit(); // save the changes

    }
}
