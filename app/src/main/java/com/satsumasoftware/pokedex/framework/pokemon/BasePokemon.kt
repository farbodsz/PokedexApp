package com.satsumasoftware.pokedex.framework.pokemon

import com.satsumasoftware.pokedex.db.PokemonDBHelper

open class BasePokemon(val id: Int, val speciesId: Int, val formId: Int, val name: String,
                       val formName: String, formAndPokemonName: String?,
                       val nationalDexNumber: Int) {

    val formAndPokemonName: String? = formAndPokemonName
        get() = field ?: name

    companion object {
        @JvmField val DB_COLUMNS = arrayOf(PokemonDBHelper.COL_ID,
                PokemonDBHelper.COL_SPECIES_ID,
                PokemonDBHelper.COL_FORM_ID,
                PokemonDBHelper.COL_NAME,
                PokemonDBHelper.COL_FORM_NAME,
                PokemonDBHelper.COL_FORM_POKEMON_NAME,
                PokemonDBHelper.COL_IS_DEFAULT,
                PokemonDBHelper.COL_FORM_IS_DEFAULT,
                PokemonDBHelper.COL_FORM_IS_MEGA,
                PokemonDBHelper.COL_POKEDEX_NATIONAL)
    }

}
