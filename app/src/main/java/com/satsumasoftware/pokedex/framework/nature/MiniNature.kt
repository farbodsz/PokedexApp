package com.satsumasoftware.pokedex.framework.nature

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.satsumasoftware.pokedex.db.NaturesDBHelper

class MiniNature(val id: Int, val name: String) : Parcelable {

    fun toNature(context: Context): Nature {
        val helper = NaturesDBHelper.getInstance(context)
        val cursor = helper.readableDatabase.query(
                NaturesDBHelper.TABLE_NAME,
                null,
                NaturesDBHelper.COL_ID + "=?",
                arrayOf(id.toString()),
                null,
                null,
                null)
        cursor.moveToFirst()
        val nature = Nature(cursor)
        cursor.close()
        return nature
    }

    constructor(source: Parcel) : this(source.readInt(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MiniNature> = object : Parcelable.Creator<MiniNature> {
            override fun createFromParcel(source: Parcel): MiniNature = MiniNature(source)
            override fun newArray(size: Int): Array<MiniNature?> = arrayOfNulls(size)
        }

        @JvmField val DB_COLUMNS = arrayOf(NaturesDBHelper.COL_ID, NaturesDBHelper.COL_NAME)

    }
}
