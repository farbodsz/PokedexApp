package com.phoenixenterprise.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenixenterprise.pokedex.object.Nature;
import com.phoenixenterprise.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NatureDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "nature.db";
    public static final String TABLE_NAME = "natures";
    public static final int DATABASE_VERSION = 4;

    /* All Column Names */
    public static final String COL_NAME = "name";
    public static final String COL_STAT_INCREASED = "increased_stat";
    public static final String COL_STAT_DESCREASED = "descreased_stat";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_NAME + " TEXT, " +
            COL_STAT_INCREASED + " TEXT, " +
            COL_STAT_DESCREASED + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public NatureDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.NATUREDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.NATUREDEX_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();
                    values.put(COL_NAME, line[0]);
                    values.put(COL_STAT_INCREASED, line[1]);
                    values.put(COL_STAT_DESCREASED, line[2]);
                    db.insert(TABLE_NAME, null, values);

                }
            }
            reader.close();
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
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String statIncreased = cursor.getString(cursor.getColumnIndex(COL_STAT_INCREASED));
            String statDecreased = cursor.getString(cursor.getColumnIndex(COL_STAT_DESCREASED));
            Nature nature = new Nature(name, statIncreased, statDecreased);
            list.add(nature);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
