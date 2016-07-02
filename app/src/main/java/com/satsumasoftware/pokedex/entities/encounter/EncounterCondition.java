package com.satsumasoftware.pokedex.entities.encounter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;

import com.satsumasoftware.pokedex.db.EncounterConditionsDBHelper;
import com.satsumasoftware.pokedex.entities.Langs;

public class EncounterCondition {

    private int mValueId, mConditionId;
    private boolean mIsDefault;

    private ArrayMap<String, String> mValueNames, mConditionNames;

    public EncounterCondition(Context context, int encounterConditionValueId) {
        EncounterConditionsDBHelper helper = new EncounterConditionsDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                EncounterConditionsDBHelper.TABLE_NAME,
                null,
                EncounterConditionsDBHelper.COL_VALUE_ID + "=?",
                new String[] {String.valueOf(encounterConditionValueId)},
                null, null, null);
        cursor.moveToFirst();

        mValueId = encounterConditionValueId;
        mConditionId = cursor.getInt(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_CONDITION_ID));
        int isDefaultAsInt = cursor.getInt(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_IS_DEFAULT_VALUE));
        mIsDefault = (isDefaultAsInt == 1);
        String valueEn = cursor.getString(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_VALUE_NAME));
        String valueDe = cursor.getString(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_VALUE_NAME_DE));
        String conditionEn = cursor.getString(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_CONDITION_NAME));
        String conditionFr = cursor.getString(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_CONDITION_NAME_FR));
        String conditionDe = cursor.getString(cursor.getColumnIndex(EncounterConditionsDBHelper.COL_CONDITION_NAME_DE));
        cursor.close();

        mValueNames = new ArrayMap<>(2);
        mValueNames.put(Langs.KEY_EN, valueEn);
        mValueNames.put(Langs.KEY_DE, valueDe);

        mConditionNames = new ArrayMap<>(3);
        mConditionNames.put(Langs.KEY_EN, conditionEn);
        mConditionNames.put(Langs.KEY_FR, conditionFr);
        mConditionNames.put(Langs.KEY_DE, conditionDe);
    }

    public int getValueId() {
        return mValueId;
    }

    public int getConditionId() {
        return mConditionId;
    }

    public boolean valueIsDefault() {
        return mIsDefault;
    }

    public ArrayMap<String, String> getValueNames() {
        return mValueNames;
    }

    public ArrayMap<String, String> getConditionNames() {
        return mConditionNames;
    }

}
