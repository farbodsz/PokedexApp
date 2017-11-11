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

import android.content.Context
import com.satsumasoftware.pokedex.db.PokeDB

class Experience {

    companion object {

        @JvmStatic
        fun getTotalExperience(context: Context, growthRate: GrowthRate, level: Int): Int {
            val pokeDB = PokeDB.getInstance(context)
            val cursor = pokeDB.readableDatabase.query(
                    PokeDB.Experience.TABLE_NAME,
                    null,
                    "${PokeDB.Experience.COL_GROWTH_RATE_ID}=? AND ${PokeDB.Experience.COL_LEVEL}=?",
                    arrayOf(growthRate.id.toString(), level.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val experience = cursor.getInt(cursor.getColumnIndex(PokeDB.Experience.COL_EXPERIENCE))
            cursor.close()
            return experience
        }

    }
}
