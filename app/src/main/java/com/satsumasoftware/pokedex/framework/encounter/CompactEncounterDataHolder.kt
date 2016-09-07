package com.satsumasoftware.pokedex.framework.encounter

import java.util.ArrayList

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
