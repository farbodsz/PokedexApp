package com.satsumasoftware.pokedex.framework.location

import android.os.Parcel
import android.os.Parcelable

import android.content.Context
import com.satsumasoftware.pokedex.db.LocationAreasDBHelper
import com.satsumasoftware.pokedex.db.LocationsDBHelper
import java.util.*

class Location(val id: Int, val regionId: Int, val name: String) : Parcelable {

    fun getLocationAreas(context: Context): ArrayList<LocationArea> {
        val helper = LocationAreasDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                LocationAreasDBHelper.TABLE_NAME,
                null,
                LocationAreasDBHelper.COL_LOCATION_ID + "=?",
                arrayOf(id.toString()),
                null, null, null)
        cursor.moveToFirst()

        val locationAreas = ArrayList<LocationArea>(cursor.count)
        while (!cursor.isAfterLast) {
            val areaId = cursor.getInt(cursor.getColumnIndex(LocationAreasDBHelper.COL_ID))
            val areaName = cursor.getString(cursor.getColumnIndex(LocationAreasDBHelper.COL_NAME))
            locationAreas.add(LocationArea(areaId, id, areaName))
            cursor.moveToNext()
        }
        cursor.close()

        return locationAreas
    }

    constructor(source: Parcel): this(source.readInt(), source.readInt(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeInt(regionId)
        dest?.writeString(name)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Location> = object : Parcelable.Creator<Location> {
            override fun createFromParcel(source: Parcel): Location = Location(source)
            override fun newArray(size: Int): Array<Location?> = arrayOfNulls(size)
        }
    }
}
