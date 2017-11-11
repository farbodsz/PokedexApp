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

class Pokedex(val id: Int) {

    val name: String

    init {
        name = findName(id)
    }

    private fun findName(id: Int) = when (id) {
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
        else -> throw IllegalArgumentException("invalid pokedex id '$id'")
    }

}
