package com.satsumasoftware.pokedex.object.internal;

public class Encounter {

    private int mId, mVersionId, mLocationAreaId, mEncounterSlotId, mPokemonId, mMinLvl, mMaxLvl;

    public Encounter(int id, int versionId, int locationAreaId, int encounterSlotId,
                     int pokemonId, int minLvl, int maxLvl) {
        mId = id;
        mVersionId = versionId;
        mLocationAreaId = locationAreaId;
        mEncounterSlotId = encounterSlotId;
        mPokemonId = pokemonId;
        mMinLvl = minLvl;
        mMaxLvl = maxLvl;
    }

    public int getId() {
        return mId;
    }

    public int getGameVersionId() {
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

    public int getPokemonMinLvl() {
        return mMinLvl;
    }

    public int getPokemonMaxLvl() {
        return mMaxLvl;
    }

}
