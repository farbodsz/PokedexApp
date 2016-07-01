package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LearnsetDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "learnset.db";
    public static final String TABLE_NAME = "pokemon_learnsets";
    public static final int DATABASE_VERSION = 3;

    /* All Column Names */
    public static final String COL_PKMN_ID = "pokemon_id";
    public static final String COL_PKMN_FORM = "pokemon_form";
    public static final String COL_VERSION_ID = "version_id";
    public static final String COL_LEARN_METHOD = "learn_method_id";
    public static final String COL_LEARN_LEVEL = "learn_level";
    public static final String COL_MOVE_ID = "move_id";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_PKMN_ID + " INTEGER, " +
            COL_PKMN_FORM + " TEXT, " +
            COL_VERSION_ID + " INTEGER, " +
            COL_LEARN_METHOD + " INTEGER, " +
            COL_LEARN_LEVEL + " INTEGER, " +
            COL_MOVE_ID + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public LearnsetDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.LEARNSET_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.LEARNSET_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();
                    values.put(COL_PKMN_ID, Integer.parseInt(line[0]));
                    values.put(COL_PKMN_FORM, line[1]);
                    values.put(COL_VERSION_ID, Integer.parseInt(line[2]));
                    values.put(COL_LEARN_METHOD, Integer.parseInt(line[3]));
                    values.put(COL_LEARN_LEVEL, Integer.parseInt(line[4]));
                    values.put(COL_MOVE_ID, Integer.parseInt(line[5]));
                    db.insert(TABLE_NAME, null, values);

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
