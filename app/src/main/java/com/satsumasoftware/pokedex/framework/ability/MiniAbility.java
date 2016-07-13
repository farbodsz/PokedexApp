package com.satsumasoftware.pokedex.framework.ability;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;

public class MiniAbility extends BaseAbility implements Parcelable {

    public MiniAbility(int id, String name) {
        mId = id;
        mName = name;
    }

    public MiniAbility(Context context, int id) {
        mId = id;

        AbilitiesDBHelper helper = new AbilitiesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                AbilitiesDBHelper.TABLE_NAME,
                BaseAbility.DB_COLUMNS,
                AbilitiesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();
        mName = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME));
        cursor.close();
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
        Ability ability = new Ability(cursor);
        cursor.close();
        return ability;
    }


    @Override
    public String toString() {
        return mName;
    }


    protected MiniAbility(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
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
        dest.writeString(mName);
    }

}
