package com.satsumasoftware.pokedex.framework.encounter

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB

class EncounterMethodProse(val encounterMethodId: Int, val name: String) {

    companion object {
        @JvmStatic
        fun create(context: Context, id: Int): EncounterMethodProse {
            val pokeDB = PokeDB.getInstance(context)
            val cursor = pokeDB.readableDatabase.query(
                    PokeDB.EncounterMethodProse.TABLE_NAME,
                    null,
                    "${PokeDB.EncounterMethodProse.COL_ENCOUNTER_METHOD_ID}=? AND " +
                            "${PokeDB.EncounterMethodProse.COL_LOCAL_LANGUAGE_ID}=?",
                    arrayOf(id.toString(), 9.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndex(PokeDB.EncounterMethodProse.COL_NAME))
            cursor.close()
            return EncounterMethodProse(id, name)
        }
    }

}
