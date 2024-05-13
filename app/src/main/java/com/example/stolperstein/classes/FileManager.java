package com.example.stolperstein.classes;

import static android.content.ContentValues.TAG;
import static com.example.stolperstein.MainActivity.CacheFileName;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileManager {

    public static Long CacheFileLastModified(Context context, File file) {
        return file.lastModified();
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
    public static void saveDataInDownload(Context context, String fileName){
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //utils.showToast(context,"downloads" + downloads.toString());
        File mExternalFile = new File(downloads, fileName);

        if (externalStorageWrite()) {
            String kmlFile = readCacheFile(context, CacheFileName);
            Log.i("ST_file","else");
            try {
                FileOutputStream fos = new FileOutputStream(mExternalFile);
                fos.write(kmlFile.getBytes());
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, "IOE_saveDataFile: " + e.getMessage());
                utils.showToast(context,"Not saved.");
            } finally {
                utils.showToast(context, "Ready.");
            }
        } else {
            utils.showToast(context,"Saving in Downloads not possible!");
        }
    }
    public static void saveCacheFile(Context context, String filename, String dataString) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(dataString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "IOE_saveCacheFile: " + e.getMessage());
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
        File file = null;
        try {
            file = context.getFileStreamPath(filename);
        } catch (Exception e) {
            Log.e(TAG, "IOE_loadCacheFile: " + e.getMessage());
        }
        return file;
    }
    public static String readCacheFile(Context context, String filename) {
        String data = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(filename);
            InputStream stream = new BufferedInputStream(fileInputStream);
            data = convertStreamToString(stream);
            fileInputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "IOE_readCacheFile: " + e.getMessage());
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
            Log.e(TAG, "IOE_convertStream: " + e.getMessage());
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e(TAG, "IOE_convertStream: " + e.getMessage());
            }
        }
        return stringBuilder.toString();
    }
}
