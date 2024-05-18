package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.CacheFileName;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;

public class DialogDownloadCacheFile {
    public FileManager fileManager;
    public static void show(Context context, String dialogTitle) {
        // zeigt den Dialog Download Cachefile in Downloads
        Dialog dialog = new Dialog(context);
        dialog.setTitle(dialogTitle);
        dialog.setContentView(R.layout.dialog_download_cachefile);

        Button buttonDownload = (Button) dialog.findViewById(R.id.button_download);
        if (FileManager.CacheFileExist(context, CacheFileName)) {
            buttonDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FileManager.saveDataInDownload(context, CacheFileName);
                    dialog.dismiss();
                }
            });
        } else {
            utils.showToast(context,"No data available yet. Please run Setting first.");
        }
        Button buttonClose = (Button) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { dialog.dismiss(); }
        });
        dialog.show();
    }
}
