package com.satsumasoftware.pokedex.framework.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.encounter.Encounter;
import com.satsumasoftware.pokedex.framework.encounter.EncounterDataHolder;
import com.satsumasoftware.pokedex.framework.encounter.EncounterSlot;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;

import java.util.ArrayList;

public class LocationDetail implements DetailInfo {

    private String mTitle;
    private ArrayList<EncounterDataHolder> mEncounterDataHolders;


    public LocationDetail(String title, ArrayList<EncounterDataHolder> encounterDataHolders) {
        mTitle = title;
        mEncounterDataHolders = encounterDataHolders;
    }


    @Override
    public void setupCard(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TextView title = (TextView) inflater.inflate(R.layout.card_pokemon_detail_title, container, false);
        title.setText(mTitle);
        container.addView(title);

        for (int i = 0; i < mEncounterDataHolders.size(); i++) {
            EncounterDataHolder encounterData = mEncounterDataHolders.get(i);
            Encounter encounter = encounterData.getEncounter();
            EncounterSlot encounterSlot = encounterData.getEncounterSlot();

            View row = inflater.inflate(R.layout.list_item_encounter, container, false);

            TextView pokemon = (TextView) row.findViewById(R.id.text1);
            pokemon.setText(new MiniPokemon(context, encounter.getPokemonId()).getName());

            TextView level = (TextView) row.findViewById(R.id.text2);
            level.setText("Lv. " + encounter.getMinLevel() + " - " + encounter.getMaxLevel());

            int rarity = encounterSlot.getRarity();
            TextView rate = (TextView) row.findViewById(R.id.text3);
            rate.setText(rarity + "%");

            ProgressBar progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            progressBar.setMax(100);
            progressBar.setProgress(rarity);

            container.addView(row);
        }
    }
}
