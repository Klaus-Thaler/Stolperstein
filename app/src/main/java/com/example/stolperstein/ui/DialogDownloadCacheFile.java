package com.example.stolperstein.ui;


import static com.example.stolperstein.MainActivity.CacheKMLFileName;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;

public class DialogDownloadCacheFile {
    public static void show(Context context, String dialogTitle) {
        // zeigt den Dialog Download Cachefile in Downloads
        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_download_cachefile);

        Button buttonDownload = dialog.findViewById(R.id.button_download);

        if (FileManager.CacheFileExist(context, CacheKMLFileName)) {
            buttonDownload.setOnClickListener(v -> {
                FileManager.saveDataInDownload(context, CacheKMLFileName);
                dialog.dismiss();
            });
        } else {
            utils.showToast(context,"No data available yet. Please run Setting first.");
        }

        Button buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
