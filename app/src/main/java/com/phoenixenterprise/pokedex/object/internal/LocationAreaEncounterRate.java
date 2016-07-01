package com.phoenixenterprise.pokedex.object.internal;

public class LocationAreaEncounterRate {

    private int mLocationAreaId, mEncounterMethodId, mVersionId, mRate;

    public LocationAreaEncounterRate(int locationAreaId, int encounterMethodId, int versionId, int rate) {
        mLocationAreaId = locationAreaId;
        mEncounterMethodId = encounterMethodId;
        mVersionId = versionId;
        mRate = rate;
    }

    public int getLocationAreaId() {
        return mLocationAreaId;
    }

    public int getEncounterMethodId() {
        return mEncounterMethodId;
    }

    public int getVersionId() {
        return mVersionId;
    }

    public int getRate() {
        return mRate;
    }

}
