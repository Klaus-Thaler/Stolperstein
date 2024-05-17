package com.example.stolperstein.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;

import java.io.InputStream;

public class DialogAbout {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void show(Context context, String dialogTitle,
                            Integer resourceRaw, String resourcePic) {
        // zeigt den Dialog Frame nach Klick Settings; about, contact, coffee, ...
        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_about);
        InputStream inputStream = context.getResources().openRawResource(resourceRaw);
        String html = utils.get_text(inputStream);
        WebView webView = (WebView) dialog.findViewById(R.id.webcontent_html);
        webView.loadData(html, "text/html","UTF-8");
        webView.setBackgroundColor(0x00000000);
        TextView textView = (TextView) dialog.findViewById(R.id.title_html);
        textView.setText(dialogTitle);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.image_html);
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(dialog.getContext(), resourcePic);
        imageView.setImageBitmap(img1);
        
        Button buttonClose = (Button) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
