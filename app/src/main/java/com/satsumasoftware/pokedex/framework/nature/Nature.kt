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

package com.satsumasoftware.pokedex.framework.nature

import android.database.Cursor
import com.satsumasoftware.pokedex.db.NaturesDBHelper

class Nature(val id: Int, val decreasedStatId: Int, val increasedStatId: Int,
             val hatesFlavorId: Int, val likesFlavorId: Int, val name: String,
             val japaneseName: String, val koreanName: String, val frenchName: String,
             val germanName: String, val spanishName: String, val italianName: String) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_DECREASED_STAT_ID)),
            cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_INCREASED_STAT_ID)),
            cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_HATES_FLAVOR_ID)),
            cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_LIKES_FLAVOR_ID)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_JAPANESE)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_KOREAN)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_FRENCH)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_GERMAN)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_SPANISH)),
            cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_ITALIAN)))

}
