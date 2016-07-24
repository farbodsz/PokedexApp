package com.satsumasoftware.pokedex.framework.pokemon;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.framework.move.MiniMove;

public class PokemonMove {

    private int mMoveId, mLevel, mOrderNumber;

    public PokemonMove(int moveId, int level, int orderNumber) {
        mMoveId = moveId;
        mLevel = level;
        mOrderNumber = orderNumber;
    }

    public PokemonMove(Cursor cursor) {
        mMoveId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_MOVE_ID));
        mLevel = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_LEVEL));
        mOrderNumber = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_ORDER));
    }

    public int getMoveId() {
        return mMoveId;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getOrderNumber() {
        return mOrderNumber;
    }

    public MiniMove toMiniMove(Context context) {
        return new MiniMove(context, mMoveId);
    }

    public boolean hasLearnLevel() {
        return mLevel != 0; // TODO FIXME is -1 the correct number
    }

}
