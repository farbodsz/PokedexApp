package com.satsumasoftware.pokedex.entities.pokemon;

import android.content.Context;
import android.database.Cursor;

import com.satsumasoftware.pokedex.db.PokemonMovesDBHelper;
import com.satsumasoftware.pokedex.entities.move.MiniMove;

import java.util.ArrayList;

public class PokemonMoves {

    private Context mContext;
    private int mPokemonId, mVersionGroupId, mPokemonMoveMethodId;
    private ArrayList<Integer> mMoveIds, mLevels, mOrders;

    private ArrayList<PokemonMove> mPokemonMoves;


    public PokemonMoves(Context context, MiniPokemon pokemon, int moveMethodId) {
        this(context, pokemon.getId(), moveMethodId);
    }

    public PokemonMoves(Context context, Pokemon pokemon, int moveMethodId) {
        this(context, pokemon.getId(), moveMethodId);
    }

    public PokemonMoves(Context context, int pokemonId, int moveMethodId) {
        this(context, pokemonId, moveMethodId, 16);  // TODO replace with latest version / default from a settings option
    }

    public PokemonMoves(Context context, int pokemonId, int moveMethodId, int versionGroupId) {
        mContext = context;
        mPokemonId = pokemonId;
        mPokemonMoveMethodId = moveMethodId;
        mVersionGroupId = versionGroupId;
        findValues();
    }

    private void findValues() {
        ArrayList<Integer> moveIds = new ArrayList<>();
        ArrayList<Integer> levels = new ArrayList<>();
        ArrayList<Integer> orders = new ArrayList<>();
        ArrayList<PokemonMove> pokemonMoves = new ArrayList<>();

        PokemonMovesDBHelper helper = new PokemonMovesDBHelper(mContext);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonMovesDBHelper.TABLE_NAME,
                null,
                PokemonMovesDBHelper.COL_POKEMON_ID + "=? AND " +
                        PokemonMovesDBHelper.COL_POKEMON_MOVE_METHOD_ID + "=? AND " +
                        PokemonMovesDBHelper.COL_VERSION_GROUP_ID + "=?",
                new String[] {String.valueOf(mPokemonId), String.valueOf(mPokemonMoveMethodId),
                        String.valueOf(mVersionGroupId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int moveId = cursor.getInt(cursor.getColumnIndex(PokemonMovesDBHelper.COL_MOVE_ID));
            int level = cursor.getInt(cursor.getColumnIndex(PokemonMovesDBHelper.COL_LEVEL));
            int order = cursor.getInt(cursor.getColumnIndex(PokemonMovesDBHelper.COL_ORDER));
            pokemonMoves.add(new PokemonMove(moveId, level, order));
            //moveIds.add(moveId);
            //levels.add(level);
            //orders.add(order);
            cursor.moveToNext();
        }
        cursor.close();

        mPokemonMoves = pokemonMoves;

        //mMoveIds = moveIds;
        //mLevels = levels;
        //mOrders = orders;
    }


    public int getPokemonId() {
        return mPokemonId;
    }

    public int getPokemonMoveMethodId() {
        return mPokemonMoveMethodId;
    }

    public int getVersionGroupId() {
        return mVersionGroupId;
    }

    /*
    public ArrayList<Integer> getMoveIds() {
        return mMoveIds;
    }

    public ArrayList<Integer> getLevels() {
        return mLevels;
    }

    public ArrayList<Integer> getOrderNumbers() {
        return mOrders;
    }
    */

    public ArrayList<PokemonMove> getPokemonMoves() {
        return mPokemonMoves;
    }


    public class PokemonMove {

        private int mMoveId, mLevel, mOrderNumber;

        public PokemonMove(int moveId, int level, int orderNumber) {
            mMoveId = moveId;
            mLevel = level;
            mOrderNumber = orderNumber;
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

}
