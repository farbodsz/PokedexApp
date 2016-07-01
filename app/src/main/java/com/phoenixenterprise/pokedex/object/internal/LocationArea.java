package com.phoenixenterprise.pokedex.object.internal;

public class LocationArea {

    private int mId, mLocationId, mGameIndex;
    private String mIdentifier;

    public LocationArea(int id, int locationId, int gameIndex, String identifier) {
        mId = id;
        mLocationId = locationId;
        mGameIndex = gameIndex;
        mIdentifier = identifier;
    }

    public int getId() {
        return mId;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public int getGameIndex() {
        return mGameIndex;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

}
