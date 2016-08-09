package com.satsumasoftware.pokedex.framework.location;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.LocationAreasDBHelper;
import com.satsumasoftware.pokedex.db.LocationsDBHelper;

import java.util.ArrayList;

public class Location implements Parcelable {

    private int mId, mRegionId;
    private String mName;


    public Location(int id, int regionId, String name) {
        mId = id;
        mRegionId = regionId;
        mName = name;
    }

    public Location(Context context, int id) {
        LocationsDBHelper dbHelper = LocationsDBHelper.getInstance(context);
        Cursor cursor = dbHelper.getReadableDatabase().query(
                LocationsDBHelper.TABLE_NAME,
                null,
                LocationsDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        mId = id;
        mRegionId = cursor.getInt(cursor.getColumnIndex(LocationsDBHelper.COL_REGION_ID));
        mName = cursor.getString(cursor.getColumnIndex(LocationsDBHelper.COL_NAME));
        cursor.close();
    }


    public int getId() {
        return mId;
    }

    public int getRegionId() {
        return mRegionId;
    }

    public String getName() {
        return mName;
    }


    public ArrayList<LocationArea> getLocationAreas(Context context) {
        LocationAreasDBHelper helper = LocationAreasDBHelper.getInstance(context);
        Cursor cursor = helper.getReadableDatabase().query(
                LocationAreasDBHelper.TABLE_NAME,
                null,
                LocationAreasDBHelper.COL_LOCATION_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();

        ArrayList<LocationArea> locationAreas = new ArrayList<>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            int areaId = cursor.getInt(cursor.getColumnIndex(LocationAreasDBHelper.COL_ID));
            String areaName = cursor.getString(cursor.getColumnIndex(LocationAreasDBHelper.COL_NAME));
            locationAreas.add(new LocationArea(areaId, mId, areaName));
            cursor.moveToNext();
        }
        cursor.close();

        return locationAreas;
    }


    protected Location(Parcel in) {
        mId = in.readInt();
        mRegionId = in.readInt();
        mName = in.readString();
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
        dest.writeInt(mRegionId);
        dest.writeString(mName);
    }
}
