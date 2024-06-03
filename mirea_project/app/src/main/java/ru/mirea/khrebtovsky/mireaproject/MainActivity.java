package ru.mirea.khrebtovsky.mireaproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_data, R.id.nav_webview, R.id.nav_camera, R.id.nav_compass, R.id.nav_micro)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;

                if (id == R.id.nav_data) {
                    selectedFragment = new DataFragment();
                    startWorker();
                }
                if (id == R.id.nav_camera) {
                    selectedFragment = new CameraFragment();
                }
                if (id == R.id.nav_compass) {
                    selectedFragment = new CompassFragment();
                }
                if (id == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }
                if (id == R.id.nav_fragment_file) {
                    selectedFragment = new FileFragment();
                }
                if (id == R.id.nav_micro) {
                    selectedFragment = new MicrophoneFragment();
                }if (id == R.id.nav_fragment_book) {
                    selectedFragment = new BookFragment();
                }else if (id == R.id.nav_webview) {
                    selectedFragment = new WebViewFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, selectedFragment)
                            .commit();
                }

                drawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void startWorker() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SimpleWorker.class).build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }
}
