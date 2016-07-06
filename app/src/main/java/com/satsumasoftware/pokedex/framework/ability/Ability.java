package com.satsumasoftware.pokedex.framework.ability;

import android.os.Parcel;
import android.os.Parcelable;

public class Ability implements Parcelable {

    private int mId, mGenerationId;

    private String mName, mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;


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

    public int getId() {
        return mId;
    }

    public int getGenerationId() {
        return mGenerationId;
    }

    public String getName() {
        return mName;
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
