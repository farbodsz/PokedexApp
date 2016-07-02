package com.satsumasoftware.pokedex.entities.move;

public class Move {

    // TODO optimise (like Pokemon class)

    // TODO FIXME descriptions need to be formatted

    private int mId, mGenerationId, mTypeId, mPower, mPp, mAccuracy, mPriority, mTargetId,
            mDamageClassId, mEffectId, mEffectChance, mContestTypeId, mContestEffectId,
            mSuperContestEffectId;

    private String mName, mNameJa, mNameKo, mNameFr, mNameDe, mNameEs, mNameIt;


    public Move(int id, int generationId, int typeId, int power, int pp, int accuracy, int priority,
                int targetId, int damageClassId, int effectId, int effectChance, int contestTypeId,
                int contestEffectId, int superContestEffectId, String name, String nameJa,
                String nameKo, String nameFr, String nameDe, String nameEs, String nameIt) {
        mId = id;
        mGenerationId = generationId;
        mTypeId = typeId;
        mPower = power;
        mPp = pp;
        mAccuracy = accuracy;
        mPriority = priority;
        mTargetId = targetId;
        mDamageClassId = damageClassId;
        mEffectId = effectId;
        mEffectChance = effectChance;
        mContestTypeId = contestTypeId;
        mContestEffectId = contestEffectId;
        mSuperContestEffectId = superContestEffectId;

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

    public int getTypeId() {
        return mTypeId;
    }

    public int getPower() {
        return mPower;
    }

    public int getPp() {
        return mPp;
    }

    public int getAccuracy() {
        return mAccuracy;
    }

    public int getPriority() {
        return mPriority;
    }

    public int getTargetId() {
        return mTargetId;
    }

    public int getDamageClassId() {
        return mDamageClassId;
    }

    public int getEffectId() {
        return mEffectId;
    }

    public int getEffectChance() {
        return mEffectChance;
    }

    public int getContestTypeId() {
        return mContestTypeId;
    }

    public int getContestEffectId() {
        return mContestEffectId;
    }

    public int getSuperContestEffectId() {
        return mSuperContestEffectId;
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

}
