package com.satsumasoftware.pokedex.framework.encounter;

import java.util.ArrayList;

public class CompactEncounterDataHolder {

    private int mPokemonId;
    private int mMinLevel = -1, mMaxLevel = -1, mRarity = 0;
    private ArrayList<EncounterDataHolder> mEncounterDataHolders;

    public CompactEncounterDataHolder(int pokemonId) {
        mPokemonId = pokemonId;
    }

    public int getPokemonId() {
        return mPokemonId;
    }

    public void addEncounterDataHolder(EncounterDataHolder encounterDataHolder) {
        if (encounterDataHolder.getEncounter().getPokemonId() != mPokemonId) {
            throw new IllegalArgumentException("the pokemon id must match the one previously specified");
        }

        if (mEncounterDataHolders == null) {
            mEncounterDataHolders = new ArrayList<>();
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

        mRarity += encounterDataHolder.getEncounterSlot().getRarity();

        mEncounterDataHolders.add(encounterDataHolder);
    }

    public int getMinLevel() {
        return mMinLevel;
    }

    public int getMaxLevel() {
        return mMaxLevel;
    }

    public int getRarity() {
        return mRarity;
    }

}
