package com.satsumasoftware.pokedex.framework.move

import android.content.Context
import android.database.Cursor
import com.satsumasoftware.pokedex.db.MovesDBHelper
import com.satsumasoftware.pokedex.db.PokeDB
import com.satsumasoftware.pokedex.util.NULL_INT
import java.util.regex.Pattern

class Move(val id: Int, val generationId: Int, val typeId: Int, val power: Int, val pp: Int,
           val accuracy: Int, val priority: Int, val targetId: Int, val damageClassId: Int,
           val effectId: Int, val effectChance: Int, val contestTypeId: Int,
           val contestEffectId: Int, val superContestEffectId: Int, val name: String,
           val japaneseName: String, val koreanName: String, val frenchName: String,
           val germanName: String, val spanishName: String, val italianName: String) {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_GENERATION_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_TYPE_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_POWER)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_PP)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_ACCURACY)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_PRIORITY)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_TARGET_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_DAMAGE_CLASS_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_EFFECT_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_EFFECT_CHANCE)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_CONTEST_TYPE_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_CONTEST_EFFECT_ID)),
            cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_SUPER_CONTEST_EFFECT_ID)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_JAPANESE)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_KOREAN)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_FRENCH)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_GERMAN)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_SPANISH)),
            cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_ITALIAN)))

    fun hasPower() = power != NULL_INT

    fun hasAccuracy() = accuracy != NULL_INT

    fun hasEffectChance() = effectChance != NULL_INT

    fun hasContestType() = contestTypeId != NULL_INT

    fun hasContestEffect() = contestEffectId != NULL_INT

    fun hasSuperContestEffect() = superContestEffectId != NULL_INT

    @JvmOverloads
    fun getEffectProse(context: Context, shortEffect: Boolean, langId: Int = 9): String {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.MoveEffectProse.TABLE_NAME,
                null,
                "${PokeDB.MoveEffectProse.COL_MOVE_EFFECT_ID}=? AND " +
                        "${PokeDB.MoveEffectProse.COL_LOCAL_LANGUAGE_ID}=?",
                arrayOf(effectId.toString(), langId.toString()),
                null, null, null)
        cursor.moveToFirst()
        var effectText = if (shortEffect) {
            cursor.getString(cursor.getColumnIndex(PokeDB.MoveEffectProse.COL_SHORT_EFFECT))
        } else {
            cursor.getString(cursor.getColumnIndex(PokeDB.MoveEffectProse.COL_EFFECT))
        }
        cursor.close()

        effectText = effectText.replace("\$effect_chance", effectChance.toString())

        // matching the pattern: [label]{category:target}
        val pattern = Pattern.compile("\\[(.*?)\\]\\{(.*?):(.*?)\\}")

        val matcher = pattern.matcher(effectText)

        while (matcher.find()) {
            val entirePattern = matcher.group(0)
            val label = matcher.group(1)
            val category = matcher.group(2)
            val target = matcher.group(3)

            val displayedText = if (label == "") target else label

            effectText = effectText.replace(entirePattern, displayedText)
        }

        return effectText
    }

}
