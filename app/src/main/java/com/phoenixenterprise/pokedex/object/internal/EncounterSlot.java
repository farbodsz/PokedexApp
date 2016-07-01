package com.phoenixenterprise.pokedex.object.internal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.phoenixenterprise.pokedex.db.internal.EncounterSlotsDBHelper;

public class EncounterSlot {

    private int mId, mVersionGroupId, mEncounterMethodId, mSlot, mRarity;

    public EncounterSlot(int id, int versionGroupId, int encounterMethodId, int slot, int rarity) {
        mId = id;
        mVersionGroupId = versionGroupId;
        mEncounterMethodId = encounterMethodId;
        mSlot = slot;
        mRarity = rarity;
    }

    public EncounterSlot(Context context, int id) {
        mId = id;
        findMissingValues(context, id);
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


    private void findMissingValues(Context context, int id) {
        EncounterSlotsDBHelper helper = new EncounterSlotsDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                EncounterSlotsDBHelper.TABLE_NAME,
                null,
                EncounterSlotsDBHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mVersionGroupId = cursor.getInt(cursor.getColumnIndex(EncounterSlotsDBHelper.COL_VERSION_GROUP_ID));
            mEncounterMethodId = cursor.getInt(cursor.getColumnIndex(EncounterSlotsDBHelper.COL_ENCOUNTER_METHOD_ID));
            mSlot = cursor.getInt(cursor.getColumnIndex(EncounterSlotsDBHelper.COL_SLOT));
            mRarity = cursor.getInt(cursor.getColumnIndex(EncounterSlotsDBHelper.COL_RARITY));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

}
