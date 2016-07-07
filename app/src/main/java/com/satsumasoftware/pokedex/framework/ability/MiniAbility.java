package com.satsumasoftware.pokedex.framework.ability;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;

import java.util.ArrayList;
import java.util.Collections;

public class MiniAbility extends BaseAbility implements Parcelable {

    public MiniAbility(int id, String name) {
        mId = id;
        mName = name;
    }

    public Ability toAbility(Context context) {
        AbilitiesDBHelper helper = new AbilitiesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                AbilitiesDBHelper.TABLE_NAME,
                null,
                AbilitiesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();
        int generationId = cursor.getInt(cursor.getColumnIndex(AbilitiesDBHelper.COL_GENERATION_ID));
        String nameJa = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_JAPANESE));
        String nameKo = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_KOREAN));
        String nameFr = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_FRENCH));
        String nameDe = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_GERMAN));
        String nameEs = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_SPANISH));
        String nameIt = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME_ITALIAN));
        cursor.close();
        return new Ability(mId, generationId, mName, nameJa, nameKo, nameFr, nameDe, nameEs, nameIt);
    }

    public static SparseArray<String> findAbilityNames(Context context, SparseIntArray abilityIds) {
        StringBuilder selectionBuilder = new StringBuilder();
        ArrayList<String> selectionArgsList = new ArrayList<>();
        ArrayList<Integer> sortedNotNullIds = new ArrayList<>();
        boolean[] abilityIsNull = new boolean[] {false, false, false};
        for (int i = 0; i < 3; i++) {
            if (abilityIds.get(i+1) == 0) {
                abilityIsNull[i] = true;
            } else {
                selectionBuilder.append(AbilitiesDBHelper.COL_ID + "=? OR ");
                selectionArgsList.add(String.valueOf(abilityIds.get(i+1)));
                sortedNotNullIds.add(abilityIds.get(i+1));
            }
        }
        Collections.sort(sortedNotNullIds);
        String selection = selectionBuilder.toString();
        selection = selection.substring(0, selection.length()-4);
        String[] selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);

        SparseArray<String> collectedData = new SparseArray<>();
        SparseArray<String> abilityNames = new SparseArray<>(3);

        AbilitiesDBHelper helper = new AbilitiesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                AbilitiesDBHelper.TABLE_NAME,
                new String[] {AbilitiesDBHelper.COL_ID, AbilitiesDBHelper.COL_NAME},
                selection,
                selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        int i = 0;
        int numAppended = 0;
        while (!cursor.isAfterLast() && i < 3) {
            // Note that the contents of this list would be sorted by id number (sadly)

            if (abilityIsNull[i]) {
                i++;
                cursor.moveToNext();
                continue;
            }

            String name = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME));
            collectedData.append(sortedNotNullIds.get(numAppended), name);
            i++;
            numAppended++;
        }
        cursor.close();

        for (int j = 0; j < 3; j++) {
            if (abilityIsNull[j]) {
                abilityNames.put(j, null);
            } else {
                abilityNames.put(j, collectedData.get(abilityIds.get(j+1)));
            }
        }

        return abilityNames;
    }

    protected MiniAbility(Parcel in) {
        mId = in.readInt();
        mName = in.readString(); // TODO what if these are null? (see comment in getName())
    }

    public static final Creator<MiniAbility> CREATOR = new Creator<MiniAbility>() {
        @Override
        public MiniAbility createFromParcel(Parcel in) {
            return new MiniAbility(in);
        }

        @Override
        public MiniAbility[] newArray(int size) {
            return new MiniAbility[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName); // TODO what if these are null? (see comment in getName())
    }

}
