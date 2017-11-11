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

package com.satsumasoftware.pokedex.framework.ability

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper

class MiniAbility(val id: Int, val name: String) : Parcelable {

    constructor(source: Parcel) : this(source.readInt(), source.readString())

    fun toAbility(context: Context): Ability {
        val helper = AbilitiesDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                AbilitiesDBHelper.TABLE_NAME,
                null,
                "${AbilitiesDBHelper.COL_ID}=?",
                arrayOf(id.toString()),
                null, null, null)
        cursor.moveToFirst()
        val ability = Ability(cursor)
        cursor.close()
        return ability
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MiniAbility> = object : Parcelable.Creator<MiniAbility> {
            override fun createFromParcel(source: Parcel): MiniAbility = MiniAbility(source)
            override fun newArray(size: Int): Array<MiniAbility?> = arrayOfNulls(size)
        }

        @JvmField val DB_COLUMNS = arrayOf(AbilitiesDBHelper.COL_ID, AbilitiesDBHelper.COL_NAME)

        @JvmStatic
        fun create(context: Context, id: Int): MiniAbility {
            val helper = AbilitiesDBHelper.getInstance(context)
            val cursor = helper.readableDatabase.query(
                    AbilitiesDBHelper.TABLE_NAME,
                    DB_COLUMNS,
                    "${AbilitiesDBHelper.COL_ID}=?",
                    arrayOf(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME))
            cursor.close()
            return MiniAbility(id, name)
        }

    }

}
