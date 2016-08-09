package com.satsumasoftware.pokedex.framework.version

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.PokeDB

data class Version(val id: Int, val versionGroupId: Int) {

    private var name: String? = null

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokeDB.Versions.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokeDB.Versions.COL_VERSION_GROUP_ID)))


    fun fetchName(context: Context): String {
        if (name == null) {
            name = fetchNameFromDb(context)
        }
        return name!!
    }

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
