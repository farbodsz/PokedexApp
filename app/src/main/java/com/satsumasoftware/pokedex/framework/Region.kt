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

class Region {

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

    private fun findName(id: Int) = when (id) {
        0 -> "Not a region"
        1 -> "Kanto"
        2 -> "Johto"
        3 -> "Hoenn"
        4 -> "Sinnoh"
        5 -> "Unova"
        6 -> "Kalos"
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

    private fun findId(name: String) = when (name.toLowerCase()) {
        "kanto" -> 1
        "johto" -> 2
        "hoenn" -> 3
        "sinnoh" -> 4
        "unova" -> 5
        "kalos" -> 6
        else -> throw IllegalArgumentException("invalid region name '$name'")
    }

}
