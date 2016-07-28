package com.satsumasoftware.pokedex.framework.item;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.ItemsDBHelper;

public class MiniItem extends BaseItem implements Parcelable {

    public MiniItem(int id, String name) {
        mId = id;
        mName = name;
    }

    public MiniItem(Context context, int id) {
        mId = id;

        ItemsDBHelper helper = new ItemsDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                ItemsDBHelper.TABLE_NAME,
                BaseItem.DB_COLUMNS,
                ItemsDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();
        mName = cursor.getString(cursor.getColumnIndex(ItemsDBHelper.COL_NAME));
        cursor.close();
    }


    protected MiniItem(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    public static final Creator<MiniItem> CREATOR = new Creator<MiniItem>() {
        @Override
        public MiniItem createFromParcel(Parcel in) {
            return new MiniItem(in);
        }

        @Override
        public MiniItem[] newArray(int size) {
            return new MiniItem[size];
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
