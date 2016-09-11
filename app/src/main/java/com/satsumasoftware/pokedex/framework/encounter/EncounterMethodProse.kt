package com.satsumasoftware.pokedex.framework.encounter

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB

data class EncounterMethodProse(val encounterMethodId: Int, val name: String) {

    constructor(context: Context, encounterMethodId: Int) : this(
            encounterMethodId, context.fetchName(encounterMethodId))

}

private fun Context.fetchName(encounterMethodId: Int): String {
    val pokeDB = PokeDB.getInstance(this)
    val cursor = pokeDB.readableDatabase.query(
            PokeDB.EncounterMethodProse.TABLE_NAME,
            null,
            "${PokeDB.EncounterMethodProse.COL_ENCOUNTER_METHOD_ID}=? AND " +
                    "${PokeDB.EncounterMethodProse.COL_LOCAL_LANGUAGE_ID}=?",
            arrayOf(encounterMethodId.toString(), "9"),
            null, null, null)
    cursor.moveToFirst()
    cursor.moveToFirst()
    val name = cursor.getString(cursor.getColumnIndex(PokeDB.EncounterMethodProse.COL_NAME))
    cursor.close()
    return name
}
