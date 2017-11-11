/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class LocationAreasDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "location_areas.db";
    public static final String TABLE_NAME = "location_areas";
    public static final int DATABASE_VERSION = 10;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_LOCATION_ID = "location_id";
    public static final String COL_NAME = "name_en";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_LOCATION_ID + " INTEGER, " +
            COL_NAME + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;

    private static LocationAreasDBHelper sInstance;

    public static synchronized LocationAreasDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocationAreasDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private LocationAreasDBHelper(Context context) {
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
                PokeDB.LocationAreas.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int locationAreaId = cursor.getInt(cursor.getColumnIndex(PokeDB.LocationAreas.COL_ID));
            values.put(COL_ID, locationAreaId);

            values.put(COL_LOCATION_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.LocationAreas.COL_LOCATION_ID)));

            // "game_index" and "identifier" are not needed so they're not added
            putNameValues(values, locationAreaId, pokeDB);

            db.insert(TABLE_NAME, null, values);
            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
    }

    private void putNameValues(ContentValues values, int locationAreaId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.LocationAreaProse.TABLE_NAME,
                null,
                PokeDB.LocationAreaProse.COL_LOCATION_AREA_ID + "=?",
                new String[] {String.valueOf(locationAreaId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.LocationAreaProse.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.LocationAreaProse.COL_NAME));

            if (languageId == 9) {
                values.put(COL_NAME, name);  // only English as these are only ones at the moment
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
}
