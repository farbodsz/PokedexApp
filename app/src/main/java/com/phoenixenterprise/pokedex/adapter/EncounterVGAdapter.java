package com.phoenixenterprise.pokedex.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.DetailActivity;
import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.object.EncounterItem;
import com.phoenixenterprise.pokedex.object.MiniPokemon;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.util.ArrayList;

public class EncounterVGAdapter {

    private ViewGroup mViewGroup;
    private ArrayList<EncounterItem> mEncounterItems;
    private Activity mActivity;
    private View[] mItemViews; // Null if not yet created

    public EncounterVGAdapter(Activity activity, ViewGroup viewGroup, ArrayList<EncounterItem> encounterItems) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mEncounterItems = encounterItems;
    }

    public void createListItems() {
        mItemViews = new View[mEncounterItems.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mEncounterItems.size(); i++) {
            mItemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(mItemViews[i]);
            if (i != mEncounterItems.size()-1) {
                mViewGroup.addView(mActivity.getLayoutInflater().inflate(R.layout.divider, mViewGroup, false));
            }
        }
    }

    private View makeListItem(int itemNo, ViewGroup container) {
        EncounterItem encounter = mEncounterItems.get(itemNo);
        final MiniPokemon miniPokemon = encounter.getPokemon();

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_encounter, container, false);

        TextView tvId = (TextView) view.findViewById(R.id.item_encounter_text1);
        TextView tvName = (TextView) view.findViewById(R.id.item_encounter_text2);
        TextView tvLevels = (TextView) view.findViewById(R.id.item_encounter_text3);
        TextView tvRarity = (TextView) view.findViewById(R.id.item_encounter_text4);

        tvId.setText(miniPokemon.getNationalIdFormatted());
        //tvName.setText(InfoUtils.getNameAndForm(miniPokemon));
        tvName.setText(miniPokemon.getPokemon());

        int minLvl = encounter.getGeneralMinLvl();
        int maxLvl = encounter.getGeneralMaxLvl();
        int rarity = encounter.getTotalRarity();
        if (minLvl == maxLvl) {
            tvLevels.setText("Lv. " + minLvl);
        } else {
            tvLevels.setText("Lv. " + minLvl + " - " + maxLvl);
        }

        tvRarity.setText("("+rarity+"%)");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra("POKEMON", miniPokemon);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }
}