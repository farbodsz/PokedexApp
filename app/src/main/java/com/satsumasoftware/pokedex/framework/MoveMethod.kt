package com.satsumasoftware.pokedex.framework

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB

data class MoveMethod(val id: Int) {

    private var name: String? = null

    fun fetchName(context: Context): String {
        if (name == null) {
            name = fetchNameFromDb(context)
        }
        return name!!
    }

    private fun fetchNameFromDb(context: Context): String {
        val pokeDB = PokeDB(context)
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
