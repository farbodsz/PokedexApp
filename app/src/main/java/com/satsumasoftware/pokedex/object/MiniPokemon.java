package com.satsumasoftware.pokedex.object;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.FormIdDBHelper;
import com.satsumasoftware.pokedex.db.PokedexDBHelper;
import com.satsumasoftware.pokedex.util.InfoUtils;

public class MiniPokemon implements Parcelable {

    private String mPokemon, mForm;
    private int mNationalId;

    public MiniPokemon(int id, String pokemon, String form) {
        mNationalId = id;
        mPokemon = pokemon;
        mForm = form;
    }

    public MiniPokemon(Context context, int id, String form) {
        mNationalId = id;
        setPokemonAttrsFromId(context, id, form);
    }

    public void setPokemonAttrsFromId(Context context, int nationalId, String form) {
        if (nationalId >= 10000) {
            FormIdDBHelper dbHelper = new FormIdDBHelper(context);
            MiniPokemon miniPokemon = dbHelper.getPokemonFromLongId(nationalId);
            mNationalId = miniPokemon.getNationalId();
            mPokemon = miniPokemon.getPokemon();
            mForm = miniPokemon.getForm();
            return;
        }
        String name = null;
        PokedexDBHelper helper = new PokedexDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                PokedexDBHelper.TABLE_NAME,
                new String[]{PokedexDBHelper.COL_ID, PokedexDBHelper.COL_NAME, PokedexDBHelper.COL_FORM},
                PokedexDBHelper.COL_ID + "=? AND " + PokedexDBHelper.COL_FORM + "=?",
                new String[]{String.valueOf(nationalId), form},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_NAME));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        mPokemon = name;
        mForm = form;
    }

    public Pokemon toPokemon(Context context) {
        return new Pokemon(context, mNationalId, mPokemon, mForm);
    }

    public int getNationalId() {
        return mNationalId;
    }

    public String getNationalIdFormatted() {
        return InfoUtils.formatPokemonId(mNationalId);
    }

    public String getPokemon() {
        return mPokemon;
    }

    public String getForm() {
        return mForm;
    }

    public String getFormFormatted() {
        if (mForm.equalsIgnoreCase("Mega")) {
            return "Mega Evolution";
        } else if (mForm.equalsIgnoreCase("Mega X")) {
            return "Mega Evolution X";
        } else if (mForm.equalsIgnoreCase("Mega Y")) {
            return "Mega Evolution Y";
        } else if (mForm.equalsIgnoreCase("Primal")) {
            return "Primal " + mPokemon;
        } else {
            return mForm;
        }
    }

    public boolean isNormalForm() {
        return mForm.equals("");
    }

    public boolean isMegaForm() {
        return mForm.equalsIgnoreCase("Mega") || mForm.equals("Mega X") || mForm.equals("Mega Y");
    }


    public String toString() {
        return mNationalId + "_" + mPokemon + "_" + mForm;
    }

    public static MiniPokemon stringToPokemon(String string) {
        String[] info = string.split("_");
        if (info.length == 2) {
            // I.e. no form
            return new MiniPokemon(Integer.parseInt(info[0]), info[1], "");
        } else {
            return new MiniPokemon(Integer.parseInt(info[0]), info[1], info[2]);
        }
    }



    protected MiniPokemon(Parcel in) {
        mNationalId = in.readInt();
        mPokemon = in.readString();
        mForm = in.readString();
    }

    public static final Creator<MiniPokemon> CREATOR = new Creator<MiniPokemon>() {
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
        dest.writeInt(mNationalId);
        dest.writeString(mPokemon);
        dest.writeString(mForm);
    }
}