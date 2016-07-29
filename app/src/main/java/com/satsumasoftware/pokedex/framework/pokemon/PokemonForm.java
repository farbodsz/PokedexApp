package com.satsumasoftware.pokedex.framework.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.DataUtilsKt;

public class PokemonForm implements Parcelable {

    private int mId, mSpeciesId, mFormId, mNationalNumber, mPrimaryTypeId;
    private String mName, mFormName, mCombinedName;
    private boolean mIsDefault, mIsFormDefault, mIsFormMega;

    public PokemonForm(int id, int speciesId, int formId, String name, String formName,
                       String combinedName, int nationalDexNumber, int primaryTypeId,
                       boolean isDefault, boolean isFormDefault, boolean isFormMega) {
        mId = id;
        mSpeciesId = speciesId;
        mFormId = formId;
        mName = name;
        mFormName = formName;
        mCombinedName = combinedName;
        mNationalNumber = nationalDexNumber;
        mPrimaryTypeId = primaryTypeId;
        mIsDefault = isDefault;
        mIsFormDefault = isFormDefault;
        mIsFormMega = isFormMega;
    }

    protected PokemonForm(Parcel in) {
        mId = in.readInt();
        mSpeciesId = in.readInt();
        mFormId = in.readInt();
        mPrimaryTypeId = in.readInt();
        mNationalNumber = in.readInt();

        mName = in.readString();
        mFormName = in.readString();
        mCombinedName = in.readString();

        mIsDefault = in.readByte() != 0;
        mIsFormDefault = in.readByte() != 0;
        mIsFormMega = in.readByte() != 0;
    }

    public static final Creator<PokemonForm> CREATOR = new Creator<PokemonForm>() {
        @Override
        public PokemonForm createFromParcel(Parcel in) {
            return new PokemonForm(in);
        }

        @Override
        public PokemonForm[] newArray(int size) {
            return new PokemonForm[size];
        }
    };

    public MiniPokemon toMiniPokemon(Context context) {
        PokemonDBHelper helper = new PokemonDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                new String[] {PokemonDBHelper.COL_ID, PokemonDBHelper.COL_FORM_IS_DEFAULT},
                PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=?",
                new String[] {String.valueOf(mId), String.valueOf(1)},
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return new MiniPokemon(mId, mSpeciesId, mFormId, mName, mFormName, mCombinedName, mNationalNumber);
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

    public String getCombinedName() {
        return mCombinedName;
    }

    public int getNationalDexNumber() {
        return mNationalNumber;
    }

    public int getPrimaryTypeId() {
        return mPrimaryTypeId;
    }

    public boolean isDefault() {
        return mIsDefault;
    }

    public boolean isFormDefault() {
        return mIsFormDefault;
    }

    public boolean isFormMega() {
        return mIsFormMega;
    }

    public void setPokemonImage(ImageView imageView) {
        ActionUtils.setPokemonImage(mId,
                DataUtilsKt.formatPokemonId(mSpeciesId),
                mName,
                mIsFormMega,
                imageView);
    }

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
        dest.writeInt(mPrimaryTypeId);
        dest.writeString(mName);
        dest.writeString(mFormName);
        dest.writeString(mCombinedName);
        dest.writeByte((byte) (mIsDefault ? 1 : 0));
        dest.writeByte((byte) (mIsFormDefault ? 1 : 0));
        dest.writeByte((byte) (mIsFormMega ? 1 : 0));
    }
}
