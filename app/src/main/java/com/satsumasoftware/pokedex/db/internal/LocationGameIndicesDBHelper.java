package com.satsumasoftware.pokedex.db.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.object.internal.LocationGameIndex;
import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationGameIndicesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location_game_indices.db";
    public static final String TABLE_NAME = "location_game_indices";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_LOCATION_ID = "location_id";
    public static final String COL_GENERATION = "generation";
    public static final String COL_GAME_INDEX = "game_index";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_LOCATION_ID + " INTEGER, " +
            COL_GENERATION + " INTEGER, " +
            COL_GAME_INDEX + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LocationGameIndicesDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.LOCATION_GAME_INDICES_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.LOCATION_GAME_INDICES_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_LOCATION_ID, Integer.parseInt(line[0]));
                    values.put(COL_GENERATION, Integer.parseInt(line[1]));
                    values.put(COL_GAME_INDEX, Integer.parseInt(line[2]));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LocationGameIndex> getLocationGameIndexList() {
        ArrayList<LocationGameIndex> list = new ArrayList<>();
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
            int locationId = cursor.getInt(cursor.getColumnIndex(COL_LOCATION_ID));
            int generation = cursor.getInt(cursor.getColumnIndex(COL_GENERATION));
            int gameIndex = cursor.getInt(cursor.getColumnIndex(COL_GAME_INDEX));
            LocationGameIndex locationGameIndex = new LocationGameIndex(locationId, generation, gameIndex);
            list.add(locationGameIndex);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
