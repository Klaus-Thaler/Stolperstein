package com.example.stolperstein.ui.WebData;


import static com.example.stolperstein.MainActivity.CacheKMLFileName;
import static com.example.stolperstein.MainActivity.preAddress;
import static com.example.stolperstein.classes.sqlHandler.getInstance;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.stolperstein.MainActivity;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.sqlHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.text.Regex;

// todo hashmap gegen db tauschen
public class getWebWorker extends Worker {
    sqlHandler sqlHandler;
    public getWebWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        String mUserAgent = null;
        String website = MainActivity.web_link;
        Locale mLocale = Locale.GERMANY;

        // creating a new db handler class
        sqlHandler = getInstance(getApplicationContext());
        // alte DB loeschen
        sqlHandler.clearDB("person");
        sqlHandler.clearDB("address");

        StringBuilder kmlFile = new StringBuilder();

        Elements mRows = null;
        SettingViewModel.mButton.postValue("PLEASE WAIT");
        int numberAddress = 0;
        try {
            /*
            Problem: Eine Hausadresse kann mehrere Stolpersteine haben. Also wuerden sich
            verschiedene Stolpersteine mit gleicher Adresse in der Map ueberlagern.
            Darum 2 Hashmaps. Eine fuer die Stolpersteine selbst in der Anzeige
            im NameFragment und eine fuer die KML-Geodaten im MapFragment.
             */

            //TreeMap<String, ArrayList<String>> resultWeb = new TreeMap<>(); // alphabetische HashMap
            //HashMap<String, String> resultAddress = new HashMap<>(); // addressen HashMap

            HashMap<String,String> localPoint = new HashMap<>();

            Document doc = Jsoup.connect(website).get();
            Elements mTable = doc.select("table tbody");
            mRows = mTable.select("tr");
            // tabelle reihenweise auslesen
            //Log.i("ST_getWebWorker", "mRows: " + mRows.size());

            for (int i = 1; mRows.size() > i; i++) {
            //for (int i = 1; 10 > i; i++) { // fuer kurze tests
                //first row is the col names so skip it.
                Elements mTD = mRows.get(i).select("td");

                // arguments for sql
                List<String> args = new ArrayList<>();
                args.add(mTD.get(0).select("span[style=\"display:none;\"]").text()); //name
                // alles in klammern raus
                // todo eckige klammern auch raus
                Regex reg = new Regex("\\(.*\\)");
                String newAddress = reg.replace(mTD.get(1).text(),"");
                args.add(newAddress); //addresse
                args.add(mTD.get(2).text()); //geboren
                args.add(mTD.get(3).text()); //deportiert
                args.add(mTD.get(4).select("a").attr("href")); //bio
                args.add(mTD.get(5).select("a").attr("href")); //foto
                args.add(mTD.get(6).text()); //verlegt
                sqlHandler.addNewName("person", args);
                Log.i("ST_getWebWorker", "person: " + args.get(0));
                // hashmap fur mcoder
                localPoint.put(newAddress, null);
            }
            //Log.i("ST_getWebWorker","-> " + localPoint.toString());

            // geopoints suchen aus HashMap localpoint und der DB hinzufugen
            // geocoder um revers nach geopoints zu suchen
            int maxResults = 1;
            GeocoderNominatim mGeoCoder = new GeocoderNominatim(mLocale, mUserAgent);
            for (Object keyAddress : localPoint.keySet()) {
                // hier mcoder
                // address zu Geo Daten. Laengen- und Breitengrad
                List<Address> mCoder = mGeoCoder.getFromLocationName(preAddress
                        + Objects.requireNonNull(keyAddress), maxResults);
                if (!mCoder.isEmpty()) {
                    String mGeo = mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude();
                    // progress
                    SettingViewModel.progBarSet.postValue((numberAddress * 100) / localPoint.size());  // setting in Progressbar
                    SettingViewModel.mSearch.postValue(keyAddress + "\n"
                            + mGeo);
                    localPoint.put(keyAddress.toString(),mGeo);
                    // arguments for sql
                    List<String> args = new ArrayList<>();
                    args.add(keyAddress.toString());
                    args.add(mGeo);
                    sqlHandler.addNewGeoPoint("address", args);
                    //Log.i("ST_getWebWorker","-> " + keyAddress);
                    //Log.i("ST_getWebWorker","-> " + mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                } else {
                    SettingViewModel.mSearch.postValue(keyAddress + "\n"
                            + "error: address no found");
                    //Log.i("ST_getWebWorker", "mcoder error: " + keyAddress);
                }
                numberAddress++;
            }

            kmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">")
                    .append("<Document>\n");
            for (String keyAddress : localPoint.keySet()) {
                if (!(localPoint.get(keyAddress) == null)) {
                    kmlFile.append("<Placemark>\n") // kml file
                            .append("\t\t<name>")
                            .append(keyAddress)
                            .append("</name>\n")
                            .append("\t\t<description>");
                    // db abfrage nach geopoints

                    sqlHandler = getInstance(getApplicationContext());
                    List<String> queryDB = sqlHandler.getNamesInAddress(keyAddress);
                    Log.i("ST_getWebWorker", "query: " + queryDB.toString());
                    for (String entry : queryDB) {
                        kmlFile.append(" -> ").append(entry);
                    }

                    kmlFile.append("</description>\n")
                            .append("\t<Point>\n")
                            .append("\t\t<coordinates>")
                            .append(Objects.requireNonNull(localPoint.get(keyAddress)))
                            .append("</coordinates>\n")
                            .append("\t</Point>\n")
                            .append("</Placemark>\n");
                }
            }
            kmlFile.append("</Document>\n</kml>");
        } catch (IOException e) {
            Log.i("ST_getWebWorker", "result: " + Result.failure());
            throw new RuntimeException(e);
        } finally {
            assert mRows != null;
            // todo in @string uebersetzen
            SettingViewModel.mSearch.postValue(mRows.size() - 1 + " Stolpersteine \nin " + numberAddress + " Addressen gefunden." );
            SettingViewModel.progBarSet.postValue(0);
            SettingViewModel.mButton.postValue("GONE");

            //Log.i("ST_getWebWorker", "kml: " + kmlFile);

            // safe in cache
            FileManager.saveCacheFile(getApplicationContext(), CacheKMLFileName, kmlFile.toString());
        }
        //Log.i("ST_getWebWorker", "result: " + Result.success());
        return Result.success();
    }
}