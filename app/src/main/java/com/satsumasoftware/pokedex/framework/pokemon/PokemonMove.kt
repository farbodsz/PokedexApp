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
