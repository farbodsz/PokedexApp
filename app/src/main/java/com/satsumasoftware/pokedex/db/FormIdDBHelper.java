package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FormIdDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "form_identifiers.db";
    public static final String TABLE_NAME = "form_ids";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_STRING_ID = "string_id";
    public static final String COL_SPECIES = "species_id";
    public static final String COL_FORM = "form_name";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_STRING_ID + " TEXT, " +
            COL_SPECIES + " INTEGER, " +
            COL_FORM + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public FormIdDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.FORM_IDENTIFIERS_DB)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.FORM_IDENTIFIERS_SEP); // Splits the line up into a string array
                if (line.length > 1) {
                    ContentValues values = new ContentValues();
                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_STRING_ID, line[1]);
                    values.put(COL_SPECIES, Integer.parseInt(line[2]));
                    values.put(COL_FORM, line[3]);
                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MiniPokemon getPokemonFromLongId(int id) {
        MiniPokemon miniPokemon = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int speciesId = cursor.getInt(cursor.getColumnIndex(COL_SPECIES));
            String form = cursor.getString(cursor.getColumnIndex(COL_FORM));
            miniPokemon = new MiniPokemon(mContext, speciesId, form);
            cursor.moveToNext();
        }
        cursor.close();

        if (miniPokemon != null) {
            return miniPokemon;
        } else {
            throw new NullPointerException("pokemon with id: " + id + " not found in database");
        }
    }
}
