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
