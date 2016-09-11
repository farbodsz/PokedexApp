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
