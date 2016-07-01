package com.phoenixenterprise.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenixenterprise.pokedex.object.MiniMove;
import com.phoenixenterprise.pokedex.util.CSVUtils;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MoveDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "move.db";
    public static final String TABLE_NAME = "moves";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_TYPE = "type";
    public static final String COL_CATEGORY = "category";
    public static final String COL_CONTEST = "contest";
    public static final String COL_PP = "stat_pp";
    public static final String COL_POWER = "stat_power";
    public static final String COL_ACCURACY = "stat_accuracy";
    public static final String COL_GENERATION = "generation";
    public static final String COL_DESCRIPTION = "description";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_TYPE + " TEXT, " +
            COL_CATEGORY + " TEXT, " +
            COL_CONTEST + " TEXT, " +
            COL_PP + " INTEGER, " +
            COL_POWER + " TEXT, " +
            COL_ACCURACY + " TEXT, " +
            COL_GENERATION + " INTEGER, " +
            COL_DESCRIPTION + " TEXT" +
            ")"; // Note that power and accuracy are strings because their values could be "n/a"

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public MoveDBHelper(Context context) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.MOVEDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.MOVEDEX_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();
                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_NAME, line[1]);
                    values.put(COL_TYPE, line[2]);
                    values.put(COL_CATEGORY, line[3]);
                    values.put(COL_CONTEST, line[4]);
                    values.put(COL_PP, Integer.parseInt(line[5]));
                    values.put(COL_POWER, line[6]);
                    values.put(COL_ACCURACY, line[7]);
                    values.put(COL_GENERATION, Integer.parseInt(line[8]));
                    values.put(COL_DESCRIPTION, InfoUtils.formatDBDescription(line[9]));
                    db.insert(TABLE_NAME, null, values);

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MiniMove> getAllMoves() {
        ArrayList<MiniMove> list = new ArrayList<>();
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
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            MiniMove move = new MiniMove(id, name);
            list.add(move);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
