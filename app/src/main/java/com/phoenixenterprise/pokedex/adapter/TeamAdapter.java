package com.phoenixenterprise.pokedex.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.object.Pokemon;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.util.ArrayList;


public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.PokemonViewHolder> {

    public static class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView tvName, tvId, tvForm, tvType1, tvType2;
        ImageView imageView;

        PokemonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.teamCard_cv);
            tvName = (TextView) itemView.findViewById(R.id.teamCard_tvName);
            tvForm = (TextView) itemView.findViewById(R.id.teamCard_tvForm);
            tvId = (TextView) itemView.findViewById(R.id.teamCard_tvNationalId);
            tvType1 = (TextView) itemView.findViewById(R.id.teamCard_tvType1);
            tvType2 = (TextView) itemView.findViewById(R.id.teamCard_tvType2);
            imageView = (ImageView) itemView.findViewById(R.id.teamCard_image);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onRowClick(v, getPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<Pokemon> mArrayPokemon;

    public TeamAdapter(Context context, ArrayList<Pokemon> arrayPokemon) {
        mContext = context;
        mArrayPokemon = arrayPokemon;
    }

    @Override
    public int getItemCount() {
        return mArrayPokemon.size();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        Pokemon aPokemon = mArrayPokemon.get(position);

        holder.tvId.setText("# " + aPokemon.getNationalIdFormatted());
        holder.tvName.setText(mArrayPokemon.get(position).getPokemon());

        if (aPokemon.getFormFormatted().equals("")) {
            holder.tvForm.setVisibility(View.GONE);
        } else {
            holder.tvForm.setText(aPokemon.getFormFormatted());
        }

        holder.tvType1.setText(aPokemon.getType1());
        holder.tvType1.setBackgroundColor(mContext.getResources().getColor(
                InfoUtils.getTypeBkgdColorRes(aPokemon.getType1())));
        holder.tvType1.setTextColor(mContext.getResources().getColor(R.color.text_white));

        if (aPokemon.getType2().equals("")) {
            holder.tvType2.setVisibility(View.GONE);
        } else {
            holder.tvType2.setText(aPokemon.getType2());
            holder.tvType2.setBackgroundColor(mContext.getResources().getColor(
                    InfoUtils.getTypeBkgdColorRes(aPokemon.getType2())));
            holder.tvType2.setTextColor(mContext.getResources().getColor(R.color.text_white));
        }

        holder.imageView.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnEntryClickListener {
        void onRowClick(View view, int position);
    }

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}