package com.satsumasoftware.pokedex.framework.item;

import android.os.Parcel;
import android.os.Parcelable;

public class MiniItem extends BaseItem implements Parcelable {

    public MiniItem(int id, String name) {
        mId = id;
        mName = name;
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
