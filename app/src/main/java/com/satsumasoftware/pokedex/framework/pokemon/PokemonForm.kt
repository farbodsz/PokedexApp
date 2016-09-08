package com.satsumasoftware.pokedex.framework.pokemon

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.satsumasoftware.pokedex.db.PokemonDBHelper
import com.satsumasoftware.pokedex.util.ActionUtils
import com.satsumasoftware.pokedex.util.formatPokemonId

class PokemonForm(val id: Int, val speciesId: Int, val formId: Int, val name: String,
                  val formName: String?, val combinedName: String?, val nationalDexNumber: Int,
                  val primaryTypeId: Int, val isDefault: Boolean, val isFormDefault: Boolean,
                  val isFormMega: Boolean) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            1.equals(source.readInt()),
            1.equals(source.readInt()),
            1.equals(source.readInt()))

    fun toMiniPokemon(context: Context): MiniPokemon {
        val helper = PokemonDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                PokemonDBHelper.TABLE_NAME,
                arrayOf(PokemonDBHelper.COL_ID, PokemonDBHelper.COL_FORM_IS_DEFAULT),
                "${PokemonDBHelper.COL_ID}=? AND ${PokemonDBHelper.COL_FORM_IS_DEFAULT}=?",
                arrayOf(id.toString(), 1.toString()),
                null, null, null)
        cursor.moveToFirst()
        val miniPokemon = MiniPokemon(id, speciesId, formId, name, formName, combinedName,
                nationalDexNumber)
        cursor.close()
        return miniPokemon
    }

    fun setPokemonImage(imageView: ImageView) {
        ActionUtils.setPokemonImage(id,
                formatPokemonId(speciesId),
                name,
                isFormMega,
                imageView)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeInt(speciesId)
        dest?.writeInt(formId)
        dest?.writeString(name)
        dest?.writeString(formName)
        dest?.writeString(combinedName)
        dest?.writeInt(nationalDexNumber)
        dest?.writeInt(primaryTypeId)
        dest?.writeInt((if (isDefault) 1 else 0))
        dest?.writeInt((if (isFormDefault) 1 else 0))
        dest?.writeInt((if (isFormMega) 1 else 0))
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PokemonForm> = object : Parcelable.Creator<PokemonForm> {
            override fun createFromParcel(source: Parcel): PokemonForm = PokemonForm(source)
            override fun newArray(size: Int): Array<PokemonForm?> = arrayOfNulls(size)
        }
    }
}
