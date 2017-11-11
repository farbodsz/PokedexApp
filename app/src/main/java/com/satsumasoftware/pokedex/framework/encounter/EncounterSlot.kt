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
