package com.satsumasoftware.pokedex.entities.encounter;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

public class EncounterGroup {

    private static final String LOG_TAG = "EncounterGroup";

    private int mVersionId, mLocationAreaId, mPokemonId;
    private int mVersionGroupId, mEncounterMethodId;

    private ArrayMap<Integer, Integer> mLevelRates;

    private ArrayList<DisplayedEncounter> mDisplayedEncounters;

    public EncounterGroup() {
    }

    public boolean addDisplayedEncounter(DisplayedEncounter displayedEncounter) {
        if (mDisplayedEncounters == null) {
            mDisplayedEncounters = new ArrayList<>();
            mDisplayedEncounters.add(displayedEncounter);

            Encounter encounter = displayedEncounter.getEncounter();
            mVersionId = encounter.getVersionId();
            mLocationAreaId = encounter.getLocationAreaId();
            mPokemonId = encounter.getPokemonId();
            EncounterSlot slot = displayedEncounter.getEncounterSlot();
            mVersionGroupId = slot.getVersionGroupId();
            mEncounterMethodId = slot.getEncounterMethodId();

            updateMinAndMaxLevels(displayedEncounter);
            return true;
        }

        if (isValid(displayedEncounter)) {
            mDisplayedEncounters.add(displayedEncounter);
            updateMinAndMaxLevels(displayedEncounter);
            return true;
        }

        return false;
    }

    private void updateMinAndMaxLevels(DisplayedEncounter displayedEncounter) {
        int minLvl = displayedEncounter.getEncounter().getMinLevel();
        int maxLvl = displayedEncounter.getEncounter().getMaxLevel();

        if (minLvl != maxLvl) {
            throw new IllegalArgumentException("these are meant to be the same (I think)");
        }

        mLevelRates.put(minLvl, displayedEncounter.getEncounterSlot().getRarity());
    }

    public boolean isValid(DisplayedEncounter displayedEncounter) {
        Encounter encounter = displayedEncounter.getEncounter();
        EncounterSlot slot = displayedEncounter.getEncounterSlot();
        return (encounter.getVersionId() == mVersionId &&
                slot.getVersionGroupId() == mVersionGroupId &&
                encounter.getLocationAreaId() == mLocationAreaId &&
                encounter.getPokemonId() == mPokemonId &&
                slot.getEncounterMethodId() == mEncounterMethodId);
    }

    public ArrayList<DisplayedEncounter> getDisplayedEncounters() {
        return mDisplayedEncounters;
    }
}
