package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.move.MiniMove;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonMove;

import java.util.ArrayList;

public class PokemonMovesAdapter extends RecyclerView.Adapter<PokemonMovesAdapter.PokemonMoveViewHolder> {

    public class PokemonMoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView level, name;

        PokemonMoveViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            level = (TextView) itemView.findViewById(R.id.text1);
            name = (TextView) itemView.findViewById(R.id.text2);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<PokemonMove> mPokemonMoves;

    public PokemonMovesAdapter(Context context, ArrayList<PokemonMove> pokemonMoves) {
        mContext = context;
        mPokemonMoves = pokemonMoves;
    }

    @Override
    public int getItemCount() {
        return mPokemonMoves.size();
    }

    @Override
    public PokemonMoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_move, parent, false);
        return new PokemonMoveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonMoveViewHolder holder, int position) {
        PokemonMove pokemonMove = mPokemonMoves.get(position);

        holder.level.setText(pokemonMove.hasLearnLevel() ?
                String.valueOf(pokemonMove.getLevel()) : "-");

        MiniMove move = pokemonMove.toMiniMove(mContext);
        holder.name.setText(move.getName());
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
