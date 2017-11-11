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

package com.satsumasoftware.pokedex.framework.version

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB

class Version(private val context: Context, val id: Int, val versionGroupId: Int) {

    val name: String by lazy {
        fetchNameFromDb(context)
    }

    constructor(context: Context, cursor: Cursor) : this(
            context,
            cursor.getInt(cursor.getColumnIndex(PokeDB.Versions.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Versions.COL_VERSION_GROUP_ID)))

    private fun fetchNameFromDb(context: Context): String {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.VersionNames.TABLE_NAME,
                null,
                "${PokeDB.VersionNames.COL_VERSION_ID}=? AND " +
                        "${PokeDB.VersionNames.COL_LOCAL_LANGUAGE_ID}=?",
                arrayOf(id.toString(), 9.toString()),
                null, null, null)
        cursor.moveToFirst()
        if (cursor.count == 0) throw NullPointerException("no name found")
        val name = cursor.getString(cursor.getColumnIndex(PokeDB.VersionNames.COL_NAME))
        cursor.close()
        return name
    }

}
