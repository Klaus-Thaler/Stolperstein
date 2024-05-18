package com.example.stolperstein.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

public class DialogMap {
    //Building dialog
    public static MapView dialogMap;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void show(Context context, String dialogTitle, String mName, String mAddress, String mGeopoint) {
        // zeigt den Dialog Frame nach Klick: show in maps

        utils.showToast(context,"geo: " + mGeopoint);
        double defaultZOOM = 16.;

        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_map);

        dialogMap = new MapView(context);
        dialogMap.findViewById(R.id.dialogMapView);

        //mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        dialogMap.setTileSource(TileSourceFactory.MAPNIK);
        // controller fur osm
        IMapController mapController = dialogMap.getController();
        // start in kiel
        //GeoPoint startPoint = new GeoPoint(defaultLATITUDE, defaultLONGITUDE);
        GeoPoint mPoint = GeoPoint.fromInvertedDoubleString(mGeopoint, ',');
        mapController.setCenter(mPoint);
        mapController.setZoom(defaultZOOM);
        dialogMap.setMultiTouchControls(true);
        dialogMap.setTilesScaledToDpi(true);
        // copyright
        CopyrightOverlay copyrightOverlay = new CopyrightOverlay(context);
        copyrightOverlay.setEnabled(true);
        dialogMap.getOverlays().add(copyrightOverlay);
        // scalebar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(dialogMap);
        dialogMap.getOverlays().add(myScaleBarOverlay);
        //  compass
        CompassOverlay compassOverlay = new CompassOverlay(context,
                new InternalCompassOrientationProvider(context), dialogMap);
        compassOverlay.enableCompass();
        dialogMap.getOverlays().add(compassOverlay);
        dialogMap.invalidate();
        Marker mMarker = new Marker(dialogMap);
        mMarker.setPosition(mPoint);
        mMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMarker.setTitle(mName);
        mMarker.setSnippet(mAddress);
        //mMarker.setSubDescription("SubDescription");
        dialogMap.getOverlays().add(mMarker);
        dialogMap.invalidate();
        dialog.setContentView(dialogMap);

        /*
        // Button close
        Button buttonClose = (Button) dialog.findViewById(R.id.fab_close);
        buttonClose.setOnClickListener(v -> {
            utils.showToast(context,"klick");
            //dialog.dismiss();
        });
        */
        //dialog.create();
        dialog.show();
    }
}
