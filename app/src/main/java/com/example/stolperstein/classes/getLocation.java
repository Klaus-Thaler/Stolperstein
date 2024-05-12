package com.example.stolperstein.classes;


import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.example.stolperstein.MainActivity.mapView;
import static com.example.stolperstein.ui.CardFragment.mMyLocationOverlay;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.stolperstein.MainActivity;
import com.example.stolperstein.ui.CardFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class getLocation extends CardFragment {
    public static void get(Boolean follow) {
        //final LocationManager systemService = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager = new (LocationManager) getSystemService(LOCATION_SERVICE);

        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mapView.getContext()), mapView);
        IMapController controller = mapView.getController();
        if (follow) {
            mMyLocationOverlay.enableFollowLocation();
        } else{
            mMyLocationOverlay.disableFollowLocation();
        }
        mMyLocationOverlay.enableMyLocation();
        mMyLocationOverlay.setEnabled(true);
        mapView.getOverlays().add(mMyLocationOverlay);
        mapView.invalidate();
    }
}
