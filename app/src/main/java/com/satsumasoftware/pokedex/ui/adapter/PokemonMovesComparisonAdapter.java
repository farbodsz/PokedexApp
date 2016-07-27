package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonMove;
import com.satsumasoftware.pokedex.util.AppConfig;

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
    private int mLearnMethod;

    public PokemonMovesComparisonAdapter(Context context, List<Pair<Integer, PokemonMove[]>> pokemonMoves,
                                         int learnMethod) {
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

        holder.level.setText(mLearnMethod == AppConfig.LEARN_METHOD_LEVEL_UP ?
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
