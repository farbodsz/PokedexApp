package com.satsumasoftware.pokedex.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private int mId;
    private String mLocation, mRegion;

    public Location(int id, String location, String region) {
        mId = id;
        mLocation = location;
        mRegion = region;
    }

    public int getLocationId() {
        return mId;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getRegion() {
        return mRegion;
    }


    protected Location(Parcel in) {
        mId = in.readInt();
        mLocation = in.readString();
        mRegion = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mLocation);
        dest.writeString(mRegion);
    }

}
