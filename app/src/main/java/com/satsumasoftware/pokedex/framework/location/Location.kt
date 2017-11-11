/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.framework.location

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
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
            locationAreas.add(LocationArea(cursor))
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

        @JvmStatic
        fun create(context: Context, id: Int): Location {
            val dbHelper = LocationsDBHelper.getInstance(context)
            val cursor = dbHelper.readableDatabase.query(
                    LocationsDBHelper.TABLE_NAME,
                    null,
                    LocationsDBHelper.COL_ID + "=?",
                    arrayOf(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val regionId = cursor.getInt(cursor.getColumnIndex(LocationsDBHelper.COL_REGION_ID))
            val name = cursor.getString(cursor.getColumnIndex(LocationsDBHelper.COL_NAME))
            cursor.close()
            return Location(id, regionId, name)
        }
    }

}
