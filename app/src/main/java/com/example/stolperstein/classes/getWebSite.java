package com.example.stolperstein.classes;

import static com.example.stolperstein.MainActivity.CacheFileName;
import static com.example.stolperstein.MainActivity.CacheFileName;
import static com.example.stolperstein.MainActivity.preAddress;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.example.stolperstein.ui.settings.SettingViewModel;
import com.example.stolperstein.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class getWebSite extends Activity {
       public void getWebsite(Context context, String website) {
           // hier noch 404 abfangen

           //ActivityCompat.requestPermissions(getWebSite.this, MainActivity.Perms, 2);

           // die daten als kml speichern// https://github.com/MKergall/osmbonuspack/wiki/
           // https://www.jentsch.io/geocodierung-mit-der-nominatim-api/
           final String NOMINATIM_SERVICE_URL = "https://nominatim.openstreetmap.org/";
           final String MAPQUEST_SERVICE_URL = "https://open.mapquestapi.com/nominatim/v1/";
           Locale mLocale = Locale.GERMANY;
           String mServiceUrl;
           String mKey;
           String mUserAgent = null;
           // create a kml file to cache
           StringBuilder kmlFile = new StringBuilder();
           kmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                   "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" +
                   "<Document>\n");
           // geocoder um revers nach geopoints zu suchen
           int maxResults = 1;
           GeocoderNominatim mGeoCoder = new GeocoderNominatim(mLocale, mUserAgent);

            // Objekt fuer Daten erstellem
            new Thread(() -> {
                Elements mRows = null;
                SettingViewModel.mButton.postValue("PLEASE WAIT");
                try {
                    Document doc = Jsoup.connect(website).get();
                    Elements mTable = doc.select("table tbody");
                    mRows = mTable.select("tr");
                    for (int i = 1; mRows.size() > i; i++) { //first row is the col names so skip it.
                        ArrayList<String> entryTD = new ArrayList<>();
                        Elements mTD = mRows.get(i).select("td");
                        for (int z = 0; mTD.size() > z; z++) {
                            Log.i("ST_FS_td_", "td " +  z);
                            Log.i("ST_FS_entry", "i " + i + "z " + z + " - " + mTD.get(z).toString());

                            entryTD.add(mTD.get(0).select("span[style=\"display:none;\"]").text()); // name
                            entryTD.add(mTD.get(1).text());         // adresse
                            entryTD.add(mTD.get(2).text());         // geboren
                            entryTD.add(mTD.get(3).text());         // deportiert
                            entryTD.add(mTD.get(4).html());         // Link Biografie
                            entryTD.add(mTD.get(5).html());         // Link Foto
                            entryTD.add(mTD.get(6).text());         // verlegt am
                            entryTD.add(mTD.get(5).select("a").toString()); // weblink foto
                        }
                        // address zu Geo Daten. Laengen- und Breitengrad
                        List<Address> mCoder = mGeoCoder.getFromLocationName(preAddress
                                + Objects.requireNonNull(entryTD.get(1)), maxResults);
                        if (!mCoder.isEmpty()) {
                            Log.i("ST_getWS_mcoder", "mcoder: " + i
                                    + " " + mCoder.isEmpty()
                                    + " " + preAddress + Objects.requireNonNull(entryTD.get(1))
                                    + " " + mCoder.get(0).getLongitude() + "," + mCoder.get(0).getLatitude());
                            Log.i("ST_FS_td", "index " + i + "-> " + entryTD);
                            SettingViewModel.progBarSet.postValue((i * 100) / mRows.size());
                            SettingViewModel.mSearch.postValue(Objects.requireNonNull(entryTD.get(0)) + "\n"
                                                        + Objects.requireNonNull(entryTD.get(1)));
                            kmlFile.append("<Placemark>\n") // kml file
                                    .append("\t<id>").append(i).append("</id>\n") // id
                                    .append("\t<title>").append("Stolperstein").append("</title>\n")
                                    .append("\t<description>\n")
                                    //.append("\t\t").append(Objects.requireNonNull(entryTD.get(0)))      // name
                                    //.append(" - ")
                                    .append(Objects.requireNonNull(entryTD.get(1)))   // adresse
                                    .append(" - ").append(Objects.requireNonNull(entryTD.get(2)))   // geboren
                                    .append("-").append(Objects.requireNonNull(entryTD.get(3)))     // deportiert
                                    .append("\n\t</description>\n")
                                    .append("\t<Point>\n\t\t<coordinates>")
                                    .append(mCoder.get(0).getLongitude()).append(",")
                                    .append(mCoder.get(0).getLatitude())
                                    .append("</coordinates>\n\t</Point>\n")
                                    .append("\t<data>\n")
                                    .append("\t\t<name>").append(Objects.requireNonNull(entryTD.get(0)))
                                    .append("</name>\n")
                                    .append("\t\t<address>").append(Objects.requireNonNull(entryTD.get(1)))
                                    .append("</address>\n")
                                    .append("\t\t<born>").append(Objects.requireNonNull(entryTD.get(2)))
                                    .append("</born>\n")
                                    .append("\t\t<death>").append(Objects.requireNonNull(entryTD.get(3)))
                                    .append("</death>\n")
                                    .append("\t\t<biographie>")
                                    .append(Objects.requireNonNull(entryTD.get(4))).append("</biographie>\n")
                                    .append("\t\t<photo>")
                                    .append(Objects.requireNonNull(entryTD.get(5))).append("</photo>\n")
                                    .append("\t</data>\n")
                                    .append("</Placemark>\n");
                        }
                    }
                    kmlFile.append("</Document>\n</kml>");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    SettingViewModel.mSearch.postValue(mRows.size() - 1 + " Stolpersteine gefunden.");
                    SettingViewModel.progBarSet.postValue(0);
                    SettingViewModel.mButton.postValue("GONE");

                    Log.i("ST_getWS", "kmlfile: " + kmlFile);
                    // safe in cache
                    FileManager.saveCacheFile(context, MainActivity.CacheFileName, kmlFile.toString());
                    Log.i("ST_cachefile", "mod: " + FileManager.CacheFileLastModified(
                            context,FileManager.loadCacheFile
                            (context, CacheFileName)));
                }
            }).start();
       }
    }
