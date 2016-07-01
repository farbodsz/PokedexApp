package com.phoenixenterprise.pokedex.object;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.phoenixenterprise.pokedex.db.MoveDBHelper;

public class MiniMove implements Parcelable {

    private String mMove;
    private int mMoveId;

    public MiniMove(int id, String move) {
        mMoveId = id;
        mMove = move;
    }

    public MiniMove(Context context, String name) {
        mMoveId = getIdFromMove(context, name);
        mMove = name;
    }

    public Move toMove(Context context) {
        return new Move(context, mMoveId);
    }

    public int getMoveId() {
        return mMoveId;
    }

    public String getMove() {
        return mMove;
    }

    public static int getIdFromMove(Context context, String name) {
        int id = 0;
        MoveDBHelper helper = new MoveDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                MoveDBHelper.TABLE_NAME,
                new String[] {MoveDBHelper.COL_ID, MoveDBHelper.COL_NAME},
                MoveDBHelper.COL_NAME + "=?",
                new String[] {name},
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex(MoveDBHelper.COL_ID));
            cursor.moveToNext();
        }
        cursor.close();
        return id;
    }


    protected MiniMove(Parcel in) {
        mMoveId = in.readInt();
        mMove = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMoveId);
        dest.writeString(mMove);
    }
}
