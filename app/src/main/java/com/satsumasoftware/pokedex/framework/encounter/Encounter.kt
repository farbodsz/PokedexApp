package com.satsumasoftware.pokedex.framework.encounter

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB

data class Encounter(val id: Int, val versionId: Int, val locationAreaId: Int,
                     val encounterSlotId: Int, val pokemonId: Int, val minLevel: Int,
                     val maxLevel: Int) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_LOCATION_AREA_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_ENCOUNTER_SLOT_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_POKEMON_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MIN_LEVEL)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_MAX_LEVEL)))

    fun getEncounterSlotObject(context: Context): EncounterSlot {
        val pokeDB = PokeDB(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.EncounterSlots.TABLE_NAME,
                null,
                "${PokeDB.EncounterSlots.COL_ID} = ?",
                arrayOf<String>(encounterSlotId.toString()),
                null, null, null)
        cursor.moveToFirst()
        val encounterSlot = EncounterSlot(cursor)
        cursor.close()
        return encounterSlot
    }
}
