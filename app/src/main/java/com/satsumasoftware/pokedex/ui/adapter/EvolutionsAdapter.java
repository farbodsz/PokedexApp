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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonEvolution;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;

public class EvolutionsAdapter extends RecyclerView.Adapter<EvolutionsAdapter.EvolutionsViewHolder> {

    public static class EvolutionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView text1, text2;

        EvolutionsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<MiniPokemon> mEvolutions;
    private MiniPokemon mCurrentPokemon;

    public EvolutionsAdapter(Context context, ArrayList<MiniPokemon> evolutions,
                             MiniPokemon currentPokemon) {
        mContext = context;
        mEvolutions = evolutions;
        mCurrentPokemon = currentPokemon;
    }

    @Override
    public int getItemCount() {
        return mEvolutions.size();
    }

    @Override
    public EvolutionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pokemon_with_image, parent, false);
        return new EvolutionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EvolutionsViewHolder holder, int position) {
        Pokemon pokemon = mEvolutions.get(position).toPokemon(mContext);

        if (PrefUtils.showPokemonImages(mContext)) {
            pokemon.setPokemonImage(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        boolean sameAsCurrent = pokemon.getSpeciesId() == mCurrentPokemon.getSpeciesId();

        String name = pokemon.getFormAndPokemonName();
        holder.text1.setText(sameAsCurrent ? Html.fromHtml("<b>" + name + "</b>") : name);

        ArrayList<PokemonEvolution> evolutionDataList = pokemon.getEvolutionDataObjects(mContext);

        StringBuilder stringBuilder = new StringBuilder();

        if (evolutionDataList == null) {
            stringBuilder.append("Base form");
        } else {
            for (int i = 0; i < evolutionDataList.size(); i++) {
                stringBuilder.append(evolutionDataList.get(i).makeDescriptionText(mContext));
                if (i != evolutionDataList.size() - 1) {
                    stringBuilder.append(";\n");
                }
            }
        }

        holder.text2.setText(stringBuilder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}
