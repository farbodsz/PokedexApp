package com.satsumasoftware.pokedex.util

import android.text.TextUtils

const val NULL_INT = 0

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
fun growthNameToId(growthRate: String) = when (growthRate.toLowerCase()) {
    "fluctuating" -> 6
    "slow" -> 1
    "medium slow" -> 4
    "medium fast" -> 2
    "fast" -> 3
    "erratic" -> 5
    else -> throw IllegalArgumentException("invalid growth rate '$growthRate'")
}

@Deprecated(deprecationUseDb)
fun growthIdToName(id: Int) = when (id) {
    6 -> "Fluctuating"
    1 -> "Slow"
    4 -> "Medium Slow"
    2 -> "Medium Fast"
    3 -> "Fast"
    5 -> "Erratic"
    else -> throw IllegalArgumentException("invalid growth id '$id'")
}

@Deprecated(deprecationUseDb)
fun growthAbbrToFullName(abbreviation: String): String {
    return when (abbreviation.toLowerCase()) {
        "fl" -> "Fluctuating"
        "s" -> "Slow"
        "ms" -> "Medium Slow"
        "mf" -> "Medium Fast"
        "f" -> "Fast"
        "e" -> "Erratic"
        else -> throw IllegalArgumentException("invalid abbreviation '$abbreviation'")
    }
}

@Deprecated(deprecationUseDb)
fun growthIdGetMaxExp(growthRateId: Int) = when (growthRateId) {
    1 -> 1250000
    2 -> 1000000
    3 -> 800000
    4 -> 1059860
    5 -> 600000
    6 -> 1640000
    else -> throw IllegalArgumentException("invalid growth rate id '$growthRateId'")
}

@Deprecated(deprecationUseDb)
fun growthMaxExpToId(maxExp: Int) = when (maxExp) {
    1250000 -> 1
    1000000 -> 2
    800000 -> 3
    1059860 -> 4
    600000 -> 5
    1640000 -> 6
    else -> throw IllegalArgumentException("invalid max exp value '$maxExp'")
}


@Deprecated(deprecationUseDb)
fun typeNameToId(typeName: String) = when (typeName.toLowerCase()) {
    "normal" -> 1
    "fighting" -> 2
    "flying" -> 3
    "poison" -> 4
    "ground" -> 5
    "rock" -> 6
    "bug" -> 7
    "ghost" -> 8
    "steel" -> 9
    "fire" -> 10
    "water" -> 11
    "grass" -> 12
    "electric" -> 13
    "psychic" -> 14
    "ice" -> 15
    "dragon" -> 16
    "dark" -> 17
    "fairy" -> 18
    "unknown" -> 10001
    "shadow" -> 10002
    else -> throw IllegalArgumentException("invalid type name '$typeName'")
}

@Deprecated(deprecationUseDb)
fun typeIdToName(typeId: Int) = when (typeId) {
    1 -> "Normal"
    2 -> "Fighting"
    3 -> "Flying"
    4 -> "Poison"
    5 -> "Ground"
    6 -> "Rock"
    7 -> "Bug"
    8 -> "Ghost"
    9 -> "Steel"
    10 -> "Fire"
    11 -> "Water"
    12 -> "Grass"
    13 -> "Electric"
    14 -> "Psychic"
    15 -> "Ice"
    16 -> "Dragon"
    17 -> "Dark"
    18 -> "Fairy"
    10001 -> "Unknown"
    10002 -> "Shadow"
    else -> throw IllegalArgumentException("invalid type id '$typeId'")
}


@Deprecated(deprecationUseDb)
fun colourNameToId(colour: String) = when (colour.toLowerCase()) {
    "black" -> 1
    "blue" -> 2
    "brown" -> 3
    "grey" -> 4
    "green" -> 5
    "pink" -> 6
    "purple" -> 7
    "red" -> 8
    "white" -> 9
    "yellow" -> 10
    else -> throw IllegalArgumentException("invalid colour name '$colour'")
}

@Deprecated(deprecationUseDb)
fun colorIdToName(colorId: Int) = when (colorId) {
    1 -> "Black"
    2 -> "Blue"
    3 -> "Brown"
    4 -> "Grey"
    5 -> "Green"
    6 -> "Pink"
    7 -> "Purple"
    8 -> "Red"
    9 -> "White"
    10 -> "Yellow"
    else -> throw IllegalArgumentException("invalid colour id '$colorId'")
}


@Deprecated(deprecationUseDb)
fun shapeIdToName(shapeId: Int, technicalTerm: Boolean) = when (shapeId) {
    1 -> if (technicalTerm) "Pomaceous" else "Ball"
    2 -> if (technicalTerm) "Caudal" else "Squiggle"
    3 -> if (technicalTerm) "Ichthyic" else "Fish"
    4 -> if (technicalTerm) "Brachial" else "Arms"
    5 -> if (technicalTerm) "Alvine" else "Blob"
    6 -> if (technicalTerm) "Sciurine" else "Upright"
    7 -> if (technicalTerm) "Crural" else "Legs"
    8 -> if (technicalTerm) "Mensal" else "Quadruped"
    9 -> if (technicalTerm) "Alar" else "Wings"
    10 -> if (technicalTerm) "Cilial" else "Tentacles"
    11 -> if (technicalTerm) "Polycephalic" else "Heads"
    12 -> if (technicalTerm) "Anthropomorphic" else "Humanoid"
    13 -> if (technicalTerm) "Lepidopterous" else "Bug wings"
    14 -> if (technicalTerm) "Chitinous" else "Armor"
    else -> throw IllegalArgumentException("invalid shape id '$shapeId'")
}

@Deprecated(deprecationUseDb)
fun shapeNameToId(shape: String) = when (shape) {
    "Pomaceous", "Ball" -> 1
    "Caudal", "Squiggle" -> 2
    "Ichthyic", "Fish" -> 3
    "Brachial", "Arms" -> 4
    "Alvine", "Blob" -> 5
    "Sciurine", "Upright" -> 6
    "Crural", "Legs" -> 7
    "Mensal", "Quadruped" -> 8
    "Alar", "Wings" -> 9
    "Cilial", "Tentacles" -> 10
    "Polycephalic", "Heads" -> 11
    "Anthropomorphic", "Humanoid" -> 12
    "Lepidopterous", "Bug wings" -> 13
    "Chitinous", "Armor" -> 14
    else -> throw IllegalArgumentException("invalid shape name '$shape'")
}

@Deprecated(deprecationUseDb)
fun getTechnicalShapeFromSimple(simpleShapeName: String) = when (simpleShapeName) {
    "Ball" -> "Pomaceous"
    "Squiggle" -> "Caudal"
    "Fish" -> "Ichthyic"
    "Arms" -> "Brachial"
    "Blob" -> "Alvine"
    "Upright" -> "Sciurine"
    "Legs" -> "Crural"
    "Quadruped" -> "Mensal"
    "Wings" -> "Alar"
    "Tentacles" -> "Cilial"
    "Heads" -> "Polycephalic"
    "Humanoid" -> "Anthropomorphic"
    "Bug wings" -> "Lepidopterous"
    "Armor" -> "Chitinous"
    else -> throw IllegalArgumentException("invalid simple shape name '$simpleShapeName'")
}


@Deprecated(deprecationUseDb)
fun habitatNameToId(habitat: String) = when (habitat) {
    // Habitats are something only in Pokemon FireRed and LeafGreen
    "Cave" -> 1
    "Forest" -> 2
    "Grassland" -> 3
    "Mountain" -> 4
    "Rare" -> 5
    "Rough Terrain" -> 6
    "Sea" -> 7
    "Urban" -> 8
    "Water's Edge" -> 9
    else -> throw IllegalArgumentException("invalid habitat name '$habitat'")
}

@Deprecated(deprecationUseDb)
fun habitatIdToName(habitatId: Int) = when (habitatId) {
    // Habitats are something only in Pokemon FireRed and LeafGreen
    1 -> "Cave"
    2 -> "Forest"
    3 -> "Grassland"
    4 -> "Mountain"
    5 -> "Rare"
    6 -> "Rough Terrain"
    7 -> "Sea"
    8 -> "Urban"
    9 -> "Water's Edge"
    else -> throw IllegalArgumentException("invalid habitat id '$habitatId'")
}


@Deprecated(deprecationUseDb)
fun regionIdToName(regionId: Int) = when (regionId) {
    1 -> "Kanto"
    2 -> "Johto"
    3 -> "Hoenn"
    4 -> "Sinnoh"
    5 -> "Unova"
    6 -> "Kalos"
    else -> throw IllegalArgumentException("invalid region id '$regionId'")
}

@Deprecated(deprecationUseDb)
fun regionNameToId(regionName: String) = when (regionName.toLowerCase()) {
    "kanto" -> 1
    "johto" -> 2
    "hoenn" -> 3
    "sinnoh" -> 4
    "unova" -> 5
    "kalos" -> 6
    else -> throw IllegalArgumentException("invalid region name '$regionName'")
}


@Deprecated(deprecationUseDb)
fun pokedexIdToName(pokedexId: Int) = when (pokedexId) {
    1 -> "National"
    2 -> "Kanto"
    3 -> "Original Johto"
    4 -> "Original Hoenn"
    5 -> "Original Sinnoh"
    6 -> "Extended Sinnoh"
    7 -> "Updated Johto"
    8 -> "Original Unova"
    9 -> "Updated Unova"
    11 -> "Conquest Gallery"
    12 -> "Central Kalos"
    13 -> "Coastal Kalos"
    14 -> "Mountain Kalos"
    15 -> "New Hoenn"
    else -> throw IllegalArgumentException("invalid pokedex id '$pokedexId'")
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
