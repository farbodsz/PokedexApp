package com.satsumasoftware.pokedex.framework.location

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.framework.encounter.Encounter
import java.util.*

data class LocationArea(val id: Int, val locationId: Int, val name: String) {

    fun findEncounterGameVersions(context: Context): ArrayList<Int> {
        val pokeDB = PokeDB(context)
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
        val pokeDB = PokeDB(context)
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
