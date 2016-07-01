package com.satsumasoftware.pokedex.object;

public class EncounterItemSlot {

    private int mMinLvl, mMaxLvl, mSlot, mRarity;

    public EncounterItemSlot(int minLvl, int maxLvl, int slot, int rarity) {
        mMinLvl = minLvl;
        mMaxLvl = maxLvl;
        mSlot = slot;
        mRarity = rarity;
    }

    public int getMinLvl() {
        return mMinLvl;
    }

    public int getMaxLvl() {
        return mMaxLvl;
    }

    public int getSlot() {
        return mSlot;
    }

    public int getRarity() {
        return mRarity;
    }

}
