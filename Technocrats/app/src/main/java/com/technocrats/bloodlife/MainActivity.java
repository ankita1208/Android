package com.technocrats.bloodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.technocrats.bloodlife.R;
import com.technocrats.bloodlife.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ListView drawerList;
    ActivityMainBinding binding;

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private ArrayList<DrawerData> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        goToHomeFragment();
        setNavigationDrawer();
        drawerCloseListener();
        binding.include2.navigationDrawer.setOnClickListener(view -> {
            InitializeFragment();
            openDrawer();
        });

        binding.include2.notification.setOnClickListener(view -> {
            replaceFragment(R.id.flMain, new ProfileFragment());
        });
    }

    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.dlNavigationDrawer, binding.include2.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                View container = findViewById(R.id.container);
                container.setTranslationX(slideOffset * drawerView.getWidth());
            }
        };
        binding.dlNavigationDrawer.addDrawerListener(toggle);
        InitializeFragment();
    }

    public void setToolbarTitle(String title) {
        binding.include2.title.setText(title);
    }

    public void drawerCloseListener() {
        binding.dlNavigationDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    public void openDrawer() {
        binding.dlNavigationDrawer.openDrawer(GravityCompat.START);
    }

    private void InitializeFragment() {
        NavFragment newNavFragment = new NavFragment();
        changeFragment(this, newNavFragment, true, false,
                R.id.containerNav);
    }

    private void goToHomeFragment() {
        changeFragment(this, new HomeFragment(), false, false,
                R.id.flMain);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            InitializeFragment();
            openDrawer();
            return true;
        }
        if (itemId == R.id.notification_item) {
            replaceFragment(R.id.flMain, new ProfileFragment());
            return true;
        } else if (itemId == R.id.menu_phone) {
            dialEmergencyNumber();
            return true;
        } else if (itemId == android.R.id.home) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
            if (!(currentFragment instanceof HomeFragment)) {
                changeHomeFragment(new HomeFragment());
            } else {
                // If it's the HomeFragment, toggle the navigation drawer
                if (binding.dlNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                    binding.dlNavigationDrawer.closeDrawer(GravityCompat.START);
                } else {
                    binding.dlNavigationDrawer.openDrawer(GravityCompat.START);
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("RtlHardcoded")
    public void closeNavigationDrawer() {
        binding.dlNavigationDrawer.closeDrawer(Gravity.LEFT);
    }

    void changeHomeFragment(Fragment fragment) {
        changeFragment(this, fragment, true, false,
                R.id.flMain);
        closeNavigationDrawer();
      //  binding.dlNavigationDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (binding.dlNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.dlNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
            if (fragment instanceof HomeFragment) {
                super.onBackPressed(); // Exit the app if on the HomeFragment
            } else {
                // If on a fragment other than HomeFragment, replace it with HomeFragment
                goToHomeFragment();
            }
        }
    }


    private void dialEmergencyNumber() {
        // Create an Intent with the ACTION_DIAL action
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);

        // Set the phone number to dial
        dialIntent.setData(Uri.parse("tel:" + "911"));

        // Check if there is an app to handle the Intent
        if (dialIntent.resolveActivity(this.getPackageManager()) != null) {
            // Start the dialer activity
            startActivity(dialIntent);
        } else {
            // Handle the case where there is no app to handle the Intent
            Toast.makeText(this, "No app to handle dialer", Toast.LENGTH_SHORT).show();
        }
    }
}