package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class EncounterConditionsDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "EncountersDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "encounter_conditions.db";
    public static final String TABLE_NAME = "encounter_conditions";
    public static final int DATABASE_VERSION = 10;

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

    private static EncounterConditionsDBHelper sInstance;

    public static synchronized EncounterConditionsDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EncounterConditionsDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private EncounterConditionsDBHelper(Context context) {
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
        PokeDB pokeDB = PokeDB.getInstance(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterConditionValues.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int id = cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterConditionValues.COL_ID));
            values.put(COL_VALUE_ID, id);

            int conditionId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterConditionValues.COL_ENCOUNTER_CONDITION_ID));
            values.put(COL_CONDITION_ID, conditionId);

            // the identifier is not used so not added

            values.put(COL_IS_DEFAULT_VALUE,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterConditionValues.COL_IS_DEFAULT)));

            putValueNames(values, id, pokeDB);
            putConditionNames(values, conditionId, pokeDB);

            db.insert(TABLE_NAME, null, values);

            Log.d(LOG_TAG, "Added encounter condition of id " + id);

            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }

    private void putValueNames(ContentValues values, int encounterConditionValueId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterConditionValueProse.TABLE_NAME,
                null,
                PokeDB.EncounterConditionValueProse.COL_ENCOUNTER_CONDITION_VALUE_ID + "=?",
                new String[] {String.valueOf(encounterConditionValueId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterConditionValueProse.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.EncounterConditionValueProse.COL_NAME));

            switch (languageId) {
                case 6:
                    values.put(COL_VALUE_NAME_DE, name);
                    break;
                case 9:
                    values.put(COL_VALUE_NAME, name);
                    break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putConditionNames(ContentValues values, int encounterConditionId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterConditionProse.TABLE_NAME,
                null,
                PokeDB.EncounterConditionProse.COL_ENCOUNTER_CONDITION_ID + "=?",
                new String[] {String.valueOf(encounterConditionId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterConditionProse.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.EncounterConditionProse.COL_NAME));

            switch (languageId) {
                case 5:
                    values.put(COL_CONDITION_NAME_FR, name);
                    break;
                case 6:
                    values.put(COL_CONDITION_NAME_DE, name);
                    break;
                case 9:
                    values.put(COL_CONDITION_NAME, name);
                    break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
}
