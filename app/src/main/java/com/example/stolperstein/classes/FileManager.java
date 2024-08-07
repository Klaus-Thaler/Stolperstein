package com.example.stolperstein.classes;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileManager {

    public static String CacheFileLastModified(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        long timeStamp = file.lastModified();
        Date date = new java.util.Date(timeStamp);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY).format(date);
    }
    public static boolean CacheFileExist(Context context, String filename){
        return context.getFileStreamPath(filename).exists();
    }
    public static Boolean externalStorageWrite(){
        String extStorageState = Environment.getExternalStorageState();
        if (extStorageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else if (extStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return false;
        } else {
            return false;
        }
    }
    public static void saveDataInDownload(Context context, String fileName) {
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //utils.showToast(context,"downloads" + downloads.toString());
        File mExternalFile = new File(downloads, fileName);

        if (externalStorageWrite()) {
            String kmlFile = readCacheFile(context, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(mExternalFile);
                fos.write(kmlFile.getBytes());
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            utils.showToast(context,"Saving in Downloads not possible!");
        }
    }
    public static String getFileSize(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        return (int) file.length() / (1024) + " kb";
    }

    public static void saveCacheFile(Context context, String filename, String dataString) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(dataString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean removeCacheFile(Context context, String filename) {
        File file;
        if (CacheFileExist(context, filename)) {
            file = context.getFileStreamPath(filename);
            return file.delete();
        }
        return false;
    }
    public static File loadCacheFile (Context context, String filename) {
        File file;
        try {
            file = context.getFileStreamPath(filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    public static String readCacheFile(Context context, String filename) {
        String data;
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(filename);
            InputStream stream = new BufferedInputStream(fileInputStream);
            data = convertStreamToString(stream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    private static String convertStreamToString(InputStream is) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
