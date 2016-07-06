package com.satsumasoftware.pokedex.framework.pokemon;

import android.content.Context;

import com.satsumasoftware.pokedex.db.PokemonDBHelper;

public class BasePokemon {

    public static final String[] DB_COLUMNS = new String[] {
            PokemonDBHelper.COL_ID, PokemonDBHelper.COL_SPECIES_ID, PokemonDBHelper.COL_FORM_ID,
            PokemonDBHelper.COL_NAME, PokemonDBHelper.COL_FORM_NAME,
            PokemonDBHelper.COL_FORM_POKEMON_NAME, PokemonDBHelper.COL_IS_DEFAULT,
            PokemonDBHelper.COL_FORM_IS_DEFAULT, PokemonDBHelper.COL_FORM_IS_MEGA,
            PokemonDBHelper.COL_POKEDEX_NATIONAL};

    protected int mId, mSpeciesId, mFormId, mNationalNumber;
    protected String mName, mFormName, mFormPokemonName;

    public Pokemon toPokemon(Context context) {
        return new Pokemon(context, mId, mSpeciesId, mFormId, mName, mFormName, mFormPokemonName,
                mNationalNumber);
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
}
