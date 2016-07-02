package com.satsumasoftware.pokedex.entities.move;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.satsumasoftware.pokedex.db.MoveEffectProseDBHelper;

public final class MoveEffectProse {

    // TODO: Move these to 'Move' object?

    public static String getEffect(Context context, int moveEffectId, boolean shortEffect) {
        return getEffect(context, moveEffectId, 9, shortEffect);  // English language
    }

    public static String getEffect(Context context, int moveEffectId, int langId, boolean shortEffect) {
        MoveEffectProseDBHelper helper = new MoveEffectProseDBHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(
                MoveEffectProseDBHelper.TABLE_NAME,
                null,
                MoveEffectProseDBHelper.COL_ID + "=? AND " + MoveEffectProseDBHelper.COL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(moveEffectId), String.valueOf(langId)},
                null, null, null);
        cursor.moveToFirst();
        String effect;
        if (shortEffect) {
            effect = cursor.getString(cursor.getColumnIndex(MoveEffectProseDBHelper.COL_SHORT_EFFECT));
        } else {
            effect = cursor.getString(cursor.getColumnIndex(MoveEffectProseDBHelper.COL_EFFECT));
        }
        cursor.close();
        return effect;
    }

}
