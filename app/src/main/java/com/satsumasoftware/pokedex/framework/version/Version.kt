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
