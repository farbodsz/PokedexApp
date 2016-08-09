package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.framework.nature.BaseNature;
import com.satsumasoftware.pokedex.framework.nature.MiniNature;

import java.util.ArrayList;

public final class NaturesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "natures.db";
    public static final String TABLE_NAME = "natures";
    public static final int DATABASE_VERSION = 10;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_DECREASED_STAT_ID = "decreased_stat_id";
    public static final String COL_INCREASED_STAT_ID = "increased_stat_id";
    public static final String COL_HATES_FLAVOR_ID = "hates_flavor_id";
    public static final String COL_LIKES_FLAVOR_ID = "likes_flavor_id";
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
            COL_DECREASED_STAT_ID + " INTEGER, " +
            COL_INCREASED_STAT_ID + " INTEGER, " +
            COL_HATES_FLAVOR_ID + " INTEGER, " +
            COL_LIKES_FLAVOR_ID + " INTEGER, " +

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

    private static NaturesDBHelper sInstance;

    public static synchronized NaturesDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NaturesDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private NaturesDBHelper(Context context) {
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
        PokeDB pokeDB = PokeDB.getInstance(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Natures.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int id = cursor.getInt(cursor.getColumnIndex(PokeDB.Natures.COL_ID));
            values.put(COL_ID, id);


            values.put(COL_DECREASED_STAT_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Natures.COL_DECREASED_STAT_ID)));
            values.put(COL_INCREASED_STAT_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Natures.COL_INCREASED_STAT_ID)));

            values.put(COL_HATES_FLAVOR_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Natures.COL_HATES_FLAVOR_ID)));
            values.put(COL_LIKES_FLAVOR_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Natures.COL_LIKES_FLAVOR_ID)));

            putNameValues(values, id, pokeDB);

            db.insert(TABLE_NAME, null, values);
            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }

    private void putNameValues(ContentValues values, int natureId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.NatureNames.TABLE_NAME,
                null,
                PokeDB.NatureNames.COL_NATURE_ID + "=?",
                new String[] {String.valueOf(natureId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.NatureNames.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.NatureNames.COL_NAME));

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
                    break;
                default:
                    throw new IllegalArgumentException("language id '" + languageId +
                            "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }


    public ArrayList<MiniNature> getAllMiniNatures() {
        ArrayList<MiniNature> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                BaseNature.DB_COLUMNS,
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
