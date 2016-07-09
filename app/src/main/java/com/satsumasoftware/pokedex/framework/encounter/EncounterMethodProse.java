package com.satsumasoftware.pokedex.framework.encounter;

public class EncounterMethodProse {

    private int mEncounterMethodId;
    private String mName;  // TODO add German names from the database

    public EncounterMethodProse(int encounterMethodId, String name) {
        mEncounterMethodId = encounterMethodId;
        mName = name;
    }

    public int getEncounterMethodId() {
        return mEncounterMethodId;
    }

    public String getName() {
        return mName;
    }

}
