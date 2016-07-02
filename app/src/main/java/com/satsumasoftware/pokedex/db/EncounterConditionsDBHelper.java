package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.List;

public class EncounterConditionsDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "EncountersDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounter_conditions.db";
    public static final String TABLE_NAME = "encounter_conditions";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_VALUE_ID = "id";
    public static final String COL_CONDITION_ID = "encounter_condition_id";
    public static final String COL_IS_DEFAULT_VALUE = "is_default";
    public static final String COL_VALUE_NAME = "condition_value_name";
    public static final String COL_VALUE_NAME_DE = "condition_value_name_de";
    public static final String COL_CONDITION_NAME = "condition_name";
    public static final String COL_CONDITION_NAME_FR = "condition_name_fr";
    public static final String COL_CONDITION_NAME_DE = "condition_name_de";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_VALUE_ID + " INTEGER, " +
            COL_CONDITION_ID + " INTEGER, " +
            COL_IS_DEFAULT_VALUE + " INTEGER, " +
            COL_VALUE_NAME + " TEXT, " +
            COL_VALUE_NAME_DE + " TEXT, " +
            COL_CONDITION_NAME + " TEXT, " +
            COL_CONDITION_NAME_FR + " TEXT, " +
            COL_CONDITION_NAME_DE + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public EncounterConditionsDBHelper(Context context) {
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
        CsvParser parser = CSVUtils.getMyParser();
        try {
            db.beginTransaction();
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ENCOUNTER_CONDITION_VALUES));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();

                int id = Integer.parseInt(line[0]);
                values.put(COL_VALUE_ID, id);
                int conditionId = Integer.parseInt(line[1]);
                values.put(COL_CONDITION_ID, conditionId);
                // line[2] is the identifier
                values.put(COL_IS_DEFAULT_VALUE, Integer.parseInt(line[3]));

                putValueNames(values, id);
                putConditionNames(values, conditionId);

                db.insert(TABLE_NAME, null, values);

                Log.d(LOG_TAG, "Added encounter condition of id " + String.valueOf(id));
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void putValueNames(ContentValues values, int encounterConditionValueId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ENCOUNTER_CONDITION_VALUE_PROSE));
            for (String[] line : allRows) {
                if (Integer.parseInt(line[0]) == encounterConditionValueId) {

                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    switch (languageId) {
                        case 6:
                            values.put(COL_VALUE_NAME_DE, name);
                            break;
                        case 9:
                            values.put(COL_VALUE_NAME, name);
                            return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putConditionNames(ContentValues values, int encounterConditionId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ENCOUNTER_CONDITION_PROSE));
            for (String[] line : allRows) {
                if (Integer.parseInt(line[0]) == encounterConditionId) {

                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    switch (languageId) {
                        case 5:
                            values.put(COL_CONDITION_NAME_FR, name);
                            break;
                        case 6:
                            values.put(COL_CONDITION_NAME_DE, name);
                            break;
                        case 9:
                            values.put(COL_CONDITION_NAME, name);
                            return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
