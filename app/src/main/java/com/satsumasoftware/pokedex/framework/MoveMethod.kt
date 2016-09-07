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
