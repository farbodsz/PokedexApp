package com.satsumasoftware.pokedex.framework;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

public final class Experience {

    public static int getTotalExperience(Context context, GrowthRate growthRate, int level) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Experience.TABLE_NAME,
                null,
                PokeDB.Experience.COL_GROWTH_RATE_ID + "=? AND " + PokeDB.Experience.COL_LEVEL + "=?",
                new String[] {String.valueOf(growthRate.getId()), String.valueOf(level)},
                null, null, null);
        cursor.moveToFirst();
        int experience = cursor.getInt(cursor.getColumnIndex(PokeDB.Experience.COL_EXPERIENCE));
        cursor.close();
        return experience;
    }

}
