package com.satsumasoftware.pokedex.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.move.MiniMove;
import com.satsumasoftware.pokedex.entities.pokemon.PokemonMoves;

import java.util.ArrayList;

public class PokemonMovesVgAdapter {

    private ViewGroup mViewGroup;
    private ArrayList<PokemonMoves.PokemonMove> mPokemonMoves;
    private Activity mActivity;

    public PokemonMovesVgAdapter(Activity activity, ViewGroup viewGroup, ArrayList<PokemonMoves.PokemonMove> arrayEntries) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mPokemonMoves = arrayEntries;
    }

    public void createListItems() {
        View[] itemViews = new View[mPokemonMoves.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mPokemonMoves.size(); i++) {
            itemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(itemViews[i]);
            if (i != mPokemonMoves.size()-1) {
                mViewGroup.addView(mActivity.getLayoutInflater().inflate(R.layout.divider, mViewGroup, false));
            }
        }
    }

    private View makeListItem(final int position, ViewGroup container) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_move, container, false);
        PokemonMoves.PokemonMove pokemonMove = mPokemonMoves.get(position);

        TextView level = (TextView) view.findViewById(R.id.text1);
        level.setText(pokemonMove.hasLearnLevel() ?
                String.valueOf(pokemonMove.getLevel()) : "-");

        MiniMove move = pokemonMove.toMiniMove(mActivity);

        TextView name = (TextView) view.findViewById(R.id.text2);
        name.setText(move.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEntryClickListener != null) {
                    mOnEntryClickListener.onEntryClick(v, position);
                }
            }
        });

        return view;
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}