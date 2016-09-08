package com.satsumasoftware.pokedex.framework.pokemon

import android.content.Context
import android.database.Cursor
import java.util.*
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.satsumasoftware.pokedex.db.PokemonDBHelper
import com.satsumasoftware.pokedex.util.ActionUtils

class MiniPokemon(id: Int, speciesId: Int, formId: Int, name: String, formName: String,
                  formAndPokemonName: String?, nationalNumber: Int) :
        BasePokemon(id, speciesId, formId, name, formName, formAndPokemonName, nationalNumber),
        Parcelable {

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ID)),
            cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_ID)),
            cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID)),
            cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME)),
            cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME)),
            cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME)),
            cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL)))

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt())

    fun toPokemon(context: Context) = Pokemon(context,
            id,
            speciesId,
            formId,
            name,
            formName,
            formAndPokemonName,
            nationalDexNumber)

    fun setPokemonImage(imageView: ImageView) = ActionUtils.setPokemonImage(this, imageView)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeInt(speciesId)
        dest?.writeInt(formId)
        dest?.writeString(name)
        dest?.writeString(formName)
        dest?.writeString(formAndPokemonName)
        dest?.writeInt(nationalDexNumber)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MiniPokemon> = object : Parcelable.Creator<MiniPokemon> {
            override fun createFromParcel(source: Parcel): MiniPokemon = MiniPokemon(source)
            override fun newArray(size: Int): Array<MiniPokemon?> = arrayOfNulls(size)
        }

        @JvmStatic
        fun create(context: Context, id: Int): MiniPokemon {
            val helper = PokemonDBHelper.getInstance(context)
            val cursor = helper.readableDatabase.query(
                    PokemonDBHelper.TABLE_NAME,
                    BasePokemon.DB_COLUMNS,
                    "${PokemonDBHelper.COL_ID}=? AND ${PokemonDBHelper.COL_FORM_IS_DEFAULT}=?",
                    arrayOf(id.toString(), 1.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val miniPokemon = MiniPokemon(cursor)
            cursor.close()
            return miniPokemon
        }

        @JvmStatic
        fun createFromSpecies(context: Context, speciesId: Int, isFormMega: Boolean): MiniPokemon {
            val isMegaAsInt = if (isFormMega) 1 else 0
            val pokemonDBHelper = PokemonDBHelper.getInstance(context)
            val cursor = pokemonDBHelper.readableDatabase.query(
                    PokemonDBHelper.TABLE_NAME,
                    BasePokemon.DB_COLUMNS,
                    "${PokemonDBHelper.COL_SPECIES_ID}=? AND ${PokemonDBHelper.COL_FORM_IS_MEGA}=?",
                    arrayOf(speciesId.toString(), isMegaAsInt.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val miniPokemon = MiniPokemon(cursor)
            cursor.close()
            return miniPokemon
        }
    }

}
