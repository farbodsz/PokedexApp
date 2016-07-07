package com.satsumasoftware.pokedex.framework.nature;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.NaturesDBHelper;

public class MiniNature extends BaseNature implements Parcelable {

    public MiniNature(int id, String name) {
        mId = id;
        mName = name;
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
        Nature nature = new Nature(cursor);
        cursor.close();
        return nature;
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
}
