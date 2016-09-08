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
