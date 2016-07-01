package com.satsumasoftware.pokedex.object;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.satsumasoftware.pokedex.db.LearnsetDBHelper;

import java.util.ArrayList;

public class Learnset {

    private Context mContext;
    private int mPkmnId, mVersionId, mLearnMethod;
    private String mPkmnForm;
    private ArrayList<String> mArrayLevels = new ArrayList<>();
    private ArrayList<String> mArrayMoveIds = new ArrayList<>();

    public Learnset(Context context, MiniPokemon pokemon, int versionId, int learnMethod) {
        mContext = context;
        mPkmnId = pokemon.getNationalId();
        mPkmnForm = pokemon.getForm();
        mVersionId = versionId;
        mLearnMethod = learnMethod;
        loadInformation();
    }

    private void loadInformation() {
        mArrayLevels.clear();
        mArrayMoveIds.clear();

        if (mPkmnForm == null
                || mPkmnForm.equals("Mega")
                || mPkmnForm.equals("Mega X")
                || mPkmnForm.equals("Mega Y")
                || mPkmnForm.equals("Primal")) {
            mPkmnForm = "";
        }

        LearnsetDBHelper helper = new LearnsetDBHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = LearnsetDBHelper.COL_PKMN_ID + "=? AND " + LearnsetDBHelper.COL_PKMN_FORM + "=? AND " +
                LearnsetDBHelper.COL_VERSION_ID + "=? AND " + LearnsetDBHelper.COL_LEARN_METHOD + "=?";
        String[] selectionArgs = new String[] {String.valueOf(mPkmnId), mPkmnForm,
                String.valueOf(mVersionId), String.valueOf(mLearnMethod)};

        Cursor cursor = db.query(
                LearnsetDBHelper.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String aLevel = String.valueOf(cursor.getInt(cursor.getColumnIndex(LearnsetDBHelper.COL_LEARN_LEVEL)));
            String aMoveId = String.valueOf(cursor.getInt(cursor.getColumnIndex(LearnsetDBHelper.COL_MOVE_ID)));
            mArrayLevels.add(aLevel);
            mArrayMoveIds.add(aMoveId);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<String> getLevels() {
        return mArrayLevels;
    }

    public ArrayList<String> getMoveIds() {
        return mArrayMoveIds;
    }

    public ArrayList<String> getMoveNames() {
        ArrayList<String> moveNames = new ArrayList<>();
        moveNames.clear();
        for (int i = 0; i < mArrayMoveIds.size(); i++) {
            int moveId = Integer.parseInt(mArrayMoveIds.get(i));
            String move = new Move(mContext, moveId).getMove();
            moveNames.add(move);
        }
        return moveNames;
    }
}
