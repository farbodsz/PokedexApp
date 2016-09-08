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
