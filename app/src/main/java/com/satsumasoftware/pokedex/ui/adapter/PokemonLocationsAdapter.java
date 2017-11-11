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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.encounter.Encounter;
import com.satsumasoftware.pokedex.framework.encounter.EncounterMethodProse;
import com.satsumasoftware.pokedex.framework.encounter.EncounterSlot;
import com.satsumasoftware.pokedex.framework.location.Location;
import com.satsumasoftware.pokedex.framework.location.LocationArea;

import java.util.ArrayList;

public class PokemonLocationsAdapter extends
        RecyclerView.Adapter<PokemonLocationsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mLocationName, mEncounterMethod, mLevel, mRarity;
        ProgressBar mProgressBar;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLocationName = (TextView) itemView.findViewById(R.id.text1);
            mEncounterMethod = (TextView) itemView.findViewById(R.id.text2);
            mRarity = (TextView) itemView.findViewById(R.id.text3);
            mLevel = (TextView) itemView.findViewById(R.id.text4);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View v) {
            if (mOnRowClickListener != null) {
                mOnRowClickListener.onRowClick(v, getPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<Encounter> mEncounters;

    public PokemonLocationsAdapter(Context context, ArrayList<Encounter> encounters) {
        mContext = context;
        mEncounters = encounters;
    }

    @Override
    public int getItemCount() {
        return mEncounters.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_pokemon_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Encounter encounter = mEncounters.get(position);

        LocationArea locationArea = LocationArea.create(mContext, encounter.getLocationAreaId());
        Location location = Location.create(mContext, locationArea.getLocationId());

        String name = locationArea.hasName() ? locationArea.getName() + ", " + location.getName() :
                location.getName();
        holder.mLocationName.setText(name);

        EncounterSlot encounterSlot = EncounterSlot.create(mContext, encounter.getEncounterSlotId());
        EncounterMethodProse encounterMethod = EncounterMethodProse.create(
                mContext, encounterSlot.getEncounterMethodId());

        holder.mEncounterMethod.setText(encounterMethod.getName());

        String levelText = encounter.getMinLevel() == encounter.getMaxLevel() ?
                "Lv. " + encounter.getMinLevel() :
                "Lv. " + encounter.getMinLevel() + " - " + encounter.getMaxLevel();
        holder.mLevel.setText(levelText);

        String rarityText = encounterSlot.getRarity() + "%";
        holder.mRarity.setText(rarityText);

        holder.mProgressBar.setMax(100);
        holder.mProgressBar.setProgress(encounterSlot.getRarity());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnRowClickListener {
        void onRowClick(View view, int position);
    }

    private static OnRowClickListener mOnRowClickListener;

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        mOnRowClickListener = onRowClickListener;
    }
}
