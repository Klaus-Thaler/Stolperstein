package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.CacheXMLData;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Objects;

public class MapFragment extends Fragment {
    //public static MapView mapView;
    public static MyLocationNewOverlay mMyLocationOverlay;
    private MapView mapView;
    private StringBuilder kmlFile;

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

        // kml file zusammenstellen
        if (FileManager.CacheFileExist(requireContext(), CacheXMLData)) {
            String xmlFile = FileManager.readCacheFile(requireContext(), CacheXMLData);
            Document Doc = Jsoup.parse(xmlFile, "utf-8");
            Elements data = Doc.select("person");

            Log.i("ST_MapFragment"," " + xmlFile);

            for (int z = 0; data.size() > z; z++) {
                if (!Objects.equals(data.get(z).getElementsByTag("coordinates").text(), "null")) {
                    Log.i("ST_MapFragment", " " + Objects.requireNonNull(data.get(z).getElementsByTag("coordinates").text()));

                    Marker mMarker = new Marker(mapView);
                    mMarker.setPosition(GeoPoint.fromInvertedDoubleString(data.get(z).getElementsByTag("coordinates").text(), ','));
                    mMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMarker.setTitle(data.get(z).getElementsByTag("address").text());
                    mMarker.setSnippet(data.get(z).getElementsByTag("name").text());
                    //mMarker.setSubDescription("SubDescription");
                    mapView.getOverlays().add(mMarker);
                    mapView.invalidate();
                }

            }
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