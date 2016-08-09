package com.satsumasoftware.pokedex.framework.version

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB
import java.util.*

data class VersionGroup(val id: Int, val generationId: Int) {

    private var name: String? = null

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.VersionGroups.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.VersionGroups.COL_GENERATION_ID)))


    fun fetchName(context: Context): String {
        if (name == null) {
            name = fetchNameFromDb(context)
        }
        return name!!
    }

    private fun fetchNameFromDb(context: Context): String {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.Versions.TABLE_NAME,
                null,
                "${PokeDB.Versions.COL_VERSION_GROUP_ID}=?",
                arrayOf(id.toString()),
                null, null, null)
        val versionNames = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            versionNames.add(Version(cursor).fetchName(context))
            cursor.moveToNext()
        }
        cursor.close()

        return if (versionNames.size == 1) {
            versionNames[0]
        } else {
            "${versionNames[0]} & ${versionNames[1]}"
        }
    }

}
