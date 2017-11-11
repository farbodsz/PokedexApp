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

package com.satsumasoftware.pokedex.framework.encounter

import java.util.*

class CompactEncounterDataHolder(val pokemonId: Int) {

    var minLevel = -1
        private set

    var maxLevel = -1
        private set

    var rarity = 0
        private set

    val encounterDataHolders = ArrayList<EncounterDataHolder>()

    fun addEncounterDataHolder(encounterDataHolder: EncounterDataHolder) {
        if (encounterDataHolder.encounter.pokemonId != pokemonId) {
            throw IllegalArgumentException("the pokemon id must match the one previously specified")
        }

        val encounter = encounterDataHolder.encounter
        val encounterMinLevel = encounter.minLevel
        val encounterMaxLevel = encounter.maxLevel

        if (minLevel == -1) {
            minLevel = encounterMinLevel
        } else if (encounterMinLevel < minLevel) {
            minLevel = encounterMinLevel
        }

        if (maxLevel == -1) {
            maxLevel = encounterMaxLevel
        } else if (encounterMaxLevel > maxLevel) {
            maxLevel = encounterMaxLevel
        }

        rarity += encounterDataHolder.encounterSlot.rarity

        encounterDataHolders.add(encounterDataHolder)
    }

}
