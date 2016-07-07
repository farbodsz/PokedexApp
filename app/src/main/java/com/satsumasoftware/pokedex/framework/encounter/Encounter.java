package com.satsumasoftware.pokedex.framework.encounter;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.framework.location.LocationArea;
import com.satsumasoftware.pokedex.util.DataUtils;

public class Encounter {

    private int mId, mVersionId, mLocationAreaId, mEncounterSlotId, mPokemonId, mMinLvl, mMaxLvl;

    public Encounter(int id, int versionId, int locationAreaId, int encounterSlotId, int pokemonId,
                     int minLevel, int maxLevel) {
        mId = id;
        mVersionId = versionId;
        mLocationAreaId = locationAreaId;
        mEncounterSlotId = encounterSlotId;
        mPokemonId = pokemonId;
        mMinLvl = minLevel;
        mMaxLvl = maxLevel;
    }

    public int getId() {
        return mId;
    }

    public int getVersionId() {
        return mVersionId;
    }

    public int getLocationAreaId() {
        return mLocationAreaId;
    }

    public int getEncounterSlotId() {
        return mEncounterSlotId;
    }

    public int getPokemonId() {
        return mPokemonId;
    }

    public int getMinLevel() {
        return mMinLvl;
    }

    public int getMaxLevel() {
        return mMaxLvl;
    }

    public int getEncounterConditionId(Context context) {
        // TODO STOPSHIP is this correct?
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.EncounterConditionValueMap.TABLE_NAME,
                null,
                PokeDB.EncounterConditionValueMap.COL_ENCOUNTER_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        if (cursor.getCount() == 0) {
            return DataUtils.NULL_INT;
        }
        cursor.moveToFirst();
        int encounterConditionValueId = cursor.getInt(cursor.getColumnIndex(
                PokeDB.EncounterConditionValueMap.COL_ENCOUNTER_CONDITION_VALUE_ID));
        cursor.close();
        return encounterConditionValueId;
    }

    public boolean hasEncounterCondition(Context context) {
        return getEncounterConditionId(context) != DataUtils.NULL_INT;
    }

    public DisplayedEncounter toDisplayedEncounter(Context context, LocationArea locationArea) {
        return new DisplayedEncounter(context, this, locationArea);
    }

}
