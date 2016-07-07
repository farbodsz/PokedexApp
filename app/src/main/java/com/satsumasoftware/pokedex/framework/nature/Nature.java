package com.satsumasoftware.pokedex.framework.nature;

public class Nature extends BaseNature {

    private int mDecreasedStatId, mIncreasedStatId, mHatesFlavorId, mLikesFlavorId, mGameIndex;
    private String mIdentifier;

    private String mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;

    public Nature(int id, String identifier, int decreasedStatId, int increasedStatId,
                  int hatesFlavorId, int likesFlavorId, int gameIndex, String name, String nameJa,
                  String nameKo, String nameFr, String nameDe, String nameEs, String nameIt) {
        mId = id;
        mIdentifier = identifier;
        mDecreasedStatId = decreasedStatId;
        mIncreasedStatId = increasedStatId;
        mHatesFlavorId = hatesFlavorId;
        mLikesFlavorId = likesFlavorId;
        mGameIndex = gameIndex;

        mName = name;
        mNameJa = nameJa;
        mNameKo = nameKo;
        mNameFr = nameFr;
        mNameDe = nameDe;
        mNameEs = nameEs;
        mNameIt = nameIt;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public int getDecreasedStatId() {
        return mDecreasedStatId;
    }

    public int getIncreasedStatId() {
        return mIncreasedStatId;
    }

    public int getHatesFlavorId() {
        return mHatesFlavorId;
    }

    public int getLikesFlavorId() {
        return mLikesFlavorId;
    }

    public int getGameIndex() {
        return mGameIndex;
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

}
