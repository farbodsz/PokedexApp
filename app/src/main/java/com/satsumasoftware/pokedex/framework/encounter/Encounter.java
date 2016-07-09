package com.satsumasoftware.pokedex.framework.encounter;

import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

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

    public Encounter(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ID));
        mVersionId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID));
        mLocationAreaId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_LOCATION_AREA_ID));
        mEncounterSlotId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ENCOUNTER_SLOT_ID));
        mPokemonId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_POKEMON_ID));
        mMinLvl = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MIN_LEVEL));
        mMaxLvl = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MAX_LEVEL));
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
}
