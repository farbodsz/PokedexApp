package com.satsumasoftware.pokedex.entities.move;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.satsumasoftware.pokedex.db.PokeDB;

public final class MoveEffectProse {

    // TODO: Move these to 'Move' object?

    public static String getEffect(Context context, int moveEffectId, boolean shortEffect) {
        return getEffect(context, moveEffectId, 9, shortEffect);  // English language
    }

    public static String getEffect(Context context, int moveEffectId, int langId, boolean shortEffect) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.MoveEffectProse.TABLE_NAME,
                null,
                PokeDB.MoveEffectProse.COL_MOVE_EFFECT_ID + "=? AND " +
                        PokeDB.MoveEffectProse.COL_LOCAL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(moveEffectId), String.valueOf(langId)},
                null, null, null);
        cursor.moveToFirst();
        String effect;
        if (shortEffect) {
            effect = cursor.getString(cursor.getColumnIndex(PokeDB.MoveEffectProse.COL_SHORT_EFFECT));
        } else {
            effect = cursor.getString(cursor.getColumnIndex(PokeDB.MoveEffectProse.COL_EFFECT));
        }
        cursor.close();
        return effect;
    }

}
