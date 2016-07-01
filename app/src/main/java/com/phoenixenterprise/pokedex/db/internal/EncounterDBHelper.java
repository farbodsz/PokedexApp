package com.phoenixenterprise.pokedex.db.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.phoenixenterprise.pokedex.object.internal.Encounter;
import com.phoenixenterprise.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EncounterDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounters.db";
    public static final String TABLE_NAME = "encounters";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_VERSION_ID = "game_version_id";
    public static final String COL_LOCATION_AREA_ID = "location_area_id";
    public static final String COL_ENCOUNTER_SLOT_ID = "encounter_slot_id";
    public static final String COL_POKEMON_ID = "pokemon_national_id";
    public static final String COL_POKEMON_MIN_LVL = "encounter_min_level";
    public static final String COL_POKEMON_MAX_LVL = "encounter_max_level";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_VERSION_ID + " INTEGER, " +
            COL_LOCATION_AREA_ID + " INTEGER, " +
            COL_ENCOUNTER_SLOT_ID + " INTEGER, " +
            COL_POKEMON_ID + " INTEGER, " +
            COL_POKEMON_MIN_LVL + " INTEGER, " +
            COL_POKEMON_MAX_LVL + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public EncounterDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.ENCOUNTERS_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.ENCOUNTERS_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_VERSION_ID, Integer.parseInt(line[1]));
                    values.put(COL_LOCATION_AREA_ID, Integer.parseInt(line[2]));
                    values.put(COL_ENCOUNTER_SLOT_ID, Integer.parseInt(line[3]));
                    values.put(COL_POKEMON_ID, Integer.parseInt(line[4]));
                    values.put(COL_POKEMON_MIN_LVL, Integer.parseInt(line[5]));
                    values.put(COL_POKEMON_MAX_LVL, Integer.parseInt(line[6]));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            int minLvl = cursor.getInt(cursor.getColumnIndex(COL_POKEMON_MIN_LVL));
            int maxLvl = cursor.getInt(cursor.getColumnIndex(COL_POKEMON_MAX_LVL));
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
                    cursor.getInt(cursor.getColumnIndex(COL_POKEMON_MIN_LVL)),
                    cursor.getInt(cursor.getColumnIndex(COL_POKEMON_MAX_LVL)));
            Log.d("EncounterDBHelper", "Added an encounter object to the list");
            list.add(encounter);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
