package com.example.stolperstein.classes;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class sqlHandler extends SQLiteOpenHelper {
    // creating a constant variables for this database.
    // below variable is for database name.
    private static final String DB_NAME = "stolpersteine.db";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "person";
    private static final String TABLE_ADDRESS = "address";

    private static final String NAME_ID = "id";

    private static final String NAME_NAME = "name";
    private static final String NAME_ADDRESS = "address";
    private static final String NAME_BORN = "born";
    private static final String NAME_DEATH = "death";
    private static final String NAME_BIO = "bio";
    private static final String NAME_PHOTO = "photo";
    private static final String NAME_INSTALL = "install";
    private static final String ADDRESS_ID = "id";
    private static final String ADDRESS_ADDRESS = "address";
    private static final String ADDRESS_GEOPOINT = "geopoint";

    /**
     * SQLite
     */

    // creating a constructor for our database handler.
    private static sqlHandler sInstance;
    public static synchronized sqlHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new sqlHandler(context.getApplicationContext());
        }
        return sInstance;
    }
    private sqlHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating 2 tables 'person' und 'address'
        String queryt1 = "CREATE TABLE " + TABLE_NAME + " ("
                + NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_NAME + " TEXT, "
                + NAME_ADDRESS + " TEXT,"
                + NAME_BORN + " TEXT, "
                + NAME_DEATH + " TEXT,"
                + NAME_BIO + " TEXT, "
                + NAME_INSTALL + " TEXT,"
                + NAME_PHOTO + " TEXT )";
        db.execSQL(queryt1);
        String queryt2 = "CREATE TABLE " + TABLE_ADDRESS
                + " (" + ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_ADDRESS + " TEXT, "
                + ADDRESS_GEOPOINT + " TEXT )";
        db.execSQL(queryt2);
    }
    public void clearDB(String table) {
        try {
            // inhalte loeschen
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor1 = db.rawQuery("DELETE FROM " + table, null);
            cursor1.moveToFirst();
            cursor1.close();
            // sql SET -> null u.a. PRIMARY KEY = 0
            Cursor cursor2 = db.rawQuery("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + table + "'", null);
            cursor2.moveToFirst();
            cursor2.close();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to clear database");
        }


    }
    public Integer getCount(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
    public void addNewName(String table, List<String> args) {
        // writing data in database.
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_NAME, args.get(0));
        values.put(NAME_ADDRESS, args.get(1));
        values.put(NAME_BORN, args.get(2));
        values.put(NAME_DEATH, args.get(3));
        values.put(NAME_BIO, args.get(4));
        values.put(NAME_PHOTO, args.get(5));
        values.put(NAME_INSTALL, args.get(6));
        db.insert(table, null, values);
        db.close();
    }
    public String getGeoPoint(String address) {
        // address -> geopoint
        String geoPoint = "";
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT address.geopoint FROM address WHERE address.address = '" + address + "'";
        Cursor cursor = db.rawQuery(query,null);
        try  {
            if (cursor.moveToFirst()) {
                geoPoint = cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS_GEOPOINT));
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return geoPoint;
    }
    public List<String> getNamesInAddress(String search) {
        // suche alle namen einer addresse
        // mehrere stolpersteine an einem ort
        String query = "SELECT person.name FROM person WHERE person.address = '" + search + "'";
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        try  {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_NAME)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }
    public List<String> getListFromQuery(String query, String index) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        try  {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(index)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }
    public List<String> getAllNames() {
        // suche alle namen einer addresse
        // mehrere stolpersteine an einem ort
        String query = "SELECT person.name FROM person";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        List<String> list = new ArrayList<>();
        try  {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_NAME)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }
    public String[] getNames() {
        // suche alle namen einer addresse
        // mehrere stolpersteine an einem ort
        String query = "SELECT person.name FROM person";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String[] list = new String[cursor.getCount()];
        try  {
            if (cursor.moveToFirst()) {
                do {
                    list[cursor.getPosition()] = cursor.getString(cursor.getColumnIndexOrThrow(NAME_NAME));
                } while (cursor.moveToNext());
            }
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    public void addNewGeoPoint(String table, List<String> args) {
        // writing data in database.
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ADDRESS_ADDRESS, args.get(0));
        values.put(ADDRESS_GEOPOINT, args.get(1));
        db.insert(table, null, values);
        db.close();
    }

    public HashMap<Integer,List<String>> getHashMapFromData(String query) {
        HashMap<Integer,List<String>> data = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Integer z = 0;
        try  {
            if (cursor.moveToFirst()) {
                do {
                    List<String> list = new ArrayList<>();
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_NAME)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_ADDRESS)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_BORN)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_DEATH)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_BIO)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_PHOTO)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_INSTALL)));
                    data.put(z, list);
                    z++;
                }
                while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }
    public HashMap<Integer,List<String>> getAllPersonData() {
        HashMap<Integer,List<String>> data = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        Integer z = 0;
        try  {
            if (cursor.moveToFirst()) {
                do {
                    List<String> list = new ArrayList<>();
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_NAME)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_ADDRESS)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_BORN)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_DEATH)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_BIO)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_PHOTO)));
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_INSTALL)));
                    data.put(z, list);
                    z++;
                }
                while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}