package com.satsumasoftware.pokedex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    public static class PokedexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvName, tvId, tvForm;

        PokedexViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvId = (TextView) itemView.findViewById(R.id.item_pokedex_text1);
            tvName = (TextView) itemView.findViewById(R.id.item_pokedex_text2);
            tvForm = (TextView) itemView.findViewById(R.id.item_pokedex_text3);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnEntryLongClickListener != null) {
                mOnEntryLongClickListener.onEntryLongClick(v, getPosition());
                return true;
            }
            return false;
        }
    }

    private Context mContext;
    private ArrayList<MiniPokemon> mArrayPokemon;

    public PokedexAdapter(Context context, ArrayList<MiniPokemon> arrayPokemon) {
        mContext = context;
        mArrayPokemon = arrayPokemon;
    }

    @Override
    public int getItemCount() {
        return mArrayPokemon.size();
    }

    @Override
    public PokedexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pokedex, parent, false);
        return new PokedexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokedexViewHolder holder, int position) {
        MiniPokemon thisPokemon = mArrayPokemon.get(position);

        holder.tvId.setText(thisPokemon.getNationalIdFormatted());

        if (PrefUtils.combinePokemonNameInPokedex(mContext)) {
            holder.tvName.setText(
                    InfoUtils.getNameAndForm(thisPokemon.getPokemon(), thisPokemon.getForm()));
            holder.tvForm.setText("");
        } else {
            holder.tvName.setText(thisPokemon.getPokemon());
            String form = thisPokemon.getFormFormatted();
            if (form == null || form.equals("")) {
                holder.tvForm.setText("");
            } else {
                holder.tvForm.setText("(" + form + ")");
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private static OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }


    private static OnEntryLongClickListener mOnEntryLongClickListener;

    public interface OnEntryLongClickListener {
        void onEntryLongClick(View view, int position);
    }

    public void setOnEntryLongClickListener(OnEntryLongClickListener onEntryLongClickListener) {
        mOnEntryLongClickListener = onEntryLongClickListener;
    }
}
