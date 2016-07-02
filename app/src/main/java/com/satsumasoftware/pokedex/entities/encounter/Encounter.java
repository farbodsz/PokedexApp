package com.satsumasoftware.pokedex.entities.encounter;

import android.content.Context;

import com.satsumasoftware.pokedex.entities.location.LocationArea;

public class Encounter {

    private int mId, mVersionId, mLocationAreaId, mEncounterSlotId, mPokemonId, mMinLvl, mMaxLvl, mEncounterConditionId;

    public Encounter(int id, int versionId, int locationAreaId, int encounterSlotId, int pokemonId,
                     int minLevel, int maxLevel, int encounterConditionId) {
        mId = id;
        mVersionId = versionId;
        mLocationAreaId = locationAreaId;
        mEncounterSlotId = encounterSlotId;
        mPokemonId = pokemonId;
        mMinLvl = minLevel;
        mMaxLvl = maxLevel;
        mEncounterConditionId = encounterConditionId;
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

    public int getEncounterConditionId() {
        return mEncounterConditionId;
    }

    public boolean hasEncounterCondition() {
        return mEncounterConditionId != 0;
    }

    public DisplayedEncounter toDisplayedEncounter(Context context, LocationArea locationArea) {
        return new DisplayedEncounter(context, this, locationArea);
    }

}
