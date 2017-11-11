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

class GrowthRate {

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

    fun findMaxExperience() = when (id) {
        1 -> 1250000
        2 -> 1000000
        3 -> 800000
        4 -> 1059860
        5 -> 600000
        6 -> 1640000
        else -> throw IllegalArgumentException("invalid growth rate id '$id'")
    }


    private fun findId(name: String) = when (name.toLowerCase()) {
        "fluctuating" -> 6
        "slow" -> 1
        "medium slow" -> 4
        "medium fast" -> 2
        "fast" -> 3
        "erratic" -> 5
        else -> throw IllegalArgumentException("invalid growth rate name '$name'")
    }

    private fun findName(id: Int) = when (id) {
        6 -> "Fluctuating"
        1 -> "Slow"
        4 -> "Medium Slow"
        2 -> "Medium Fast"
        3 -> "Fast"
        5 -> "Erratic"
        else -> throw IllegalArgumentException("invalid growth id '$id'")
    }

}
