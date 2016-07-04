package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.entities.encounter.Encounter;

import java.util.ArrayList;

public class EncountersDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "EncountersDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounters.db";
    public static final String TABLE_NAME = "encounters";
    public static final int DATABASE_VERSION = 3;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_VERSION_ID = "version_id";
    public static final String COL_LOCATION_AREA_ID = "location_area_id";
    public static final String COL_ENCOUNTER_SLOT_ID = "encounter_slot_id";
    public static final String COL_POKEMON_ID = "pokemon_id";
    public static final String COL_MIN_LVL = "min_level";
    public static final String COL_MAX_LVL = "max_level";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_VERSION_ID + " INTEGER, " +
            COL_LOCATION_AREA_ID + " INTEGER, " +
            COL_ENCOUNTER_SLOT_ID + " INTEGER, " +
            COL_POKEMON_ID + " INTEGER, " +
            COL_MIN_LVL + " INTEGER, " +
            COL_MAX_LVL + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public EncountersDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating database");
        db.execSQL(SQL_CREATE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Upgrading database");
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        PokeDB pokeDB = new PokeDB(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Encounters.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int id = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ID));
            values.put(COL_ID, id);

            values.put(COL_VERSION_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID)));
            values.put(COL_LOCATION_AREA_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_LOCATION_AREA_ID)));
            values.put(COL_ENCOUNTER_SLOT_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ENCOUNTER_SLOT_ID)));
            values.put(COL_POKEMON_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_POKEMON_ID)));
            values.put(COL_MIN_LVL,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MIN_LEVEL)));
            values.put(COL_MAX_LVL,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MAX_LEVEL)));

            db.insert(TABLE_NAME, null, values);

            Log.d(LOG_TAG, "Added encounter of id " + String.valueOf(id));

            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }

    public ArrayList<Encounter> getEncountersList() {
        ArrayList<Encounter> list = new ArrayList<>();
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
            int versionId = cursor.getInt(cursor.getColumnIndex(COL_VERSION_ID));
            int locationAreaId = cursor.getInt(cursor.getColumnIndex(COL_LOCATION_AREA_ID));
            int encounterSlotId = cursor.getInt(cursor.getColumnIndex(COL_ENCOUNTER_SLOT_ID));
            int pokemonId = cursor.getInt(cursor.getColumnIndex(COL_POKEMON_ID));
            int minLvl = cursor.getInt(cursor.getColumnIndex(COL_MIN_LVL));
            int maxLvl = cursor.getInt(cursor.getColumnIndex(COL_MAX_LVL));
            Encounter encounter = new Encounter(id, versionId, locationAreaId, encounterSlotId,
                    pokemonId, minLvl, maxLvl);
            list.add(encounter);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Encounter> getEncountersList(int locationAreaId) {
        ArrayList<Encounter> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COL_LOCATION_AREA_ID + "=?",
                new String[] {String.valueOf(locationAreaId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Encounter encounter = new Encounter(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_VERSION_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_LOCATION_AREA_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_ENCOUNTER_SLOT_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_POKEMON_ID)),
                    cursor.getInt(cursor.getColumnIndex(COL_MIN_LVL)),
                    cursor.getInt(cursor.getColumnIndex(COL_MAX_LVL)));
            list.add(encounter);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
