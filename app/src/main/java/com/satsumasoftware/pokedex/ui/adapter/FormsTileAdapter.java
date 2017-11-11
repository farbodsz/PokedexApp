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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.Type;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonForm;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.satsumasoftware.pokedex.util.ThemeUtilsKt;

import java.util.ArrayList;

public class FormsTileAdapter extends RecyclerView.Adapter<FormsTileAdapter.FormsTileViewHolder> {

    public class FormsTileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View backgroundView;
        ImageView imageView;
        TextView textView;

        FormsTileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            backgroundView = itemView.findViewById(R.id.view_background);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
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
    private ArrayList<PokemonForm> mArrayPokemon;

    public FormsTileAdapter(Context context, ArrayList<PokemonForm> arrayPokemon) {
        mContext = context;
        mArrayPokemon = arrayPokemon;
    }

    @Override
    public int getItemCount() {
        return mArrayPokemon.size();
    }

    @Override
    public FormsTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_simple, parent, false);
        return new FormsTileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FormsTileViewHolder holder, int position) {
        PokemonForm pokemonForm = mArrayPokemon.get(position);

        int color;
        switch (PrefUtils.detailColorType(mContext)) {
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_TYPE:
                color = ThemeUtilsKt.getLightColourByType(mContext, new Type(pokemonForm.getPrimaryTypeId()).getName());
                break;
            default:
                color = R.color.theme_neutral_grey;
                break;
        }

        holder.backgroundView.setBackgroundColor(ContextCompat.getColor(mContext, color));
        pokemonForm.setPokemonImage(holder.imageView);

        String form;
        if (pokemonForm.isDefault()) {
            form = "Normal";
        } else {
            form = pokemonForm.getFormName();
        }
        holder.textView.setText(form);
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
