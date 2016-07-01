package com.phoenixenterprise.pokedex.db.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.phoenixenterprise.pokedex.object.internal.LocationArea;
import com.phoenixenterprise.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationAreaDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location_areas.db";
    public static final String TABLE_NAME = "location_areas";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_LOCATION_ID = "location_id";
    public static final String COL_GAME_INDEX = "game_index";
    public static final String COL_IDENTIFIER = "identifier";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_LOCATION_ID + " INTEGER, " +
            COL_GAME_INDEX + " INTEGER, " +
            COL_IDENTIFIER + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationAreaDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.LOCATION_AREAS_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.LOCATION_AREAS_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    Log.d("LocationAreaDBHelper", "Line: " + data);
                    Log.d("LocationAreaDBHelper", "Line length: " + line.length);

                    ContentValues values = new ContentValues();

                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_LOCATION_ID, Integer.parseInt(line[1]));
                    values.put(COL_GAME_INDEX, Integer.parseInt(line[2]));
                    values.put(COL_IDENTIFIER, line[3]);

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LocationArea> getLocationAreaList() {
        ArrayList<LocationArea> list = new ArrayList<>();
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
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            int locationId = cursor.getInt(cursor.getColumnIndex(COL_LOCATION_ID));
            int gameIndex = cursor.getInt(cursor.getColumnIndex(COL_GAME_INDEX));
            String identifier = cursor.getString(cursor.getColumnIndex(COL_IDENTIFIER));
            LocationArea locationArea = new LocationArea(id, locationId, gameIndex, identifier);
            list.add(locationArea);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<LocationArea> getLocationAreaList(int locationId) {
        ArrayList<LocationArea> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COL_LOCATION_ID + "=?",
                new String[] {String.valueOf(locationId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationArea locationArea = new LocationArea(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_LOCATION_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_GAME_INDEX)),
                    cursor.getString(cursor.getColumnIndex(COL_IDENTIFIER)));
            Log.d("LocationAreaDBHelper", "Added LocationArea object");
            list.add(locationArea);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
