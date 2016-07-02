package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PokemonMovesDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "PokemonMovesDBHelper";

    /*
     * General Database and Table information
     */

    private static final String DATABASE_NAME = "pokemon_moves.db";
    public static final String TABLE_NAME = "pokemon_moves";
    public static final int DATABASE_VERSION = 1;

    /*
     * All Column Names
     */

    public static final String COL_POKEMON_ID = "pokemon_id";
    public static final String COL_VERSION_GROUP_ID = "version_group_id";
    public static final String COL_MOVE_ID = "move_id";
    public static final String COL_POKEMON_MOVE_METHOD_ID = "pokemon_move_method";
    public static final String COL_LEVEL = "level";
    public static final String COL_ORDER = "move_order";


    /*
     * SQL CREATE command creates all columns as defined above
     */

    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_POKEMON_ID + " INTEGER, " +
            COL_VERSION_GROUP_ID + " INTEGER, " +
            COL_MOVE_ID + " INTEGER, " +
            COL_POKEMON_MOVE_METHOD_ID + " INTEGER, " +
            COL_LEVEL + " INTEGER, " +
            COL_ORDER + " INTEGER" +
            ")";

    /*
     * SQL DROP command deletes the SQL table
     */

    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    /*
     * Main part of the class
     */

    private Context mContext;


    public PokemonMovesDBHelper(Context context) {
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
        try {
            db.beginTransaction();
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.POKEMON_MOVES)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(",");
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    int id = Integer.parseInt(line[0]);
                    values.put(COL_POKEMON_ID, id);
                    values.put(COL_VERSION_GROUP_ID, Integer.parseInt(line[1]));
                    values.put(COL_MOVE_ID, Integer.parseInt(line[2]));
                    values.put(COL_POKEMON_MOVE_METHOD_ID, Integer.parseInt(line[3]));
                    values.put(COL_LEVEL, Integer.parseInt(line[4]));
                    values.put(COL_ORDER, ((line.length > 4) ? -1 : Integer.parseInt(line[5])) );

                    db.insert(TABLE_NAME, null, values);

                    Log.d(LOG_TAG, "Added Pokemon move entry");
                }
            }
            reader.close();
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        /*
        CsvParser parser = CSVUtils.getMyParser(); // TODO don't CSV parse this way -
        // it loads everything into the memory and then uses it - use another Univocity CSV parsing
        // method


        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.POKEMON_MOVES));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();
                values.put(COL_POKEMON_ID, Integer.parseInt(line[0]));
                values.put(COL_VERSION_GROUP_ID, Integer.parseInt(line[1]));
                values.put(COL_MOVE_ID, Integer.parseInt(line[2]));
                values.put(COL_POKEMON_MOVE_METHOD_ID, Integer.parseInt(line[3]));
                values.put(COL_LEVEL, Integer.parseInt(line[4]));
                values.put(COL_ORDER, Integer.parseInt(line[5]));
                db.insert(TABLE_NAME, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}
