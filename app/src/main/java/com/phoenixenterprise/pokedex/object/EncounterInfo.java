package com.phoenixenterprise.pokedex.object;

public class EncounterInfo {

    private int mId, mMinLvl, mMaxLvl, mVersionId, mEncounterSlotId, mEncounterMethod, mSlot, mRarity;
    private MiniPokemon mPokemon;

    public EncounterInfo(int id, MiniPokemon miniPokemon, int minLvl, int maxLvl, int versionId,
                         int encounterSlotId, int encounterMethod, int slot, int rarity) {
        mId = id;
        mPokemon = miniPokemon;
        mMinLvl = minLvl;
        mMaxLvl = maxLvl;
        mVersionId = versionId;
        mEncounterSlotId = encounterSlotId;
        mEncounterMethod = encounterMethod;
        mSlot = slot;
        mRarity = rarity;
    }

    public int getId() {
        return mId;
    }

    public MiniPokemon getPokemon() {
        return mPokemon;
    }

    public int getPokemonMinLvl() {
        return mMinLvl;
    }

    public int getPokemonMaxLvl() {
        return mMaxLvl;
    }

    public int getGameVersionId() {
        return mVersionId;
    }

    public int getEncounterSlotId() {
        return mEncounterSlotId;
    }

    public int getEncounterMethodId() {
        return mEncounterMethod;
    }

    public int getSlot() {
        return mSlot;
    }

    public int getRarity() {
        return mRarity;
    }

}
