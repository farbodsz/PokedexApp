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

import com.satsumasoftware.pokedex.db.PokemonDBHelper

open class BasePokemon(val id: Int, val speciesId: Int, val formId: Int, val name: String,
                       val formName: String?, formAndPokemonName: String?,
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
