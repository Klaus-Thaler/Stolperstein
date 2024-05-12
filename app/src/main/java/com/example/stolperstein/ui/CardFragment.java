package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.CacheFileName;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.databinding.FragmentHomeBinding;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.Objects;


public class CardFragment extends Fragment {
        private FragmentHomeBinding binding;
        public static MapView mapView;
        public static MyLocationNewOverlay mMyLocationOverlay;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Start Position
        // https://www.openstreetmap.org/relation/62763#map=11/54.3413/10.1260
        double defaultLATITUDE = 54.3413;
        double defaultLONGITUDE = 10.1260;
        double defaultZOOM = 13.;

        // osm map laden
        mapView = binding.mapView;
        //mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        // controller fur osm
        IMapController mapController = mapView.getController();
        // start in kiel
        GeoPoint startPoint = new GeoPoint(defaultLATITUDE, defaultLONGITUDE);
        mapController.setCenter(startPoint);
        mapController.setZoom(defaultZOOM);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        // copyright
        CopyrightOverlay copyrightOverlay = new CopyrightOverlay(requireContext());
        copyrightOverlay.setEnabled(true);
        mapView.getOverlays().add(copyrightOverlay);
        // scalebar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(mapView);
        mapView.getOverlays().add(myScaleBarOverlay);
        //  compass
        CompassOverlay compassOverlay = new CompassOverlay(requireContext(),
                new InternalCompassOrientationProvider(requireContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
        mapView.invalidate();

        //mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        //IMapController controller = mapView.getController();
        //mMyLocationOverlay.setEnabled(false);
        //mMyLocationOverlay.enableMyLocation();
        //mMyLocationOverlay.setEnabled(true);
        //mapView.getOverlays().add(mMyLocationOverlay);
        //mapView.invalidate();

        if (FileManager.CacheFileExist(requireActivity(),CacheFileName)) {
            // cache file
            // parse den kml file aus dem cache
            File data = FileManager.loadCacheFile(requireActivity(), CacheFileName);
            KmlDocument kmlDoc = new KmlDocument();
            kmlDoc.parseKMLFile(data);
            Log.i("ST_readcache",  "data " + data);
            String erg = FileManager.readCacheFile(requireActivity(), CacheFileName);
            Log.i("ST_readfile", "readfile: " + erg);
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDoc.mKmlRoot.buildOverlay(mapView, null, null, kmlDoc);
            mapView.getOverlays().add(kmlOverlay);
            mapView.invalidate();
        }

        return root;
    }
    /*
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        if (mapView != null) { mapView.onResume(); }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        if (mapView != null) { mapView.onPause(); }
    }
}