package com.example.stolperstein.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.stolperstein.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

public class DialogSingleMap {
    //Building dialog
    public static void show(Context context, String dialogTitle, String mName, String mAddress, String mGeopoint) {
        // zeigt den Dialog Frame nach Klick: show in maps

        //utils.showToast(context,"geo: " + mGeopoint);
        double defaultZOOM = 14.;

        Dialog mapDialog = new Dialog(context);
        mapDialog.setTitle(dialogTitle);
        mapDialog.setContentView(R.layout.dialog_map);

        MapView dialogMap = (MapView) mapDialog.findViewById(R.id.dialogMapView);


        dialogMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        // controller fur osm
        IMapController mapController = dialogMap.getController();
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
        // set Marker
        Marker mMarker = new Marker(dialogMap);
        mMarker.setPosition(mPoint);
        mMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMarker.setTitle(mName);
        mMarker.setSnippet(mAddress);
        //mMarker.setSubDescription("SubDescription");
        dialogMap.getOverlays().add(mMarker);
        dialogMap.invalidate();
        // button close
        Button buttonClose = (Button) mapDialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> mapDialog.dismiss());
        // show
        mapDialog.create();
        mapDialog.show();
    }

}
