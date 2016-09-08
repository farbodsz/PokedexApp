package com.satsumasoftware.pokedex.framework.ability

import android.content.Context
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper
import com.satsumasoftware.pokedex.db.PokeDB

class Ability(val id: Int, val generationId: Int, val name: String, val japaneseName: String,
              val koreanName: String, val frenchName: String, val germanName: String,
              val spanishName: String, val italianName: String) : Parcelable {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(AbilitiesDBHelper.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(AbilitiesDBHelper.COL_GENERATION_ID)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_JAPANESE)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_KOREAN)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_FRENCH)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_GERMAN)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_SPANISH)),
            cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_ITALIAN)))

    // TODO is the Parcelable implementation redundant?
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString())

    // TODO use Settings options for version group and lang instead of defaults here
    @JvmOverloads
    fun getFlavorText(context: Context, versionGroupId: Int = 16, langId: Int = 9): String {
        val pokeDB = PokeDB.getInstance(context)
        val cursor = pokeDB.readableDatabase.query(
                PokeDB.AbilityFlavorText.TABLE_NAME,
                null,
                PokeDB.AbilityFlavorText.COL_ABILITY_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_VERSION_GROUP_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_LANGUAGE_ID + "=?",
                arrayOf(id.toString(), versionGroupId.toString(), langId.toString()),
                null, null, null)
        cursor.moveToFirst()
        val flavorText = cursor.getString(
                cursor.getColumnIndex(PokeDB.AbilityFlavorText.COL_FLAVOR_TEXT))
        cursor.close()
        return flavorText.replace("\n", " ") // to remove the line breaks
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeInt(generationId)
        dest?.writeString(name)
        dest?.writeString(japaneseName)
        dest?.writeString(koreanName)
        dest?.writeString(frenchName)
        dest?.writeString(germanName)
        dest?.writeString(spanishName)
        dest?.writeString(italianName)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Ability> = object : Parcelable.Creator<Ability> {
            override fun createFromParcel(source: Parcel): Ability = Ability(source)
            override fun newArray(size: Int): Array<Ability?> = arrayOfNulls(size)
        }
    }

}
