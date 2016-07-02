package com.satsumasoftware.pokedex.entities.nature;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.NaturesDBHelper;

public class MiniNature implements Parcelable {

    public static final String[] DB_COLUMNS =
            {NaturesDBHelper.COL_ID, NaturesDBHelper.COL_NAME};

    private int mId;
    private String mName;

    public MiniNature(int id, String name) {
        mId = id;
        mName = name;
    }

    protected MiniNature(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MiniNature> CREATOR = new Creator<MiniNature>() {
        @Override
        public MiniNature createFromParcel(Parcel in) {
            return new MiniNature(in);
        }

        @Override
        public MiniNature[] newArray(int size) {
            return new MiniNature[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Nature toNature(Context context) {
        NaturesDBHelper helper = new NaturesDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                NaturesDBHelper.TABLE_NAME,
                null,
                NaturesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        String identifier = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_IDENTIFIER));
        int decrStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_DECREASED_STAT_ID));
        int incrStatId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_INCREASED_STAT_ID));
        int hatesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_HATES_FLAVOR_ID));
        int likesFlavorId = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_LIKES_FLAVOR_ID));
        int gameIndex = cursor.getInt(cursor.getColumnIndex(NaturesDBHelper.COL_GAME_INDEX));
        String name = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME));
        String nameJa = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_JAPANESE));
        String nameKo = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_KOREAN));
        String nameFr = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_FRENCH));
        String nameDe = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_GERMAN));
        String nameEs = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_SPANISH));
        String nameIt = cursor.getString(cursor.getColumnIndex(NaturesDBHelper.COL_NAME_ITALIAN));
        cursor.close();
        return new Nature(mId, identifier, decrStatId, incrStatId, hatesFlavorId,
                likesFlavorId, gameIndex, name, nameJa, nameKo, nameFr, nameDe, nameEs, nameIt);
    }

}
