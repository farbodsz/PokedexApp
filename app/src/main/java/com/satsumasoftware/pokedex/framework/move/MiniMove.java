package com.satsumasoftware.pokedex.framework.move;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.db.MovesDBHelper;

public class MiniMove implements Parcelable {

    public static final String[] DB_COLUMNS =
            {MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME};

    private int mId;
    private String mName;


    public MiniMove(Context context, int id) {
        mId = id;

        MovesDBHelper helper = new MovesDBHelper(context);
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

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }


    public Move toMove(Context context) {
        MovesDBHelper helper = new MovesDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                MovesDBHelper.TABLE_NAME,
                null,
                MovesDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mId)},
                null, null, null);
        cursor.moveToFirst();
        int generationId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_GENERATION_ID));
        int typeId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_TYPE_ID));
        int power = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_POWER));
        int pp = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_PP));
        int accuracy = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_ACCURACY));
        int priority = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_PRIORITY));
        int targetId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_TARGET_ID));
        int damageClassId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_DAMAGE_CLASS_ID));
        int effectId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_EFFECT_ID));
        int effectChance = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_EFFECT_CHANCE));
        int contestTypeId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_CONTEST_TYPE_ID));
        int contestEffectId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_CONTEST_EFFECT_ID));
        int superContestEffectId = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_SUPER_CONTEST_EFFECT_ID));
        String nameJa = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_JAPANESE));
        String nameKo = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_KOREAN));
        String nameFr = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_FRENCH));
        String nameDe = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_GERMAN));
        String nameEs = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_SPANISH));
        String nameIt = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME_ITALIAN));
        cursor.close();
        return new Move(mId, generationId, typeId, power, pp, accuracy, priority, targetId,
                damageClassId, effectId, effectChance, contestTypeId, contestEffectId,
                superContestEffectId, mName, nameJa, nameKo, nameFr, nameDe, nameEs, nameIt);
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
