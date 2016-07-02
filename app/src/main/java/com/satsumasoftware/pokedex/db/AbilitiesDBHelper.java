package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.entities.ability.Ability;
import com.satsumasoftware.pokedex.entities.ability.MiniAbility;
import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbilitiesDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "AbilitiesDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "abilities.db";
    public static final String TABLE_NAME = "abilities";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_GENERATION_ID = "generation_id";

    public static final String COL_NAME = "name_en";
    public static final String COL_NAME_JAPANESE = "name_ja";
    public static final String COL_NAME_KOREAN = "name_ko";
    public static final String COL_NAME_FRENCH = "name_fr";
    public static final String COL_NAME_GERMAN = "name_de";
    public static final String COL_NAME_SPANISH = "name_es";
    public static final String COL_NAME_ITALIAN = "name_it";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_GENERATION_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_NAME_JAPANESE + " TEXT, " +
            COL_NAME_KOREAN + " TEXT, " +
            COL_NAME_FRENCH + " TEXT, " +
            COL_NAME_GERMAN + " TEXT, " +
            COL_NAME_SPANISH + " TEXT, " +
            COL_NAME_ITALIAN + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public AbilitiesDBHelper(Context context) {
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
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ABILITIES));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[3]) == 1) {
                    ContentValues values = new ContentValues();

                    // if "is_main_series" (i.e. not conquest)
                    int id = Integer.parseInt(line[0]);
                    values.put(COL_ID, id);
                    // line[1] identifier will not be used; to put in db
                    values.put(COL_GENERATION_ID, Integer.parseInt(line[2]));

                    putNameValues(values, Integer.parseInt(line[0]));

                    db.insert(TABLE_NAME, null, values);

                    Log.d(LOG_TAG, "Added ability");
                }
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    private void putNameValues(ContentValues values, int abilityId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.ABILITY_NAMES));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[0]) == abilityId) {
                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    switch (languageId) {
                        case 1:
                            values.put(COL_NAME_JAPANESE, name);
                            break;
                        case 3:
                            values.put(COL_NAME_KOREAN, name);
                            break;
                        case 5:
                            values.put(COL_NAME_FRENCH, name);
                            break;
                        case 6:
                            values.put(COL_NAME_GERMAN, name);
                            break;
                        case 7:
                            values.put(COL_NAME_SPANISH, name);
                            break;
                        case 8:
                            values.put(COL_NAME_ITALIAN, name);
                            break;
                        case 9:
                            values.put(COL_NAME, name);
                            return;
                        default:
                            throw new IllegalArgumentException("language id '" +
                                    String.valueOf(languageId) + "' is invalid");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Ability> getAllAbilities() {
        ArrayList<Ability> list = new ArrayList<>();
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
            int generationId = cursor.getInt(cursor.getColumnIndex(COL_GENERATION_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String nameJa = cursor.getString(cursor.getColumnIndex(COL_NAME_JAPANESE));
            String nameKo = cursor.getString(cursor.getColumnIndex(COL_NAME_KOREAN));
            String nameFr = cursor.getString(cursor.getColumnIndex(COL_NAME_FRENCH));
            String nameDe = cursor.getString(cursor.getColumnIndex(COL_NAME_GERMAN));
            String nameEs = cursor.getString(cursor.getColumnIndex(COL_NAME_SPANISH));
            String nameIt = cursor.getString(cursor.getColumnIndex(COL_NAME_ITALIAN));
            Ability ability = new Ability(id, generationId, name, nameJa, nameKo, nameFr, nameDe,
                    nameEs, nameIt);
            list.add(ability);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<MiniAbility> getAllMiniAbilities() {
        ArrayList<MiniAbility> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                MiniAbility.DB_COLUMNS,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            MiniAbility ability = new MiniAbility(id, name);
            list.add(ability);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
