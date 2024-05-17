package com.example.stolperstein.classes;

import static com.example.stolperstein.MainActivity.CacheFileName;
import static com.example.stolperstein.MainActivity.preAddress;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.stolperstein.MainActivity;
import com.example.stolperstein.ui.settings.SettingViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class getWebWorker extends Worker {
    public getWebWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        String mUserAgent = null;
        String website = MainActivity.web_link;
        Locale mLocale = Locale.GERMANY;
        // create a kml file to cache
        StringBuilder kmlFile = new StringBuilder();
        kmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" +
                "<Document>\n");
        // geocoder um revers nach geopoints zu suchen
        int maxResults = 1;
        GeocoderNominatim mGeoCoder = new GeocoderNominatim(mLocale, mUserAgent);
        Elements mRows = null;
        SettingViewModel.mButton.postValue("PLEASE WAIT");

        try {
            Document doc = Jsoup.connect(website).get();
            Elements mTable = doc.select("table tbody");
            mRows = mTable.select("tr");
            Log.i("ST_getWebWorker","mRows: " + mRows.size());
            for (int i = 1; mRows.size() > i; i++) {
            // fuer kurze tests
            //for (int i = 1; 4 > i; i++) {
                //first row is the col names so skip it.
                ArrayList<String> entryTD = new ArrayList<>();
                Elements mTD = mRows.get(i).select("td");
                for (int z = 0; mTD.size() > z; z++) {
                    Log.i("ST_FS_entry", "i " + i + " z " + z + " - " + mTD.get(z).toString());
                    entryTD.add(mTD.get(0).select("span[style=\"display:none;\"]").text()); // name
                    entryTD.add(mTD.get(1).text());                             // adresse
                    entryTD.add(mTD.get(2).text());                             // geboren
                    entryTD.add(mTD.get(3).text());                             // deportiert
                    entryTD.add(mTD.get(4).select("a").attr("href"));    // Link Biografie
                    entryTD.add(mTD.get(5).select("a").attr("href"));    // Link Foto
                    entryTD.add(mTD.get(6).text());                             // verlegt am
                }
                // address zu Geo Daten. Laengen- und Breitengrad
                List<Address> mCoder = mGeoCoder.getFromLocationName(preAddress
                        + Objects.requireNonNull(entryTD.get(1)), maxResults);
                if (!mCoder.isEmpty()) {
                    Log.i("ST_getWebWorker", "mcoder: " + i
                            //+ " " + false
                            + " " + preAddress + Objects.requireNonNull(entryTD.get(1))
                            + " " + mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                    Log.i("ST_FS_td", "index " + i + "-> " + mCoder.get(0).getLongitude() + "+" + mCoder.get(0).getLatitude());
                    SettingViewModel.progBarSet.postValue((i * 100) / mRows.size());
                    SettingViewModel.mSearch.postValue(Objects.requireNonNull(entryTD.get(0)) + "\n"
                            + Objects.requireNonNull(entryTD.get(1)));
                    kmlFile.append("<Placemark>\n") // kml file
                            .append("\t<id>").append(i).append("</id>\n") // id
                            .append("\t\t<title>Stolperstein</title>\n") //name
                            .append("\t\t<description>\n")
                            .append("\t\t\t<name><![CDATA[").append(Objects.requireNonNull(entryTD.get(0))).append("]]></name>\n") //name
                            .append("\t\t\t<address><![CDATA[").append(Objects.requireNonNull(entryTD.get(1))).append("]]></address>\n")   // adresse
                            .append("\t\t</description>\n")
                            .append("\t<Point>\n")
                            .append("\t\t<coordinates>")
                            .append(mCoder.get(0).getLongitude()).append(",").append(mCoder.get(0).getLatitude())
                            .append("</coordinates>\n")
                            .append("\t</Point>\n")
                            .append("\t<data>\n")
                            .append("\t\t<born><![CDATA[").append(Objects.requireNonNull(entryTD.get(2))).append("]]></born>\n")   // geboren
                            .append("\t\t<death><![CDATA[").append(Objects.requireNonNull(entryTD.get(3))).append("]]></death>\n")     // deportiert
                            .append("\t\t<biographie><![CDATA[").append(Objects.requireNonNull(entryTD.get(4))).append("]]></biographie>\n")
                            .append("\t\t<photo><![CDATA[").append(Objects.requireNonNull(entryTD.get(5))).append("]]></photo>\n")
                            .append("\t\t<installed>").append((Objects.requireNonNull(entryTD.get(6)))).append("</installed>\n")
                            .append("\t</data>\n")
                            .append("</Placemark>\n");
                }
            }
            kmlFile.append("</Document>\n</kml>");
            // todo
            // live in karte eintragen
        } catch (IOException e) {
            Log.i("ST_getWebWorker", "result: " + Result.failure());
            throw new RuntimeException(e);
        } finally {
            assert mRows != null;
            SettingViewModel.mSearch.postValue(mRows.size() - 1 + " Stolpersteine gefunden.");
            SettingViewModel.progBarSet.postValue(0);
            SettingViewModel.mButton.postValue("GONE");

            Log.i("ST_getWebWorker", "kmlfile: " + kmlFile);
            // safe in cache
            FileManager.saveCacheFile(getApplicationContext(), MainActivity.CacheFileName, kmlFile.toString());

            // logging
            File cacheFile = FileManager.loadCacheFile(getApplicationContext(), CacheFileName);
            Log.i("ST_getWebWorker", "Cachefile size: " + FileManager.getFileSize(cacheFile));
            Log.i("ST_getWebWorker", "Cachefile modified: " + FileManager.CacheFileLastModified(cacheFile));
        }
        Log.i("ST_getWebWorker", "result: "+ Result.success());
        return Result.success();
    }
}