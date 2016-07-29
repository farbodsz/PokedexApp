package com.satsumasoftware.pokedex.framework.encounter

import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB

data class EncounterSlot(val id: Int, val versionGroupId: Int, val encounterMethodId: Int,
                         val slot: Int, val rarity: Int) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_VERSION_GROUP_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_ENCOUNTER_METHOD_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_SLOT)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.EncounterSlots.COL_RARITY)))

}
