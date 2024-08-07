package com.example.stolperstein;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.ActivityMainBinding;
import com.example.stolperstein.ui.DialogAbout;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String CacheKMLFileName = "StolpersteineKiel.kml";

    public static HashMap<Integer, List<String>> hashPerson;

    //LocationManager locationManager;
    public static String[] PermsLocation = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET
    };
    public static String[] PermsStorage = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    //public static HashMap<Integer, List<String>> dataPerson;
    public static String web_link = "https://kiel-wiki.de/Stolpersteine";
    public static String preAddress = "Deutschland Schleswig-Holstein Kiel ";

    private AppBarConfiguration mAppBarConfiguration;
    public static ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityCompat.requestPermissions(this, PermsLocation, 2);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_artist, R.id.nav_settings,
                R.id.nav_project, R.id.nav_stones, R.id.nav_app_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // start explicit Fragment
        // https://developer.android.com/guide/navigation/use-graph/programmatic?hl=de
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
        navGraph.setStartDestination(R.id.nav_home); // here the Fragment  to start
        navController.setGraph(navGraph);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // sollte es noch kein kml geben, dann begruessung anzeigen
        if (!FileManager.CacheFileExist(getApplicationContext(), CacheKMLFileName)) {
            Dialog dialog = new Dialog(this);
            dialog.setTitle(R.string.welcome);
            dialog.setContentView(R.layout.dialog_welcome);
            Button buttonClose = dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(v -> dialog.dismiss());
            ImageView imageView = dialog.findViewById(R.id.welcome_image);
            imageView.setCropToPadding(true);
            imageView.setMaxWidth(10);
            imageView.setMaxHeight(10);
            Bitmap img1 = utils.getBitmapFromAsset(dialog.getContext(), "stolperstein_ein_mensch.png");
            imageView.setImageBitmap(img1);
            dialog.create();
            dialog.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Items in Settings, rechts oben
        if (item.getItemId() == R.id.settings_about) {
            String content = getString(R.string.dialog_body_about);
            DialogAbout.show(this, "About", content,"stolperstein_ein_mensch.png");
        }
        if (item.getItemId() == R.id.settings_donat) {
            String content = getString(R.string.dialog_body_me);
            DialogAbout.show(this, "Coffee & Donuts", content, "donut.png");
        }
        if (item.getItemId() == R.id.settings_info) {
            String content = getString(R.string.info_app);
            DialogAbout.show(this, "Info", content, null);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}