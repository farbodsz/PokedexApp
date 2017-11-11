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

package com.satsumasoftware.pokedex.framework

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB

class MoveMethod(private val context: Context, val id: Int) {

    val name: String by lazy {
        fetchNameFromDb(context)
    }

    fun isLevelUpMethod() = id == 1

    private fun fetchNameFromDb(context: Context): String {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.PokemonMoveMethodProse.TABLE_NAME,
                null,
                "${PokeDB.PokemonMoveMethodProse.COL_POKEMON_MOVE_METHOD_ID}=? AND " +
                        "${PokeDB.PokemonMoveMethodProse.COL_LOCAL_LANGUAGE_ID}=?",
                arrayOf(id.toString(), "9"),
                null, null, null)
        cursor.moveToFirst()
        val name = cursor.getString(cursor.getColumnIndex(PokeDB.PokemonMoveMethodProse.COL_NAME))
        cursor.close()
        return name
    }

}
