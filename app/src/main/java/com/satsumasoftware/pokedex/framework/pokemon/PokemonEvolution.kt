package com.satsumasoftware.pokedex.framework.pokemon

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.framework.Gender
import com.satsumasoftware.pokedex.framework.location.Location
import com.satsumasoftware.pokedex.framework.move.MiniMove
import com.satsumasoftware.pokedex.util.NULL_INT
import com.satsumasoftware.pokedex.util.regionIdToName
import com.satsumasoftware.pokedex.util.typeIdToName

data class PokemonEvolution(val id: Int, val evolvedSpeciesId: Int, val evolutionTriggerId: Int,
                            val triggerItemId: Int, val minimumLevel: Int, val genderId: Int,
                            val locationId: Int, val heldItemId: Int, val timeOfDay: String,
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
            cursor.getString(cursor.getColumnIndex(PokeDB.PokemonEvolution.COL_TIME_OF_DAY)),
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
                val levelText = if (minimumLevel == NULL_INT) {
                    "Level-up"
                } else {
                    "Level " + minimumLevel
                }
                description.append(levelText)
            }
            2 -> {
                val tradeText = if (tradeSpeciesId == NULL_INT)
                    "Trade"
                else
                    "Trade with " + MiniPokemon(context, tradeSpeciesId, false).name
                description.append(tradeText)
            }
            3 -> description.append("Use item")  // TODO specify item
            4 -> description.append("By shed")
        }

        if (genderId != NULL_INT) {
            description.append(" (${Gender(genderId).getSymbol()} only)")
        }

        if (locationId != NULL_INT) {
            val location = Location(context, locationId)
            description.append(", around ${location.name} (${regionIdToName(location.regionId)})")
        }

        if (heldItemId != NULL_INT) {
            description.append(", while holding an item")  // TODO specify item
        }

        if (!timeOfDay.equals("")) {
            description.append(", during the $timeOfDay")  // TODO specify item
        }

        if (knownMoveId != NULL_INT) {
            description.append(", while knowing ${MiniMove(context, knownMoveId).name}")
        }

        if (knownMoveTypeId != NULL_INT) {
            description.append(", while knowing a ${typeIdToName(knownMoveTypeId)}-type move")
        }

        if (minimumHappiness != NULL_INT) {
            description.append(", with at least $minimumHappiness happiness")
        }

        if (minimumBeauty != NULL_INT) {
            description.append(", with at least $minimumBeauty beauty")
        }

        if (minimumAffection != NULL_INT) {
            description.append(", with at least $minimumAffection affection")
        }

        // TODO relativePhysicalStats

        if (partySpeciesId != NULL_INT) {
            description.append(", with ${MiniPokemon(context, partySpeciesId, false).name} in the party")
        }

        if (partyTypeId != NULL_INT) {
            description.append(", with a ${typeIdToName(partyTypeId)}-type Pokémon in the party")
        }

        if (needsOverworldRain) {
            description.append(", while it is raining outside of battle")
        }

        if (turnUpsideDown) {
            description.append(", with the 3DS turned upside down")
        }

        return description.toString()
    }

}
