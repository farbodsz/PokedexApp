package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.framework.ability.BaseAbility;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.framework.item.BaseItem;
import com.satsumasoftware.pokedex.framework.item.MiniItem;

import java.util.ArrayList;

public class ItemsDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "ItemsDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "items.db";
    public static final String TABLE_NAME = "items";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_COST = "cost";
    public static final String COL_FLING_POWER = "fling_power";
    public static final String COL_FLING_EFFECT_ID = "fling_effect_id";

    public static final String COL_ITEM_POCKET_ID = "item_pocket_id";

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
            COL_CATEGORY_ID + " INTEGER, " +
            COL_COST + " INTEGER, " +
            COL_FLING_POWER + " INTEGER, " +
            COL_FLING_EFFECT_ID + " INTEGER, " +

            COL_ITEM_POCKET_ID + " INTEGER, " +

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


    public ItemsDBHelper(Context context) {
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
        PokeDB pokeDB = new PokeDB(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Items.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int id = cursor.getInt(cursor.getColumnIndex(PokeDB.Items.COL_ID));
            values.put(COL_ID, id);

            // the identifier will not be used so it's not put in db

            int categoryId = cursor.getInt(cursor.getColumnIndex(PokeDB.Items.COL_CATEGORY_ID));
            values.put(COL_CATEGORY_ID, categoryId);

            values.put(COL_COST,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Items.COL_COST)));
            values.put(COL_FLING_POWER,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Items.COL_FLING_POWER)));
            values.put(COL_FLING_EFFECT_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Items.COL_FLING_EFFECT_ID)));

            putPocketIdValues(values, categoryId, pokeDB);
            putNameValues(values, id, pokeDB);

            db.insert(TABLE_NAME, null, values);

            Log.d(LOG_TAG, "Added item");

            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }

    private void putPocketIdValues(ContentValues values, int itemCategoryId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.ItemCategories.TABLE_NAME,
                null,
                PokeDB.ItemCategories.COL_ID + "=?",
                new String[] {String.valueOf(itemCategoryId)},
                null, null, null);
        cursor.moveToFirst();
        values.put(COL_ITEM_POCKET_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.ItemCategories.COL_POCKET_ID)));
        cursor.close();
    }

    private void putNameValues(ContentValues values, int itemId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.ItemNames.TABLE_NAME,
                null,
                PokeDB.ItemNames.COL_ITEM_ID + "=?",
                new String[] {String.valueOf(itemId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.ItemNames.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.ItemNames.COL_NAME));

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


    public ArrayList<MiniItem> getAllItems() {
        ArrayList<MiniItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                BaseItem.DB_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            MiniItem item = new MiniItem(id, name);
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
