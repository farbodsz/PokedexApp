/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.MoveMethod;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonMove;

import java.util.List;

public class PokemonMovesComparisonAdapter extends RecyclerView.Adapter<PokemonMovesComparisonAdapter.PokemonMoveViewHolder> {

    public class PokemonMoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView level, name1, name2;

        PokemonMoveViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            level = (TextView) itemView.findViewById(R.id.text1);
            name1 = (TextView) itemView.findViewById(R.id.text2);
            name2 = (TextView) itemView.findViewById(R.id.text3);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private List<Pair<Integer, PokemonMove[]>> mPokemonMoves;
    private MoveMethod mLearnMethod;

    public PokemonMovesComparisonAdapter(Context context, List<Pair<Integer, PokemonMove[]>> pokemonMoves,
                                         MoveMethod learnMethod) {
        mContext = context;
        mPokemonMoves = pokemonMoves;
        mLearnMethod = learnMethod;
    }

    @Override
    public int getItemCount() {
        return mPokemonMoves.size();
    }

    @Override
    public PokemonMoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_move_comparison, parent, false);
        return new PokemonMoveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonMoveViewHolder holder, int position) {
        Pair pair = mPokemonMoves.get(position);
        int level = (int) pair.first;
        PokemonMove[] moves = (PokemonMove[]) pair.second;

        holder.level.setText(mLearnMethod.isLevelUpMethod() ?
                String.valueOf(level) : "-");

        String text1 = "";
        String text2 = "";

        if (moves[0] != null) {
            text1 = moves[0].toMiniMove(mContext).getName();
        }
        if (moves[1] != null) {
            text2 = moves[1].toMiniMove(mContext).getName();
        }

        holder.name1.setText(text1);
        holder.name2.setText(text2);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}
