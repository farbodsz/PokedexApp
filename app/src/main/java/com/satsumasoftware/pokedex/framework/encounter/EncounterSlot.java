package com.satsumasoftware.pokedex.framework.encounter;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

public class EncounterSlot {

    private int mId, mVersionGroupId, mEncounterMethodId, mSlot, mRarity;

    public EncounterSlot(Context context, int id) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterSlots.TABLE_NAME,
                null,
                PokeDB.EncounterSlots.COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        mId = id;
        mVersionGroupId = cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_VERSION_GROUP_ID));
        mEncounterMethodId = cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_ENCOUNTER_METHOD_ID));
        mSlot = cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_SLOT));
        mRarity = cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_RARITY));
        cursor.close();
    }

    public EncounterSlot(int id, int versionGroupId, int encounterMethodId, int slot, int rarity) {
        mId = id;
        mVersionGroupId = versionGroupId;
        mEncounterMethodId = encounterMethodId;
        mSlot = slot;
        mRarity = rarity;
    }

    public int getId() {
        return mId;
    }

    public int getVersionGroupId() {
        return mVersionGroupId;
    }

    public int getEncounterMethodId() {
        return mEncounterMethodId;
    }

    public int getSlot() {
        return mSlot;
    }

    public int getRarity() {
        return mRarity;
    }

}
