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

class Habitat {

    // Habitats are something only in Pokemon FireRed and LeafGreen

    val id: Int
    val name: String

    constructor(id: Int) {
        this.id = id
        this.name = findName(id)
    }

    @Deprecated("Use the constructor taking id instead")
    constructor(name: String) {
        this.id = findId(name)
        this.name = name
    }

    private fun findId(name: String) = when (name.toLowerCase()) {
        "cave" -> 1
        "forest" -> 2
        "grassland" -> 3
        "mountain" -> 4
        "rare" -> 5
        "rough terrain" -> 6
        "sea" -> 7
        "urban" -> 8
        "water's edge" -> 9
        else -> throw IllegalArgumentException("invalid habitat name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        1 -> "Cave"
        2 -> "Forest"
        3 -> "Grassland"
        4 -> "Mountain"
        5 -> "Rare"
        6 -> "Rough Terrain"
        7 -> "Sea"
        8 -> "Urban"
        9 -> "Water's Edge"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
