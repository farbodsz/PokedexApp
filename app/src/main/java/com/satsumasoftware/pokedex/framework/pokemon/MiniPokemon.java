package com.satsumasoftware.pokedex.framework.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.DataUtils;

public class MiniPokemon extends BasePokemon implements Parcelable {

    private int mAltDexId = -1, mAltDexNumber = -1;


    public MiniPokemon(Context context, int id) {
        PokemonDBHelper helper = new PokemonDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                DB_COLUMNS,
                PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=?",
                new String[] {String.valueOf(id), "1"},
                null, null, null);
        cursor.moveToFirst();
        mId = id;
        mSpeciesId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_ID));
        mFormId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID));
        mName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME));
        mFormName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME));
        mFormPokemonName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
        mNationalNumber = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
        cursor.close();
    }

    public MiniPokemon(Cursor cursor) {  // TODO use this more often
        mId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ID));
        mSpeciesId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_ID));
        mFormId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID));
        mName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME));
        mFormName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME));
        mFormPokemonName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
        mNationalNumber = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
    }

    public MiniPokemon(int id, int speciesId, int formId, String name,
                       String formName, String formPokemonName, int nationalDexNumber) {
        mId = id;
        mSpeciesId = speciesId;
        mFormId = formId;
        mName = name;
        mFormName = formName;
        mFormPokemonName = formPokemonName;
        mNationalNumber = nationalDexNumber;
    }

    @Deprecated
    public static MiniPokemon createFromSpecies(Context context, int speciesId, boolean isFormMega) {
        MiniPokemon miniPokemon = null;
        int isMegaAsInt = (isFormMega ? 1 : 0);
        PokemonDBHelper pokemonDBHelper = new PokemonDBHelper(context);
        Cursor cursor = pokemonDBHelper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                DB_COLUMNS,
                PokemonDBHelper.COL_SPECIES_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_MEGA + "=?",
                new String[] {String.valueOf(speciesId), String.valueOf(isMegaAsInt)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            miniPokemon = new MiniPokemon(cursor);  // TODO did I mean to use moveToNext() here?
        }
        cursor.close();
        return miniPokemon;

    }

    public Pokemon toPokemon(Context context) {
        return new Pokemon(context, mId, mSpeciesId, mFormId, mName, mFormName, mFormPokemonName,
                mNationalNumber);
    }

    public void addAlternatePokedexInfo(int pokedexId, int pokedexNumber) {
        mAltDexId = pokedexId;
        mAltDexNumber = pokedexNumber;
    }

    public boolean hasAddedAltDexInfo() {
        return mAltDexNumber != DataUtils.NULL_INT;
    }

    public int getAlternateDexId() {
        return mAltDexId;
    }

    public int getAlterateDexNumber() {
        return mAltDexNumber;
    }


    public void setPokemonImage(ImageView imageView) {
        ActionUtils.setPokemonImage(this, imageView);
    }


    protected MiniPokemon(Parcel in) {
        mId = in.readInt();
        mSpeciesId = in.readInt();
        mFormId = in.readInt();
        mNationalNumber = in.readInt();
        mAltDexId = in.readInt();
        mAltDexNumber = in.readInt();

        mName = in.readString();
        mFormName = in.readString();
        mFormPokemonName = in.readString();
    }


    public static final Parcelable.Creator<MiniPokemon> CREATOR = new Parcelable.Creator<MiniPokemon>() {
        @Override
        public MiniPokemon createFromParcel(Parcel in) {
            return new MiniPokemon(in);
        }

        @Override
        public MiniPokemon[] newArray(int size) {
            return new MiniPokemon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSpeciesId);
        dest.writeInt(mFormId);
        dest.writeInt(mNationalNumber);
        dest.writeInt(mAltDexId);
        dest.writeInt(mAltDexNumber);

        dest.writeString(mName);
        dest.writeString(mFormName);
        dest.writeString(mFormPokemonName);
    }

}
