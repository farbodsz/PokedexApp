package com.satsumasoftware.pokedex.framework.item;

public class Item extends BaseItem {

    private int mCategoryId, mCost, mFlingPower, mFlingEffectId, mItemPocketId;
    private String mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;

    public Item(int id, int categoryId, int cost, int flingPower, int flingEffectId,
                int itemPocketId, String name, String nameJa, String nameKo, String nameFr,
                String nameDe, String nameEs, String nameIt) {
        mId = id;
        mCategoryId = categoryId;
        mCost = cost;
        mFlingPower = flingPower;
        mFlingEffectId = flingEffectId;
        mItemPocketId = itemPocketId;

        mName = name;
        mNameJa = nameJa;
        mNameKo = nameKo;
        mNameFr = nameFr;
        mNameDe = nameDe;
        mNameEs = nameEs;
        mNameIt = nameIt;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public int getCost() {
        return mCost;
    }

    public int getFlingPower() {
        return mFlingPower;
    }

    public int getFlingEffectId() {
        return mFlingEffectId;
    }

    public int getItemPocketId() {
        return mItemPocketId;
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
