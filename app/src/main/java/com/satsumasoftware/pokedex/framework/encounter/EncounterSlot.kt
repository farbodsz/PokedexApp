package com.satsumasoftware.pokedex.framework.encounter

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB

class EncounterSlot(val id: Int, val versionGroupId: Int, val encounterMethodId: Int,
                    val slot: Int, val rarity: Int) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_VERSION_GROUP_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_ENCOUNTER_METHOD_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_SLOT)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_RARITY)))

    companion object {
        @JvmStatic
        fun create(context: Context, id: Int): EncounterSlot {
            val pokeDB = PokeDB.getInstance(context)
            val cursor = pokeDB.readableDatabase.query(
                    PokeDB.EncounterSlots.TABLE_NAME,
                    null,
                    "${PokeDB.EncounterSlots.COL_ID} = ?",
                    arrayOf(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val encounterSlot = EncounterSlot(cursor)
            cursor.close()
            return encounterSlot
        }
    }

}
