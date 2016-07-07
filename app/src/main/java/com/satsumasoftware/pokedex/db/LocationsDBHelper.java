package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.framework.location.Location;

import java.util.ArrayList;

public class LocationsDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "locations.db";
    public static final String TABLE_NAME = "locations";
    public static final int DATABASE_VERSION = 2;

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
        PokeDB pokeDB = new PokeDB(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Locations.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int locationId = cursor.getInt(cursor.getColumnIndex(PokeDB.Locations.COL_ID));
            values.put(COL_ID, locationId);

            values.put(COL_REGION_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Locations.COL_REGION_ID)));

            // the identifier will not be used so it's not put in db

            putNameValues(values, locationId, pokeDB);

            db.insert(TABLE_NAME, null, values);
            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }


    private void putNameValues(ContentValues values, int locationId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.LocationNames.TABLE_NAME,
                null,
                PokeDB.LocationNames.COL_LOCATION_ID + "=?",
                new String[] {String.valueOf(locationId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.LocationNames.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.LocationNames.COL_NAME));

            if (languageId == 9) {
                values.put(COL_NAME, name);
                // only puts english names as these are guaranteed to be here, but other
                // languages only have some names
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
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
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            Location location = new Location(id, regionId, name);
            list.add(location);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
