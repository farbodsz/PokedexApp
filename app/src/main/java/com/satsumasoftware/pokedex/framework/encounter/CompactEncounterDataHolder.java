package com.satsumasoftware.pokedex.framework.encounter;

import java.util.ArrayList;

public class CompactEncounterDataHolder {

    private int mPokemonId;
    private int mMinLevel = -1, mMaxLevel = -1;
    private ArrayList<EncounterDataHolder> mEncounterDataHolders;

    public CompactEncounterDataHolder(int pokemonId) {
        mPokemonId = pokemonId;
    }

    public int getPokemonId() {
        return mPokemonId;
    }

    public void addEncounterDataHolder(EncounterDataHolder encounterDataHolder) {
        if (mEncounterDataHolders == null) {
            mEncounterDataHolders = new ArrayList<>();
        }

        if (encounterDataHolder.getEncounter().getPokemonId() != mPokemonId) {
            throw new IllegalArgumentException("the pokemon id must match the one previously specified");
        }

        Encounter encounter = encounterDataHolder.getEncounter();
        int encounterMinLevel = encounter.getMinLevel();
        int encounterMaxLevel = encounter.getMaxLevel();

        if (mMinLevel == -1) {
            mMinLevel = encounterMinLevel;
        } else if (encounterMinLevel < mMinLevel) {
            mMinLevel = encounterMinLevel;
        }

        if (mMaxLevel == -1) {
            mMaxLevel = encounterMaxLevel;
        } else if (encounterMaxLevel > mMaxLevel) {
            mMaxLevel = encounterMaxLevel;
        }

        mEncounterDataHolders.add(encounterDataHolder);
    }

    public int getMinLevel() {
        return mMinLevel;
    }

    public int getMaxLevel() {
        return mMaxLevel;
    }

}
