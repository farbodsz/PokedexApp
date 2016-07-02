package com.satsumasoftware.pokedex.entities.encounter;

import android.content.Context;

import com.satsumasoftware.pokedex.entities.location.LocationArea;

public class DisplayedEncounter {

    private Encounter mEncounter;
    private EncounterSlot mEncounterSlot;
    private LocationArea mLocationArea;

    public DisplayedEncounter(Context context, Encounter encounter, LocationArea locationArea) {
        this(encounter, new EncounterSlot(context, encounter.getEncounterSlotId()), locationArea);
    }

    public DisplayedEncounter(Encounter encounter, EncounterSlot encounterSlot, LocationArea locationArea) {
        mEncounter = encounter;
        mEncounterSlot = encounterSlot;
        mLocationArea = locationArea;
    }

    public Encounter getEncounter() {
        return mEncounter;
    }

    public EncounterSlot getEncounterSlot() {
        return mEncounterSlot;
    }

    public LocationArea getLocationArea() {
        return mLocationArea;
    }

}
