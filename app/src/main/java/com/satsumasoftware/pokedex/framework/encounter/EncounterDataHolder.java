package com.satsumasoftware.pokedex.framework.encounter;

public class EncounterDataHolder {

    private Encounter mEncounter;
    private EncounterSlot mEncounterSlot;

    public EncounterDataHolder(Encounter encounter, EncounterSlot encounterSlot) {
        mEncounter = encounter;
        mEncounterSlot = encounterSlot;
    }

    public Encounter getEncounter() {
        return mEncounter;
    }

    public EncounterSlot getEncounterSlot() {
        return mEncounterSlot;
    }

}
