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

package com.satsumasoftware.pokedex.util

import android.text.TextUtils

const val NULL_INT = 0

const val MAX_NATIONAL_ID = 721

private const val deprecationUseDb: String =
        "Instead use values from the relevant DB tables and objects"

fun formatPokemonId(id: Int): String {
    val strNum = id.toString()

    return when (strNum.length) {
        1 -> "00" + strNum
        2 -> "0" + strNum
        else -> strNum
    }
}

fun unformatPokemonId(formattedId: String): String {
    if (!TextUtils.isDigitsOnly(formattedId)) {
        return formattedId
    }

    var changeable = formattedId
    while (changeable.startsWith("0")) {
        changeable = changeable.replace("0", "")
    }
    return changeable
}


fun romanToGenId(romanNumerals: String) = when (romanNumerals.toUpperCase()) {
    "I" -> 1
    "II" -> 2
    "III" -> 3
    "IV" -> 4
    "V" -> 5
    "VI" -> 6
    else -> throw IllegalArgumentException("invalid roman numeral '$romanNumerals'")
}

fun genIdToRoman(generationId: Int) = when (generationId) {
    1 -> "I"
    2 -> "II"
    3 -> "III"
    4 -> "IV"
    5 -> "V"
    6 -> "VI"
    else -> throw IllegalArgumentException("invalid generation id '$generationId'")
}


@Deprecated(deprecationUseDb)
fun pokemonStatIdToName(statId: Int) = when (statId) {
    1 -> "HP"
    2 -> "Attack"
    3 -> "Defense"
    4 -> "Special Attack"
    5 -> "Special Defense"
    6 -> "Speed"
    7 -> "Accuracy"
    8 -> "Evasion"
    else -> throw IllegalArgumentException("invalid stat id '$statId'")
}


fun maleFromGenderRate(genderRateId: Int): Double {
    if (genderRateId == -1) {
        return -1.0  // genderless
    } else if (genderRateId >= 0 && genderRateId <= 8) {
        return 100.0 / 8.0 * (8.0 - genderRateId)
    } else {
        throw IllegalArgumentException("invalid gender rate id '$genderRateId'")
    }
}
