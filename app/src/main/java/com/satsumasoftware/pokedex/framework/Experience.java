package com.satsumasoftware.pokedex.framework;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

public class Experience {

    public static final int GROWTH_FLUCTUATING = 1;
    public static final int GROWTH_SLOW = 2;
    public static final int GROWTH_MEDIUM_SLOW = 3;
    public static final int GROWTH_MEDIUM_FAST = 4;
    public static final int GROWTH_FAST = 5;
    public static final int GROWTH_ERRATIC = 6;

    public static int getTotalExperience(Context context, int growthId, int level) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Experience.TABLE_NAME,
                null,
                PokeDB.Experience.COL_GROWTH_RATE_ID + "=? AND " + PokeDB.Experience.COL_LEVEL + "=?",
                new String[] {String.valueOf(growthId), String.valueOf(level)},
                null, null, null);
        cursor.moveToFirst();
        int experience = cursor.getInt(cursor.getColumnIndex(PokeDB.Experience.COL_EXPERIENCE));
        cursor.close();
        return experience;
    }

    public static int getGrowthIdFromString(String string) {
        switch (string.toLowerCase()) {
            case "fluctuating": return GROWTH_FLUCTUATING;
            case "slow": return GROWTH_SLOW;
            case "medium slow": return GROWTH_MEDIUM_SLOW;
            case "medium fast": return GROWTH_MEDIUM_FAST;
            case "fast": return GROWTH_FAST;
            case "erratic": return GROWTH_ERRATIC;
            default: throw new IllegalArgumentException("The string in the parameter is not a recognised growth rate");
        }
    }

}
