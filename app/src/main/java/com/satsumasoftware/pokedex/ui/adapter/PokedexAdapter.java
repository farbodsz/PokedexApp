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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder>
        implements INameableAdapter, ICustomAdapter {

    @Override
    public Character getCharacterForElement(int element) {
        return mArrayPokemon.get(element).getName().charAt(0);
    }

    @Override
    public String getCustomStringForElement(int element) {
        int generationId = Pokemon.getGenerationId(
                mArrayPokemon.get(element).toPokemon(mContext).getMoreValues());
        return "Gen. " + DataUtilsKt.genIdToRoman(generationId);
    }

    public class PokedexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvName, tvId, tvForm;

        PokedexViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvId = (TextView) itemView.findViewById(R.id.text1);
            tvName = (TextView) itemView.findViewById(R.id.text2);
            tvForm = (TextView) itemView.findViewById(R.id.text3);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnEntryLongClickListener != null) {
                mOnEntryLongClickListener.onEntryLongClick(v, getLayoutPosition());
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

        holder.tvId.setText(DataUtilsKt.formatPokemonId(thisPokemon.getNationalDexNumber()));

        if (PrefUtils.combinePokemonNameInPokedex(mContext)) {
            holder.tvName.setText(thisPokemon.getFormAndPokemonName());
            holder.tvForm.setText("");
        } else {
            holder.tvName.setText(thisPokemon.getName());
            String form = thisPokemon.getFormName();
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


    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }


    private OnEntryLongClickListener mOnEntryLongClickListener;

    public interface OnEntryLongClickListener {
        void onEntryLongClick(View view, int position);
    }

    public void setOnEntryLongClickListener(OnEntryLongClickListener onEntryLongClickListener) {
        mOnEntryLongClickListener = onEntryLongClickListener;
    }
}
