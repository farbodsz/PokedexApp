package com.satsumasoftware.pokedex.framework.move

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.satsumasoftware.pokedex.db.MovesDBHelper

class MiniMove(val id: Int, val name: String) : Parcelable {

    constructor(source: Parcel) : this(source.readInt(), source.readString())

    fun toMove(context: Context): Move {
        val helper = MovesDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                MovesDBHelper.TABLE_NAME,
                null,
                "${MovesDBHelper.COL_ID}=?",
                arrayOf(id.toString()),
                null, null, null)
        cursor.moveToFirst()
        val move = Move(cursor)
        cursor.close()
        return move
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MiniMove> = object : Parcelable.Creator<MiniMove> {
            override fun createFromParcel(source: Parcel): MiniMove = MiniMove(source)
            override fun newArray(size: Int): Array<MiniMove?> = arrayOfNulls(size)
        }

        @JvmField val DB_COLUMNS = arrayOf(MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME)

        @JvmStatic
        fun create(context: Context, id: Int): MiniMove {
            val helper = MovesDBHelper.getInstance(context)
            val cursor = helper.readableDatabase.query(
                    MovesDBHelper.TABLE_NAME,
                    arrayOf(MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME),
                    MovesDBHelper.COL_ID + "=?",
                    arrayOf<String>(id.toString()),
                    null, null, null)
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME))
            cursor.close()
            return MiniMove(id, name)
        }
    }

}
