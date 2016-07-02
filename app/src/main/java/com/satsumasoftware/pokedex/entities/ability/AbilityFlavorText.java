package com.satsumasoftware.pokedex.entities.ability;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.satsumasoftware.pokedex.db.AbilityFlavorDBHelper;

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
        AbilityFlavorDBHelper helper = new AbilityFlavorDBHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(
                AbilityFlavorDBHelper.TABLE_NAME,
                null,
                AbilityFlavorDBHelper.COL_ABILITY_ID + "=? AND " +
                        AbilityFlavorDBHelper.COL_VERSION_GROUP_ID + "=? AND " +
                        AbilityFlavorDBHelper.COL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(abilityId), String.valueOf(versionGroupId), String.valueOf(langId)},
                null, null, null);
        cursor.moveToFirst();
        String flavorText = cursor.getString(cursor.getColumnIndex(AbilityFlavorDBHelper.COL_FLAVOR_TEXT));
        cursor.close();
        return flavorText.replace("\n", " ");  // to remove the line breaks
    }

}
