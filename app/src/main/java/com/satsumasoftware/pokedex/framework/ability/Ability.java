package com.satsumasoftware.pokedex.framework.ability;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.PokeDB;

public class Ability extends BaseAbility implements Parcelable {

    private int mGenerationId;
    private String mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;


    public Ability(int id, int generationId, String name, String nameJa, String nameKo,
                   String nameFr, String nameDe, String nameEs, String nameIt) {
        mId = id;
        mGenerationId = generationId;

        mName = name;
        mNameJa = nameJa;
        mNameKo = nameKo;
        mNameFr = nameFr;
        mNameDe = nameDe;
        mNameEs = nameEs;
        mNameIt = nameIt;
    }

    public int getGenerationId() {
        return mGenerationId;
    }

    public String getNameJapanese() {
        return mNameJa;
    }

    public String getNameKorean() {
        return mNameKo;
    }

    public String getNameFrench() {
        return mNameFr;
    }

    public String getNameGerman() {
        return mNameDe;
    }

    public String getNameSpanish() {
        return mNameEs;
    }

    public String getNameItalian() {
        return mNameIt;
    }


    public String getFlavorText(Context context) {
        return getFlavorText(context, 16);  // latest version group
        // TODO call above with a default version (Settings option)
    }

    public String getFlavorText(Context context, int versionGroupId) {
        return getFlavorText(context, versionGroupId, 9);  // English language
        // TODO call above with a default language (Settings option)
    }

    public String getFlavorText(Context context, int versionGroupId, int langId) {
        PokeDB pokeDB = new PokeDB(context);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.AbilityFlavorText.TABLE_NAME,
                null,
                PokeDB.AbilityFlavorText.COL_ABILITY_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_VERSION_GROUP_ID + "=? AND " +
                        PokeDB.AbilityFlavorText.COL_LANGUAGE_ID + "=?",
                new String[] {String.valueOf(mId), String.valueOf(versionGroupId),
                        String.valueOf(langId)},
                null, null, null);
        cursor.moveToFirst();
        String flavorText = cursor.getString(
                cursor.getColumnIndex(PokeDB.AbilityFlavorText.COL_FLAVOR_TEXT));
        cursor.close();
        return flavorText.replace("\n", " ");  // to remove the line breaks
    }


    protected Ability(Parcel in) {
        mId = in.readInt();
        mGenerationId = in.readInt();

        mName = in.readString();
        mNameJa = in.readString();
        mNameKo = in.readString();
        mNameFr = in.readString();
        mNameDe = in.readString();
        mNameEs = in.readString();
        mNameIt = in.readString();
    }

    public static final Parcelable.Creator<Ability> CREATOR = new Parcelable.Creator<Ability>() {
        @Override
        public Ability createFromParcel(Parcel in) {
            return new Ability(in);
        }

        @Override
        public Ability[] newArray(int size) {
            return new Ability[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mGenerationId);

        dest.writeString(mName);
        dest.writeString(mNameJa);
        dest.writeString(mNameKo);
        dest.writeString(mNameFr);
        dest.writeString(mNameDe);
        dest.writeString(mNameEs);
        dest.writeString(mNameIt);
    }

}
