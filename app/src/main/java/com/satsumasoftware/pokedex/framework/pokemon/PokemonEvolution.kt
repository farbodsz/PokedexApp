package com.satsumasoftware.pokedex.framework.pokemon

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.util.NULL_INT

data class PokemonEvolution(val id: Int, val evolvedSpeciesId: Int, val evolutionTriggerId: Int,
                            val triggerItemId: Int, val minimumLevel: Int, val genderId: Int,
                            val locationId: Int, val heldItemId: Int, val timeOfDay: Int,
                            val knownMoveId: Int, val knownMoveTypeId: Int,
                            val minimumHappiness: Int, val minimumBeauty: Int,
                            val minimumAffection: Int, val relativePhysicalStats: Int,
                            val partySpeciesId: Int, val partyTypeId: Int, val tradeSpeciesId: Int,
                            val needsOverworldRain: Boolean, val turnUpsideDown: Boolean) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_EVOLVED_SPECIES_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_EVOLUTION_TRIGGER_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_TRIGGER_ITEM_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_MINIMUM_LEVEL)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_GENDER_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_LOCATION_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_HELD_ITEM_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_TIME_OF_DAY)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_KNOWN_MOVE_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_KNOWN_MOVE_TYPE_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_MINIMUM_HAPPINESS)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_MINIMUM_BEAUTY)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_MINIMUM_AFFECTION)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_RELATIVE_PHYSICAL_STATS)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_PARTY_SPECIES_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_PARTY_TYPE_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_TRADE_SPECIES_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_NEEDS_OVERWORLD_RAIN)) == 1,
            cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_TURN_UPSIDE_DOWN)) == 1)


    fun makeDescriptionText(context: Context): String {
        // TODO get string values via the database for different langs

        val description = StringBuilder()

        when (evolutionTriggerId) {
            1 -> {
                val levelText = "Lv. " + minimumLevel
                description.append(levelText)
            }
            2 -> {
                val tradeText = if (tradeSpeciesId == NULL_INT)
                    "Trade"
                else
                    "Trade with " + MiniPokemon(context, tradeSpeciesId, false).name
                description.append(tradeText)
            }
            3 -> description.append("By item")
            4 -> description.append("By shed")
        }

        return description.toString()
    }

}
