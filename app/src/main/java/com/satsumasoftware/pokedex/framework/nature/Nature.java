package com.satsumasoftware.pokedex.framework.nature;

import android.database.Cursor;

import com.satsumasoftware.pokedex.db.NaturesDBHelper;

public class Nature extends BaseNature {

    private int mDecreasedStatId, mIncreasedStatId, mHatesFlavorId, mLikesFlavorId;
    private String mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;

    public Nature(int id, int decreasedStatId, int increasedStatId, int hatesFlavorId,
                  int likesFlavorId, String name, String nameJa, String nameKo, String nameFr,
                  String nameDe, String nameEs, String nameIt) {
        mId = id;
        mDecreasedStatId = decreasedStatId;
        mIncreasedStatId = increasedStatId;
        mHatesFlavorId = hatesFlavorId;
        mLikesFlavorId = likesFlavorId;

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
        mDecreasedStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_DECREASED_STAT_ID));
        mIncreasedStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_INCREASED_STAT_ID));
        mHatesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_HATES_FLAVOR_ID));
        mLikesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_LIKES_FLAVOR_ID));

        mName = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME));
        mNameJa = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_JAPANESE));
        mNameKo = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_KOREAN));
        mNameFr = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_FRENCH));
        mNameDe = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_GERMAN));
        mNameEs = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_SPANISH));
        mNameIt = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_ITALIAN));
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
