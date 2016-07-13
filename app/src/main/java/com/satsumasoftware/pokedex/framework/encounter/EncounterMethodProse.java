package com.satsumasoftware.pokedex.framework.encounter;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

public class EncounterMethodProse {

    private int mEncounterMethodId;
    private String mName;  // TODO add German names from the database

    public EncounterMethodProse(int encounterMethodId, String name) {
        mEncounterMethodId = encounterMethodId;
        mName = name;
    }

    public EncounterMethodProse(Context context, int encounterMethodId) {
        mEncounterMethodId = encounterMethodId;
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterMethodProse.TABLE_NAME,
                null,
                PokeDB.EncounterMethodProse.COL_ENCOUNTER_METHOD_ID + "=? AND " +
                        PokeDB.EncounterMethodProse.COL_LOCAL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(mEncounterMethodId), "9"},
                null, null, null);
        cursor.moveToFirst();
        mName = cursor.getString(cursor.getColumnIndex(PokeDB.EncounterMethodProse.COL_NAME));
        cursor.close();
    }

    public int getEncounterMethodId() {
        return mEncounterMethodId;
    }

    public String getName() {
        return mName;
    }

}
