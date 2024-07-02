package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.CacheKMLFileName;
import static com.example.stolperstein.MainActivity.PermsLocation;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.databinding.FragmentMapBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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

public class MapFragment extends Fragment {
    //public static MapView mapView;
    public static MyLocationNewOverlay mMyLocationOverlay;
    private MapView mapView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentMapBinding binding = FragmentMapBinding.inflate(inflater, container, false);
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

        // location klick
        binding.fab.setOnClickListener(view -> {
            Dialog dialog = new Dialog(view.getContext());
            dialog.setTitle(R.string.location);
            dialog.setContentView(R.layout.dialog_location);
            // Dialog Button close
            Button buttonClose = dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(v -> dialog.dismiss());
            // Dialog Button follow
            Button buttonNo = dialog.findViewById(R.id.button_yes);
            buttonNo.setOnClickListener(v -> {
                getLocation(true);
                dialog.dismiss();
            });
            // Dialog Button no follow
            Button buttonYes = dialog.findViewById(R.id.button_no);
            buttonYes.setOnClickListener(v -> {
                getLocation(false);
                dialog.dismiss();
            });
            dialog.create();
            dialog.show();
        });

        if (FileManager.CacheFileExist(requireActivity(),CacheKMLFileName)) {
            // cache file
            // parse den kml file aus dem cache
            File mCacheFile = FileManager.loadCacheFile(requireActivity(), CacheKMLFileName);
            KmlDocument kmlDoc = new KmlDocument();
            kmlDoc.parseKMLFile(mCacheFile);
            String erg = FileManager.readCacheFile(requireActivity(), CacheKMLFileName);
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDoc.mKmlRoot.buildOverlay(
                    mapView, null, null, kmlDoc);
            mapView.getOverlays().add(kmlOverlay);
            mapView.invalidate();
        }
        return root;
    }
    public void getLocation (Boolean follow) {
        ActivityCompat.requestPermissions(requireActivity(), PermsLocation, 2);
        mMyLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireContext()), mapView);
        mMyLocationOverlay.enableMyLocation();
        if (follow) {
            mMyLocationOverlay.enableFollowLocation();
        } else {
            mMyLocationOverlay.disableFollowLocation();
        }
        mMyLocationOverlay.setEnabled(true);
        mapView.getOverlays().add(mMyLocationOverlay);
        mapView.invalidate();
    }
    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        //if (mapView != null) { mapView.onResume(); }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        //if (mapView != null) { mapView.onPause(); }
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}