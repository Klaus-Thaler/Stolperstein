package com.example.stolperstein.ui.settings;


import static com.example.stolperstein.MainActivity.CacheXMLData;
import static com.example.stolperstein.MainActivity.preAddress;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.stolperstein.MainActivity;
import com.example.stolperstein.classes.FileManager;

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
import java.util.TreeMap;

import kotlin.text.Regex;

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
        // create a xml Data File
        StringBuilder xmlFile = new StringBuilder();

        // geocoder um revers nach geopoints zu suchen
        int maxResults = 1;
        GeocoderNominatim mGeoCoder = new GeocoderNominatim(mLocale, mUserAgent);
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
            TreeMap<String, ArrayList<String>> resultWeb = new TreeMap<>(); // alphabetisch
            HashMap<String, String> resultAddress = new HashMap<>();
            Document doc = Jsoup.connect(website).get();
            Elements mTable = doc.select("table tbody");
            mRows = mTable.select("tr");
            // tabelle reihenweise auslesen
            //Log.i("ST_getWebWorker", "mRows: " + mRows.size());
            for (int i = 1; mRows.size() > i; i++) {
                //for (int i = 1; 10 > i; i++) {
                //first row is the col names so skip it.
                Elements mTD = mRows.get(i).select("td");
                for (int z = 0; mTD.size() > z; z++) {
                    //Log.i("ST_getWebWorker", "i " + i + " z " + z + " - " + mTD.get(z).toString());
                    ArrayList<String> entryTD = new ArrayList<>();
                    entryTD.add(mTD.get(0).select("span[style=\"display:none;\"]").text()); // name
                    entryTD.add(mTD.get(1).text());                             // adresse
                    entryTD.add(mTD.get(2).text());                             // geboren
                    entryTD.add(mTD.get(3).text());                             // deportiert
                    entryTD.add(mTD.get(4).select("a").attr("href"));    // Link Biografie
                    entryTD.add(mTD.get(5).select("a").attr("href"));    // Link Foto
                    entryTD.add(mTD.get(6).text());                             // verlegt am

                    // daten in hashes
                    resultWeb.put(entryTD.get(0), entryTD); // name, treemap, alphabetisch

                    // info: default ist string null, wird mcoder kein geo ermitteln , bleibt es bei null!
                    // info: @null erzeugt bei marker in map einen abbruch, darum string null
                    resultAddress.put(entryTD.get(1), "null"); // hashmap, addressen fuer mcoder
                }

            }

            // geopoints suchen und der hashmap hinzufugen
            Object[] keysAddress = resultAddress.keySet().toArray();
            for (Object keyAddress : keysAddress) {
                // alles in klammern raus
                Regex reg = new Regex("\\(.*\\)");
                String newKeyAddress = reg.replace((CharSequence) keyAddress,"");
                // hier mcoder
                // address zu Geo Daten. Laengen- und Breitengrad
                List<Address> mCoder = mGeoCoder.getFromLocationName(preAddress
                        + Objects.requireNonNull(newKeyAddress), maxResults);
                if (!mCoder.isEmpty()) {
                    resultAddress.replace((String) keyAddress, mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                    //Log.i("ST_getWebWorker",  "points " + mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                    // progress
                    SettingViewModel.progBarSet.postValue((numberAddress * 100) / resultAddress.size());  // setting in Progressbar
                    SettingViewModel.mSearch.postValue(newKeyAddress + "\n"
                            + mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                } else {
                    SettingViewModel.mSearch.postValue(keyAddress + "\n"
                            + "error: address no found");
                    //Log.i("ST_getWebWorker", "mcoder error: " + keyAddress);
                }
                numberAddress++;
            }
            //Log.i("ST_getWebWorker", "address in geopoints: " + index);
            //Log.i("ST_getWebWorker", "resWeb size: " + resultWeb.keySet().size());
            //Log.i("ST_getWebWorker", "resAddress size: " + resultAddress.keySet().size());

            // die geopoints den addressen hinzufuegen
            // schleife aus webdaten ueber schleife aus geodaten
            for (String keyWeb : resultWeb.keySet()) {
                //Log.i("ST_getWebWorker","name " + keyWeb);
                ArrayList<String> values = resultWeb.get(keyWeb); // .get values
                if (values != null) {
                    for (String keyAddress : resultAddress.keySet()) {
                        String geopoint = resultAddress.get(keyAddress);
                        if (keyAddress != null) {
                            //Log.i("ST_getWebWorker", "true/false " + values.get(1).equals(keyAddress));
                            if (values.get(1).equals(keyAddress)) {
                                values.add(geopoint);
                            }
                        }
                    } //Log.i("ST_getWebWorker", "values : " + values);
                } //Log.i("ST_getWebWorker", "resWeb: " + resultWeb);

            }

            /*
            kmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">")
                    .append("<Document>\n");
            for (String keyWeb : resultWeb.keySet()) {
                //Log.i("ST_getWebWorker","name " + keyWeb);
                ArrayList<String> values = resultWeb.get(keyWeb); // .get values
                assert values != null;
                if (values.get(7) != null) { // kein geopoint - keine anzeige
                    kmlFile.append("<Placemark>\n") // kml file
                            //.append("\t<id>").append(i).append("</id>\n") // id
                            .append("\t\t<name>")
                            .append(Objects.requireNonNull(values.get(1))) // strasse
                            .append("</name>\n")
                            .append("\t\t<description>")
                            .append(Objects.requireNonNull(values.get(0))) //name
                            .append("</description>\n")
                            .append("\t<Point>\n")
                            .append("\t\t<coordinates>")
                            .append(Objects.requireNonNull(values.get(7)))
                            .append("</coordinates>\n")
                            .append("\t</Point>\n")
                            .append("</Placemark>\n");
                }
            }
            kmlFile.append("</Document>\n</kml>");
             */

            xmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            for (String keyWeb : resultWeb.keySet()) {
                ArrayList<String> values = resultWeb.get(keyWeb); // .get values
                assert values != null;
                xmlFile.append("<person>\n")
                        .append("\t<name>").append(Objects.requireNonNull(values.get(0))).append("</name>\n")
                        .append("\t<address>").append(Objects.requireNonNull(values.get(1))).append("</address>\n")
                        .append("\t<coordinates>").append(values.get(7)).append("</coordinates>\n")
                        .append("\t<born>").append(Objects.requireNonNull(values.get(2))).append("</born>\n")
                        .append("\t<death>").append(Objects.requireNonNull(values.get(3))).append("</death>\n")
                        .append("\t<installed>").append(Objects.requireNonNull(values.get(6))).append("</installed>\n")
                        .append("\t<bio>").append(Objects.requireNonNull(values.get(4))).append("</bio>\n")
                        .append("\t<photo>").append(Objects.requireNonNull(values.get(5))).append("</photo>\n")
                        .append("</person>\n");
            }
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
            //Log.i("ST_getWebWorker", "xml: " + xmlFile);

            // safe in cache
            //FileManager.saveCacheFile(getApplicationContext(), CacheKMLFileName, kmlFile.toString());
            FileManager.saveCacheFile(getApplicationContext(), CacheXMLData, xmlFile.toString());
        }
        Log.i("ST_getWebWorker", "result: " + Result.success());
        return Result.success();
    }
}