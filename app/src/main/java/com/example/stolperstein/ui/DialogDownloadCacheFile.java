package com.example.stolperstein.ui;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;
import static com.example.stolperstein.MainActivity.CacheXMLData;

public class DialogDownloadCacheFile {
    public FileManager fileManager;
    public static void show(Context context, String dialogTitle) {
        // zeigt den Dialog Download Cachefile in Downloads
        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_download_cachefile);

        Button buttonDownload = (Button) dialog.findViewById(R.id.button_download);

        if (FileManager.CacheFileExist(context, CacheXMLData)) {
            buttonDownload.setOnClickListener(v -> {
                FileManager.saveDataInDownload(context, CacheXMLData);
                dialog.dismiss();
            });
        } else {
            utils.showToast(context,"No data available yet. Please run Setting first.");
        }

        Button buttonClose = (Button) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
