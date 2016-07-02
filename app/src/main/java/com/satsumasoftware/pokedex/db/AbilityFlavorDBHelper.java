package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.List;

public class AbilityFlavorDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "ability_flavor_text.db";
    public static final String TABLE_NAME = "ability_flavor_text";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ABILITY_ID = "ability_id";
    public static final String COL_VERSION_GROUP_ID = "version_group_id";
    public static final String COL_LANGUAGE_ID = "language_id";
    public static final String COL_FLAVOR_TEXT = "flavor_text";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ABILITY_ID + " INTEGER, " +
            COL_VERSION_GROUP_ID + " INTEGER, " +
            COL_LANGUAGE_ID + " INTEGER, " +
            COL_FLAVOR_TEXT + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public AbilityFlavorDBHelper(Context context) {
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
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ABILITY_FLAVOR_TEXT));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();

                int id = Integer.parseInt(line[0]);
                values.put(COL_ABILITY_ID, id);
                values.put(COL_VERSION_GROUP_ID, Integer.parseInt(line[1]));
                values.put(COL_LANGUAGE_ID, Integer.parseInt(line[2]));
                values.put(COL_FLAVOR_TEXT, line[3]);

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
