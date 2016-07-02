package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.List;

public class EggGroupProseDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "egg_group_prose.db";
    public static final String TABLE_NAME = "egg_group_prose";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_EGG_GROUP_ID = "egg_group_id";
    public static final String COL_NAME = "name_en";
    public static final String COL_NAME_JAPANESE = "name_ja";
    public static final String COL_NAME_FRENCH = "name_fr";
    public static final String COL_NAME_GERMAN = "name_de";
    public static final String COL_NAME_SPANISH = "name_es";
    public static final String COL_NAME_ITALIAN = "name_it";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_EGG_GROUP_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_NAME_JAPANESE + " TEXT, " +
            COL_NAME_FRENCH + " TEXT, " +
            COL_NAME_GERMAN + " TEXT, " +
            COL_NAME_SPANISH + " TEXT, " +
            COL_NAME_ITALIAN + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public EggGroupProseDBHelper(Context context) {
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
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.EGG_GROUP_PROSE));
            for (String[] line : allRows) {

                ContentValues values = new ContentValues();
                int id = Integer.parseInt(line[0]);
                values.put(COL_EGG_GROUP_ID, id);
                putNameValues(values, Integer.parseInt(line[0])); // FIXME does this work?
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void putNameValues(ContentValues values, int eggGroupId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.EGG_GROUP_PROSE));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[0]) == eggGroupId) {
                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    switch (languageId) {
                        case 1:
                            values.put(COL_NAME_JAPANESE, name);
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

}
