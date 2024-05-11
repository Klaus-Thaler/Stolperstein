package com.example.stolperstein.classes;


import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.example.stolperstein.MainActivity.mapView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class getLocation extends Activity {
    public void get(View view, MapView mapView) {
        final LocationManager systemService = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager = new (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            MyLocationNewOverlay mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mapView);
            //IMapController controller = mapView.getController();
            mMyLocationOverlay.setEnabled(false);
            mMyLocationOverlay.enableMyLocation();
            mMyLocationOverlay.enableFollowLocation();
            mMyLocationOverlay.setEnabled(true);
            mapView.getOverlays().add(mMyLocationOverlay);
            mapView.invalidate();
        }
    }
}
