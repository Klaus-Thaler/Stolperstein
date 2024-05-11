package com.example.stolperstein.classes;

import static com.example.stolperstein.MainActivity.binding;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stolperstein.R;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class utils {
    public static void showToast(Context mContext, String message) {
        // Toast anzeigen
        if (message != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    // read raw resource file
    public static String get_text(InputStream inputStream) {
        // String lesen und bereitstellen
        StringBuilder sb = new StringBuilder();
        String string = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            try {
                if ((string = br.readLine()) == null) break; }
            catch (IOException e) {
                e.printStackTrace(); }
            sb.append(string).append("\n");
        }
        return sb.toString();
    }
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        // Asset Bilder holen und bereitstellen
        Bitmap img1 = null;
        InputStream inStream;
        try {
            AssetManager assetManager = context.getResources().getAssets();
            inStream = assetManager.open(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BitmapFactory.decodeStream(inStream);
    }
}
