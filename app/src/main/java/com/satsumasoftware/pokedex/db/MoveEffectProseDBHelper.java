package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.util.List;

public class MoveEffectProseDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "MoveEffectProseDBHelper";

    /* General Database and Table information */
    private static final String DATABASE_NAME = "move_effect_prose.db";
    public static final String TABLE_NAME = "move_effect_prose";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "move_effect_id";
    public static final String COL_LANGUAGE_ID = "local_language_id";
    public static final String COL_SHORT_EFFECT = "short_effect";
    public static final String COL_EFFECT = "effect";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_LANGUAGE_ID + " INTEGER, " +
            COL_SHORT_EFFECT + " TEXT, " +
            COL_EFFECT + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public MoveEffectProseDBHelper(Context context) {
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
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);
        settings.setMaxCharsPerColumn(8192);  // the longest column value here has more chars than the default, 4096
        CsvParser parser = new CsvParser(settings);
        try {
            db.beginTransaction();
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.MOVE_EFFECT_PROSE));
            for (String[] line : allRows) {
                ContentValues values = new ContentValues();

                int moveEffectId = Integer.parseInt(line[0]);
                values.put(COL_ID, moveEffectId);
                values.put(COL_LANGUAGE_ID, Integer.parseInt(line[1]));
                values.put(COL_SHORT_EFFECT, line[2]);
                values.put(COL_EFFECT, line[3]);

                db.insert(TABLE_NAME, null, values);

                Log.d(LOG_TAG, "Added move effect prose");
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
