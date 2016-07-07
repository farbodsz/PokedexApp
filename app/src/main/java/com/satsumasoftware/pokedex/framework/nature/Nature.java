package com.satsumasoftware.pokedex.framework.nature;

import android.database.Cursor;

import com.satsumasoftware.pokedex.db.NaturesDBHelper;

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

    public Nature(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_ID));
        mIdentifier = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_IDENTIFIER));
        mDecreasedStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_DECREASED_STAT_ID));
        mIncreasedStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_INCREASED_STAT_ID));
        mHatesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_HATES_FLAVOR_ID));
        mLikesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_LIKES_FLAVOR_ID));
        mGameIndex = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_GAME_INDEX));

        mName = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME));
        mNameJa = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_JAPANESE));
        mNameKo = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_KOREAN));
        mNameFr = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_FRENCH));
        mNameDe = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_GERMAN));
        mNameEs = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_SPANISH));
        mNameIt = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_ITALIAN));
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
