package com.example.stolperstein.classes;


import static com.example.stolperstein.MainActivity.mapView;

import com.example.stolperstein.ui.MapFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class getLocation extends MapFragment {
    public static void get(Boolean follow) {
        //final LocationManager systemService = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager = new (LocationManager) getSystemService(LOCATION_SERVICE);

        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mapView.getContext()), mapView);
        //IMapController controller = mapView.getController();
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
