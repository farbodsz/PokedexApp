package com.phoenixenterprise.pokedex.db.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenixenterprise.pokedex.object.internal.LocationAreaEncounterRate;
import com.phoenixenterprise.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationAreaEncounterRatesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location_area_encounter_rates.db";
    public static final String TABLE_NAME = "location_area_encounter_rates";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_LOCATION_AREA_ID = "location_area_id";
    public static final String COL_ENCOUNTER_METHOD_ID = "encounter_method_id";
    public static final String COL_VERSION_ID = "version_id";
    public static final String COL_RATE = "rate";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_LOCATION_AREA_ID + " INTEGER, " +
            COL_ENCOUNTER_METHOD_ID + " INTEGER, " +
            COL_VERSION_ID + " INTEGER, " +
            COL_RATE + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationAreaEncounterRatesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.LOCATION_AREA_ENCOUNTER_RATES_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.LOCATION_AREA_ENCOUNTER_RATES_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_LOCATION_AREA_ID, Integer.parseInt(line[0]));
                    values.put(COL_ENCOUNTER_METHOD_ID, Integer.parseInt(line[1]));
                    values.put(COL_VERSION_ID, Integer.parseInt(line[2]));
                    values.put(COL_RATE, Integer.parseInt(line[3]));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LocationAreaEncounterRate> getLocationAreaEncounterRatesList() {
        ArrayList<LocationAreaEncounterRate> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int locationAreaId = cursor.getInt(cursor.getColumnIndex(COL_LOCATION_AREA_ID));
            int encounterMethodId = cursor.getInt(cursor.getColumnIndex(COL_ENCOUNTER_METHOD_ID));
            int versionId = cursor.getInt(cursor.getColumnIndex(COL_VERSION_ID));
            int rate = cursor.getInt(cursor.getColumnIndex(COL_RATE));
            LocationAreaEncounterRate locAreaEncounterRate =
                    new LocationAreaEncounterRate(locationAreaId, encounterMethodId, versionId, rate);
            list.add(locAreaEncounterRate);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<LocationAreaEncounterRate> getLocationAreaEncounterRatesList(int locationAreaId, int encounterMethodId) {
        ArrayList<LocationAreaEncounterRate> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COL_LOCATION_AREA_ID + "=? AND " + COL_ENCOUNTER_METHOD_ID + "=?",
                new String[] {String.valueOf(locationAreaId), String.valueOf(encounterMethodId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationAreaEncounterRate locAreaEncounterRate = new LocationAreaEncounterRate(
                    cursor.getInt(cursor.getColumnIndex(COL_LOCATION_AREA_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_ENCOUNTER_METHOD_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_VERSION_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_RATE)));
            list.add(locAreaEncounterRate);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
