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

package com.satsumasoftware.pokedex.framework.move

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.satsumasoftware.pokedex.db.MovesDBHelper

class MiniMove(val id: Int, val name: String) : Parcelable {

    constructor(source: Parcel) : this(source.readInt(), source.readString())

    fun toMove(context: Context): Move {
        val helper = MovesDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                MovesDBHelper.TABLE_NAME,
                null,
                "${MovesDBHelper.COL_ID}=?",
                arrayOf(id.toString()),
                null, null, null)
        cursor.moveToFirst()
        val move = Move(cursor)
        cursor.close()
        return move
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MiniMove> = object : Parcelable.Creator<MiniMove> {
            override fun createFromParcel(source: Parcel): MiniMove = MiniMove(source)
            override fun newArray(size: Int): Array<MiniMove?> = arrayOfNulls(size)
        }

        @JvmField val DB_COLUMNS = arrayOf(MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME)

        @JvmStatic
        fun create(context: Context, id: Int): MiniMove {
            val helper = MovesDBHelper.getInstance(context)
            val cursor = helper.readableDatabase.query(
                    MovesDBHelper.TABLE_NAME,
                    arrayOf(MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME),
                    MovesDBHelper.COL_ID + "=?",
                    arrayOf<String>(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME))
            cursor.close()
            return MiniMove(id, name)
        }
    }

}
