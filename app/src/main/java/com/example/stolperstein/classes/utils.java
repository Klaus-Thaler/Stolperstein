package com.example.stolperstein.classes;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

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
        String string;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            try {
                if ((string = br.readLine()) == null) break; }
            catch (IOException e) {
                throw new RuntimeException(e); }
            sb.append(string).append("\n");
        }
        return sb.toString();
    }
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        // Asset Bilder holen und bereitstellen
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
