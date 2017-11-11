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

package com.satsumasoftware.pokedex.framework

class EggGroup(val id: Int) {

    val name: String

    init {
        name = findName(id)
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Monster"
        2 -> "Water 1"
        3 -> "Bug"
        4 -> "Flying"
        5 -> "Field"
        6 -> "Fairy"
        7 -> "Grass"
        8 -> "Human-Like"
        9 -> "Water 3"
        10 -> "Mineral"
        11 -> "Amorphous"
        12 -> "Water 2"
        13 -> "Ditto"
        14 -> "Dragon"
        15 -> "Undiscovered"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
