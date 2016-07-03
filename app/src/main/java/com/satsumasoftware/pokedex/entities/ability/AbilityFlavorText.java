package com.satsumasoftware.pokedex.entities.ability;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;

public final class AbilityFlavorText {

    // TODO: Move these to 'Ability' object?

    public static String getFlavorText(Context context, int abilityId) {
        return getFlavorText(context, abilityId, 16);  // latest version group
        // TODO call above with a default version (Settings option)
    }

    public static String getFlavorText(Context context, int abilityId, int versionGroupId) {
        return getFlavorText(context, abilityId, versionGroupId, 9);  // English language
        // TODO call above with a default language (Settings option)
    }

    public static String getFlavorText(Context context, int abilityId, int versionGroupId, int langId) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.AbilityFlavorText.TABLE_NAME,
                null,
                PokeDB.AbilityFlavorText.COL_ABILITY_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_VERSION_GROUP_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(abilityId), String.valueOf(versionGroupId), String.valueOf(langId)},
                null, null, null);
        cursor.moveToFirst();
        String flavorText = cursor.getString(cursor.getColumnIndex(PokeDB.AbilityFlavorText.COL_FLAVOR_TEXT));
        cursor.close();
        return flavorText.replace("\n", " ");  // to remove the line breaks
    }

}
