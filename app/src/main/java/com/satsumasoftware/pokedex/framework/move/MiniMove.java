package com.satsumasoftware.pokedex.framework.move;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.MovesDBHelper;

public class MiniMove extends BaseMove implements Parcelable {

    public MiniMove(Context context, int id) {
        mId = id;

        MovesDBHelper helper = MovesDBHelper.getInstance(context);
        Cursor cursor = helper.getReadableDatabase().query(
                MovesDBHelper.TABLE_NAME,
                new String[] {MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME},
                MovesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();

        mName = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME));
        cursor.close();
    }

    public MiniMove(int id, String name) {
        mId = id;
        mName = name;
    }

    public Move toMove(Context context) {
        MovesDBHelper helper = MovesDBHelper.getInstance(context);
        Cursor cursor = helper.getReadableDatabase().query(
                MovesDBHelper.TABLE_NAME,
                null,
                MovesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();
        Move move = new Move(cursor);
        cursor.close();
        return move;
    }


    protected MiniMove(Parcel in) {
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

    public static final Creator<MiniMove> CREATOR = new Creator<MiniMove>() {
        @Override
        public MiniMove createFromParcel(Parcel in) {
            return new MiniMove(in);
        }

        @Override
        public MiniMove[] newArray(int size) {
            return new MiniMove[size];
        }
    };

}
