package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.entities.nature.MiniNature;
import com.satsumasoftware.pokedex.entities.nature.Nature;
import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NaturesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "natures.db";
    public static final String TABLE_NAME = "natures";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_IDENTIFIER = "identifier";  // TODO remove identifier?
    public static final String COL_DECREASED_STAT_ID = "decreased_stat_id";
    public static final String COL_INCREASED_STAT_ID = "increased_stat_id";
    public static final String COL_HATES_FLAVOR_ID = "hates_flavor_id";
    public static final String COL_LIKES_FLAVOR_ID = "likes_flavor_id";
    public static final String COL_GAME_INDEX = "game_index";
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
            COL_IDENTIFIER + " TEXT, " +
            COL_DECREASED_STAT_ID + " INTEGER, " +
            COL_INCREASED_STAT_ID + " INTEGER, " +
            COL_HATES_FLAVOR_ID + " INTEGER, " +
            COL_LIKES_FLAVOR_ID + " INTEGER, " +
            COL_GAME_INDEX + " INTEGER, " +
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


    public NaturesDBHelper(Context context) {
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
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.NATURES));
            for (String[] line : allRows) {

                ContentValues values = new ContentValues();

                int id = Integer.parseInt(line[0]);
                values.put(COL_ID, id);
                values.put(COL_IDENTIFIER, line[1]);

                values.put(COL_DECREASED_STAT_ID, Integer.parseInt(line[2]));
                values.put(COL_INCREASED_STAT_ID, Integer.parseInt(line[3]));

                values.put(COL_HATES_FLAVOR_ID, Integer.parseInt(line[4]));
                values.put(COL_LIKES_FLAVOR_ID, Integer.parseInt(line[5]));
                values.put(COL_GAME_INDEX, Integer.parseInt(line[6]));

                putNameValues(values, Integer.parseInt(line[0]));

                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void putNameValues(ContentValues values, int natureId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.NATURE_NAMES));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[0]) == natureId) {
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

    public ArrayList<Nature> getAllNatures() {
        ArrayList<Nature> list = new ArrayList<>();
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
            String identifier = cursor.getString(cursor.getColumnIndex(COL_IDENTIFIER));
            int decrStatId = cursor.getInt(cursor.getColumnIndex(COL_DECREASED_STAT_ID));
            int incrStatId = cursor.getInt(cursor.getColumnIndex(COL_INCREASED_STAT_ID));
            int hatesFlavorId = cursor.getInt(cursor.getColumnIndex(COL_HATES_FLAVOR_ID));
            int likesFlavorId = cursor.getInt(cursor.getColumnIndex(COL_LIKES_FLAVOR_ID));
            int gameIndex = cursor.getInt(cursor.getColumnIndex(COL_GAME_INDEX));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String nameJa = cursor.getString(cursor.getColumnIndex(COL_NAME_JAPANESE));
            String nameKo = cursor.getString(cursor.getColumnIndex(COL_NAME_KOREAN));
            String nameFr = cursor.getString(cursor.getColumnIndex(COL_NAME_FRENCH));
            String nameDe = cursor.getString(cursor.getColumnIndex(COL_NAME_GERMAN));
            String nameEs = cursor.getString(cursor.getColumnIndex(COL_NAME_SPANISH));
            String nameIt = cursor.getString(cursor.getColumnIndex(COL_NAME_ITALIAN));
            Nature nature = new Nature(id, identifier, decrStatId, incrStatId, hatesFlavorId,
                    likesFlavorId, gameIndex, name, nameJa, nameKo, nameFr, nameDe, nameEs, nameIt);
            list.add(nature);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<MiniNature> getAllMiniNatures() {
        ArrayList<MiniNature> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                MiniNature.DB_COLUMNS,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            MiniNature nature = new MiniNature(id, name);
            list.add(nature);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
