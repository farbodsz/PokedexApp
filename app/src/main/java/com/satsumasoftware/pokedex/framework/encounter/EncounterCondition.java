/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.framework.encounter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;

import com.satsumasoftware.pokedex.db.EncounterConditionsDBHelper;
import com.satsumasoftware.pokedex.framework.Langs;

public class EncounterCondition {

    private int mValueId, mConditionId;
    private boolean mIsDefault;

    private ArrayMap<String, String> mValueNames, mConditionNames;

    public EncounterCondition(Context context, int encounterConditionValueId) {
        EncounterConditionsDBHelper helper = EncounterConditionsDBHelper.getInstance(context);
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
