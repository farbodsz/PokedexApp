package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.entities.location.Location;
import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationsDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "locations.db";
    public static final String TABLE_NAME = "locations";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_REGION_ID = "region_id";
    public static final String COL_NAME = "name_en";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_REGION_ID + " INTEGER, " +
            COL_NAME + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationsDBHelper(Context context) {
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
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.LOCATIONS));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();
                int locationId = Integer.parseInt(line[0]);
                values.put(COL_ID, locationId);
                //values.put(COL_REGION_ID, Integer.parseInt(line[1]));
                values.put(COL_REGION_ID, ((line[1] == null) ? -1 : Integer.parseInt(line[1])) ); // FIXME see EncounterSlotsDBHelper
                // line[2] (identifier) will not be used; to put in db
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


    private void putNameValues(ContentValues values, int locationId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.LOCATION_NAMES));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[0]) == locationId) {
                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    if (languageId == 9) {
                        values.put(COL_NAME, name);  // only put English names
                        // (as these are guaranteed to be here, but other languages only have
                        // some names)
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> list = new ArrayList<>();
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
            int regionId = cursor.getInt(cursor.getColumnIndex(COL_REGION_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));  // TODO: more languages
            Location location = new Location(id, regionId, name);
            list.add(location);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
