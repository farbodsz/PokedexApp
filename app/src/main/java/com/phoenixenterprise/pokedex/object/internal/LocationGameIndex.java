package com.phoenixenterprise.pokedex.object.internal;

import android.content.Context;
import android.database.Cursor;

import com.phoenixenterprise.pokedex.db.internal.LocationGameIndicesDBHelper;

public class LocationGameIndex {

    private int mLocationId, mGeneration, mGameIndex;

    public LocationGameIndex(int locationId, int generation, int gameIndex) {
        mLocationId = locationId;
        mGeneration = generation;
        mGameIndex = gameIndex;
    }

    public LocationGameIndex(Context context, int locationId) {
        mLocationId = locationId;

        int[] values = getGenAndIndex(context, locationId);
        mGeneration = values[0];
        mGameIndex = values[1];
    }

    public int getLocationId() {
        return mLocationId;
    }

    public int getGeneration() {
        return mGeneration;
    }

    public int getGameIndex() {
        return mGameIndex;
    }


    private int[] getGenAndIndex(Context context, int locationId) {
        int values[] = new int[2];
        LocationGameIndicesDBHelper helper = new LocationGameIndicesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                LocationGameIndicesDBHelper.TABLE_NAME,
                null,
                LocationGameIndicesDBHelper.COL_LOCATION_ID + "=?",
                new String[] {String.valueOf(locationId)},
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            values[0] = cursor.getInt(cursor.getColumnIndex(LocationGameIndicesDBHelper.COL_GENERATION));
            values[1] = cursor.getInt(cursor.getColumnIndex(LocationGameIndicesDBHelper.COL_GAME_INDEX));
            cursor.moveToNext();
        }
        cursor.close();

        return values;
    }

}