package com.example.stolperstein.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;

public class DialogAbout {
    public static void show(Context context, String dialogTitle,
                            String dialogText, String resourcePic) {
        // zeigt den Dialog Frame nach Klick Settings; about, contact, coffee, ...
        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_about);

        TextView bodyText = (TextView) dialog.findViewById(R.id.dialog_body);
        bodyText.setText(dialogText);

        if (resourcePic != null) {
            ImageView imageView = (ImageView) dialog.findViewById(R.id.dialog_image);
            imageView.setCropToPadding(true);
            imageView.setMaxWidth(10);
            imageView.setMaxHeight(10);
            Bitmap img1 = utils.getBitmapFromAsset(dialog.getContext(), resourcePic);
            imageView.setImageBitmap(img1);
        }
        Button buttonClose = (Button) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
