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

class Color {

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
        else -> throw IllegalArgumentException("invalid color name '$name'")
    }

    private fun findName(id: Int) = when (id) {
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
        else -> throw IllegalArgumentException("invalid id '$id'")
    }

}
