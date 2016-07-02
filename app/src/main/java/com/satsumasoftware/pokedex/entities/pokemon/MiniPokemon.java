package com.satsumasoftware.pokedex.entities.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.PokemonDBHelper;

public class MiniPokemon extends BasePokemon implements Parcelable {

    public static final String[] DB_COLUMNS = new String[] {
            PokemonDBHelper.COL_ID, PokemonDBHelper.COL_SPECIES_ID, PokemonDBHelper.COL_FORM_ID,
            PokemonDBHelper.COL_NAME, PokemonDBHelper.COL_FORM_NAME,
            PokemonDBHelper.COL_FORM_POKEMON_NAME, PokemonDBHelper.COL_IS_DEFAULT,
            PokemonDBHelper.COL_FORM_IS_DEFAULT, PokemonDBHelper.COL_FORM_IS_MEGA,
            PokemonDBHelper.COL_POKEDEX_NATIONAL};

    private int mId, mSpeciesId, mFormId, mNationalNumber;
    private String mName, mFormName, mFormPokemonName;

    private int mAltDexId = -1, mAltDexNumber = -1;


    public MiniPokemon(Context context, int id) {
        PokemonDBHelper helper = new PokemonDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                DB_COLUMNS,
                PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=?",
                new String[] {String.valueOf(id), String.valueOf(1)},
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
            int id = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ID));
            int formId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID));
            String name = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME));
            String formName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME));
            String formAndPokemonName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
            int nationalNumber = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
            miniPokemon = new MiniPokemon(id, speciesId, formId, name, formName,
                    formAndPokemonName, nationalNumber);
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

    public int getId() {
        return mId;
    }

    public int getSpeciesId() {
        return mSpeciesId;
    }

    public int getFormId() {
        return mFormId;
    }

    public String getName() {
        return mName;
    }

    public String getFormName() {
        return mFormName;
    }

    public String getFormAndPokemonName() {
        // mFormPokemonName could be "" (if it is the default form)
        return (mFormPokemonName == null) ? mName : mFormPokemonName;
    }

    public int getNationalDexNumber() {
        return mNationalNumber;
    }

    public boolean hasAddedAltDexInfo() {
        return mAltDexNumber != -1;
    }

    public int getAlternateDexId() {
        return mAltDexId;
    }

    public int getAlterateDexNumber() {
        return mAltDexNumber;
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
