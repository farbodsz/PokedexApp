package com.satsumasoftware.pokedex.framework.location;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.framework.encounter.Encounter;

import java.util.ArrayList;

public class LocationArea {

    private int mId, mLocationId;
    private String mName;


    public LocationArea(int id, int locationId, String name) {
        mId = id;
        mLocationId = locationId;
        mName = name;
    }


    public int getId() {
        return mId;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Integer> findEncounterGameVersions(Context context) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Encounters.TABLE_NAME,
                null,
                PokeDB.Encounters.COL_LOCATION_AREA_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();

        ArrayList<Integer> versions = new ArrayList<>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            versions.add(cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID)));
            cursor.moveToNext();
        }
        cursor.close();

        return versions;
    }

    public ArrayList<Encounter> findAllEncounters(Context context, int versionId) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Encounters.TABLE_NAME,
                null,
                PokeDB.Encounters.COL_LOCATION_AREA_ID + "=? AND " + PokeDB.Encounters.COL_VERSION_ID + "=?",
                new String[] {String.valueOf(mId), String.valueOf(versionId)},
                null, null, null);
        cursor.moveToFirst();

        ArrayList<Encounter> encounters = new ArrayList<>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            encounters.add(new Encounter(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return encounters;
    }

    /*
    public int getEncounterRate(Context context, int encounterMethodId, int versionId) {
        LocationAreaEncounterRatesDBHelper helper = new LocationAreaEncounterRatesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                LocationAreaEncounterRatesDBHelper.TABLE_NAME,
                null,
                LocationAreaEncounterRatesDBHelper.COL_LOCATION_AREA_ID + "=? AND " +
                        LocationAreaEncounterRatesDBHelper.COL_ENCOUNTER_METHOD_ID + "=? AND " +
                        LocationAreaEncounterRatesDBHelper.COL_VERSION_ID + "=?",
                new String[] {String.valueOf(mId), String.valueOf(encounterMethodId),
                        String.valueOf(versionId)},
                null, null, null);
        cursor.moveToFirst();
        int rate = cursor.getInt(cursor.getColumnIndex(LocationAreaEncounterRatesDBHelper.COL_RATE));
        cursor.close();
        return rate;
    }
    */

}
