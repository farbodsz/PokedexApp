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

class Shape(val id: Int) {

    val simpleName: String
    val technicalName: String

    init {
        simpleName = findName(id, false)
        technicalName = findName(id, true)
    }

    private fun findName(id: Int, technicalTerm: Boolean) = when (id) {
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
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
