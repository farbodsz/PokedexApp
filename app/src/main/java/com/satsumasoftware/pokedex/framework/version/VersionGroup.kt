package com.satsumasoftware.pokedex.framework.version

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB
import java.util.*

data class VersionGroup(private val context: Context, val id: Int, val generationId: Int) {

    val name: String by lazy {
        fetchNameFromDb(context)
    }

    constructor(context: Context, cursor: Cursor) : this(
            context,
            cursor.getInt(cursor.getColumnIndex(PokeDB.VersionGroups.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.VersionGroups.COL_GENERATION_ID)))

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
            versionNames.add(Version(context, cursor).name)
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
