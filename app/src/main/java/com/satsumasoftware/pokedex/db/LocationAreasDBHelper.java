package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.entities.location.LocationArea;
import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationAreasDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location_areas.db";
    public static final String TABLE_NAME = "location_areas";
    public static final int DATABASE_VERSION = 2;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_LOCATION_ID = "location_id";
    public static final String COL_NAME = "name_en";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_LOCATION_ID + " INTEGER, " +
            COL_NAME + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationAreasDBHelper(Context context) {
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
        CsvParser parser = CSVUtils.getMyParser();
        try {
            db.beginTransaction();
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.LOCATION_AREAS));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();
                int locationAreaId = Integer.parseInt(line[0]);
                values.put(COL_ID, locationAreaId);
                values.put(COL_LOCATION_ID, Integer.parseInt(line[1]));
                // line[2] (game index) and line[3] (identifier) are not needed
                putNameValues(values, Integer.parseInt(line[0]));
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void putNameValues(ContentValues values, int locationAreaId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.LOCATION_AREA_PROSE));
            for (String[] line : allRows) {
                if (Integer.parseInt(line[0]) == locationAreaId) {

                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    if (languageId == 9) {
                        values.put(COL_NAME, name);  // only English as these are only ones
                        return;
                    }
                }
            }
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
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            LocationArea locationArea = new LocationArea(id, locationId, name);
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
                    cursor.getString(cursor.getColumnIndex(COL_NAME)));
            list.add(locationArea);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
