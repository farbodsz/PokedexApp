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
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.framework.move.MiniMove
import com.satsumasoftware.pokedex.util.NULL_INT

data class PokemonMove(val moveId: Int, val level: Int, val orderNumber: Int) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_MOVE_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_LEVEL)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_ORDER)))

    fun toMiniMove(context: Context) = MiniMove.create(context, moveId)

    fun hasLearnLevel() = level != NULL_INT

}
