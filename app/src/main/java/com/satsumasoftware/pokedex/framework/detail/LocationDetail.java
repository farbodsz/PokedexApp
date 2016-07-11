package com.satsumasoftware.pokedex.framework.detail;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.encounter.CompactEncounterDataHolder;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;

public class LocationDetail implements DetailInfo {

    private String mTitle;
    private SparseArray<CompactEncounterDataHolder> mCompactHolders;


    public LocationDetail(String title, SparseArray<CompactEncounterDataHolder> encounterDataHolders) {
        mTitle = title;
        mCompactHolders = encounterDataHolders;
    }


    @Override
    public void setupCard(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TextView title = (TextView) inflater.inflate(R.layout.card_pokemon_detail_title, container, false);
        title.setText(mTitle);
        container.addView(title);

        for (int i = 0; i < mCompactHolders.size(); i++) {
            CompactEncounterDataHolder compactHolder = mCompactHolders.valueAt(i);

            View row = inflater.inflate(R.layout.list_item_encounter, container, false);

            MiniPokemon pokemonObject = new MiniPokemon(context, compactHolder.getPokemonId());

            ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
            pokemonObject.setPokemonImage(imageView);

            TextView pokemon = (TextView) row.findViewById(R.id.text1);
            pokemon.setText(pokemonObject.getName());

            TextView level = (TextView) row.findViewById(R.id.text2);
            String levelText = (compactHolder.getMinLevel() == compactHolder.getMaxLevel()) ?
                    "Lv. " + compactHolder.getMinLevel() :
                    "Lv. " + compactHolder.getMinLevel() + " - " + compactHolder.getMaxLevel();
            level.setText(levelText);

            int rarity = compactHolder.getRarity();
            TextView rate = (TextView) row.findViewById(R.id.text3);
            rate.setText(rarity + "%");

            ProgressBar progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            progressBar.setMax(100);
            progressBar.setProgress(rarity);

            container.addView(row);
        }
    }
}
