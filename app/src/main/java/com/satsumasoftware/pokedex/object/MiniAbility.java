package com.satsumasoftware.pokedex.object;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.AbilityDBHelper;

public class MiniAbility implements Parcelable {

    private String mAbility;
    private int mAbilityId;

    public MiniAbility(int id, String ability) {
        mAbilityId = id;
        mAbility = ability;
    }

    public MiniAbility(Context context, String ability) {
        mAbilityId = getIdFromAbility(context, ability);
        mAbility = ability;
    }

    public Ability toAbility(Context context) {
        return new Ability(context, mAbility);
    }

    public int getAbilityId() {
        return mAbilityId;
    }

    public String getAbility() {
        return mAbility;
    }

    public static int getIdFromAbility(Context context, String ability) {
        int id = 0;
        AbilityDBHelper helper = new AbilityDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                AbilityDBHelper.TABLE_NAME,
                new String[] {AbilityDBHelper.COL_ID, AbilityDBHelper.COL_NAME},
                AbilityDBHelper.COL_NAME + "=?",
                new String[] {ability},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex(AbilityDBHelper.COL_ID));
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }


    protected MiniAbility(Parcel in) {
        mAbilityId = in.readInt();
        mAbility = in.readString();
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
        dest.writeInt(mAbilityId);
        dest.writeString(mAbility);
    }

}
