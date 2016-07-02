package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.List;

public class EncounterSlotsDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounter_slots.db";
    public static final String TABLE_NAME = "encounter_slots";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_VERSION_GROUP_ID = "version_group_id";
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
        CsvParser parser = CSVUtils.getMyParser();
        try {
            db.beginTransaction();
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ENCOUNTER_SLOTS));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();
                int id = Integer.parseInt(line[0]);
                values.put(COL_ID, id);
                values.put(COL_VERSION_GROUP_ID, Integer.parseInt(line[1]));
                values.put(COL_ENCOUNTER_METHOD_ID, Integer.parseInt(line[2]));
                values.put(COL_SLOT, ((line[3] == null) ? -1 : Integer.parseInt(line[3])) );
                //values.put(COL_SLOT, Integer.parseInt(line[3]));  // FIXME: see my opened issue on veekun GitHub - change later if there is a solution
                values.put(COL_RARITY, Integer.parseInt(line[4]));
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
