package com.satsumasoftware.pokedex.object;

import java.util.ArrayList;

public class EncounterItem {

    private MiniPokemon mPokemon;
    private ArrayList<EncounterItemSlot> mEncounterSlots;

    private int mGeneralMinLvl, mGeneralMaxLvl, mTotalRarity;

    public EncounterItem(MiniPokemon miniPokemon, ArrayList<EncounterItemSlot> encounterSlots) {
        mPokemon = miniPokemon;
        mEncounterSlots = encounterSlots;

        int minLvl = 100;
        int maxLvl = 0;
        int totalRarityCount = 0;
        for (EncounterItemSlot slot : mEncounterSlots) {
            if (slot.getMinLvl() < minLvl) minLvl = slot.getMinLvl();
            if (slot.getMaxLvl() > maxLvl) maxLvl = slot.getMaxLvl();
            totalRarityCount += slot.getRarity();
        }
        mGeneralMinLvl = minLvl;
        mGeneralMaxLvl = maxLvl;
        mTotalRarity = totalRarityCount;
    }

    public MiniPokemon getPokemon() {
        return mPokemon;
    }

    public ArrayList<EncounterItemSlot> getEncounterItemSlots() {
        return mEncounterSlots;
    }

    public int getGeneralMinLvl() {
        return mGeneralMinLvl;
    }

    public int getGeneralMaxLvl() {
        return mGeneralMaxLvl;
    }

    public int getTotalRarity() {
        return mTotalRarity;
    }

}
