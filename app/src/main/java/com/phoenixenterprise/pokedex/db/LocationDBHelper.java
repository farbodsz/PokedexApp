package com.phoenixenterprise.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenixenterprise.pokedex.object.Location;
import com.phoenixenterprise.pokedex.util.CSVUtils;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location.db";
    public static final String TABLE_NAME = "locations_list";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name_identifier";
    public static final String COL_REGION = "region_id";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_REGION + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.LOCATIONS_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.LOCATIONS_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_ID, Integer.parseInt(line[0]));
                    // String identifier is line[1]
                    values.put(COL_NAME, InfoUtils.formatAccents(line[2]));
                    values.put(COL_REGION, InfoUtils.getRegionFromId(Integer.parseInt(line[3])));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] {COL_ID, COL_NAME, COL_REGION},
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String region = cursor.getString(cursor.getColumnIndex(COL_REGION));
            Location location = new Location(id, name, region);
            list.add(location);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
