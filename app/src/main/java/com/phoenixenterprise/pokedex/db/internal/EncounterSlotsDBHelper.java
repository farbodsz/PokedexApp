package com.phoenixenterprise.pokedex.db.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenixenterprise.pokedex.object.internal.EncounterSlot;
import com.phoenixenterprise.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EncounterSlotsDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounter_slots.db";
    public static final String TABLE_NAME = "encounter_slots";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_VERSION_GROUP_ID = "game_version_group_id";
    public static final String COL_ENCOUNTER_METHOD_ID = "encounter_method_id";
    public static final String COL_SLOT = "slot";
    public static final String COL_RARITY = "rarity";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_VERSION_GROUP_ID + " INTEGER, " +
            COL_ENCOUNTER_METHOD_ID + " INTEGER, " +
            COL_SLOT + " INTEGER, " +
            COL_RARITY + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public EncounterSlotsDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.ENCOUNTER_SLOTS_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.ENCOUNTER_SLOTS_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_VERSION_GROUP_ID, Integer.parseInt(line[1]));
                    values.put(COL_ENCOUNTER_METHOD_ID, Integer.parseInt(line[2]));
                    values.put(COL_SLOT, Integer.parseInt(line[3]));
                    values.put(COL_RARITY, Integer.parseInt(line[4]));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EncounterSlot> getEncounterSlotsList() {
        ArrayList<EncounterSlot> list = new ArrayList<>();
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
            int versionGroupId = cursor.getInt(cursor.getColumnIndex(COL_VERSION_GROUP_ID));
            int encounterMethodId = cursor.getInt(cursor.getColumnIndex(COL_ENCOUNTER_METHOD_ID));
            int slot = cursor.getInt(cursor.getColumnIndex(COL_SLOT));
            int rarity = cursor.getInt(cursor.getColumnIndex(COL_RARITY));
            EncounterSlot encounterSlot = new EncounterSlot(id, versionGroupId, encounterMethodId, slot, rarity);
            list.add(encounterSlot);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
