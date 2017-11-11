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
import android.database.Cursor
import android.util.Log
import com.satsumasoftware.pokedex.db.LocationAreasDBHelper
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.framework.encounter.Encounter
import java.util.*

class LocationArea(val id: Int, val locationId: Int, val name: String) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(LocationAreasDBHelper.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(LocationAreasDBHelper.COL_LOCATION_ID)),
            if (cursor.isNull(cursor.getColumnIndex(LocationAreasDBHelper.COL_NAME))) {
                ""
            } else {
                cursor.getString(cursor.getColumnIndex(LocationAreasDBHelper.COL_NAME))
            })

    companion object {
        @JvmStatic
        fun create(context: Context, id: Int): LocationArea {
            Log.d("LA", "id : " + id)
            val db = LocationAreasDBHelper.getInstance(context)
            val cursor = db.readableDatabase.query(
                    LocationAreasDBHelper.TABLE_NAME,
                    null,
                    LocationAreasDBHelper.COL_ID + "=?",
                    arrayOf(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val locationArea = LocationArea(cursor)
            cursor.close()
            return locationArea
        }
    }

    fun hasName() = name.trim().length != 0

    fun findEncounterGameVersions(context: Context): ArrayList<Int> {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.Encounters.TABLE_NAME,
                null,
                "${PokeDB.Encounters.COL_LOCATION_AREA_ID}=?",
                arrayOf(id.toString()),
                null, null, null)
        cursor.moveToFirst()

        val versions = ArrayList<Int>(cursor.count)
        while (!cursor.isAfterLast) {
            versions.add(cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID)))
            cursor.moveToNext()
        }
        cursor.close()

        return versions
    }

    fun findAllEncounters(context: Context, versionId: Int): ArrayList<Encounter> {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.Encounters.TABLE_NAME,
                null,
                "${PokeDB.Encounters.COL_LOCATION_AREA_ID}=? AND " +
                        "${PokeDB.Encounters.COL_VERSION_ID}=?",
                arrayOf(id.toString(), versionId.toString()),
                null, null, null)
        cursor.moveToFirst()

        val encounters = ArrayList<Encounter>(cursor.count)
        while (!cursor.isAfterLast) {
            encounters.add(Encounter(cursor))
            cursor.moveToNext()
        }
        cursor.close()

        return encounters
    }

}
