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

package com.satsumasoftware.pokedex.framework.pokemon

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB
import java.util.*

class PokemonLearnset(val context: Context, val pokemonId: Int, val pokemonMoveMethodId: Int,
                      val versionGroupId: Int) {

    val pokemonMoves: ArrayList<PokemonMove>

    init {
        val list = ArrayList<PokemonMove>()

        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.PokemonMoves.TABLE_NAME,
                null,
                "${PokeDB.PokemonMoves.COL_POKEMON_ID}=? AND " +
                        "${PokeDB.PokemonMoves.COL_POKEMON_MOVE_METHOD_ID}=? AND " +
                        "${PokeDB.PokemonMoves.COL_VERSION_GROUP_ID}=?",
                arrayOf(pokemonId.toString(), pokemonMoveMethodId.toString(),
                        versionGroupId.toString()),
                null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            list.add(PokemonMove(cursor))
            cursor.moveToNext()
        }
        cursor.close()

        pokemonMoves = list
    }

}
