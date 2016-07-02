package com.satsumasoftware.pokedex.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.pokemon.PokemonForm;

import java.util.ArrayList;

public class FormsVGAdapter {

    private Activity mActivity;
    private ViewGroup mViewGroup;
    private PokemonForm mPokemon;
    private ArrayList<PokemonForm> mArrayAltForms;
    private View[] mItemViews; // Null if not yet created

    public FormsVGAdapter(Activity activity, ViewGroup viewGroup, PokemonForm pokemon, ArrayList<PokemonForm> arrayAltForms) {
        mActivity = activity;
        mViewGroup = viewGroup;
        mPokemon = pokemon;
        mArrayAltForms = arrayAltForms;
    }

    public void createListItems() {
        mItemViews = new View[mArrayAltForms.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mArrayAltForms.size(); i++) {
            mItemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(mItemViews[i]);
            if (i != mArrayAltForms.size()-1) {
                mViewGroup.addView(mActivity.getLayoutInflater().inflate(R.layout.divider, mViewGroup, false));
            }
        }
    }

    private View makeListItem(final int itemNo, ViewGroup container) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_forms, container, false);

        if (mArrayAltForms == null) {
            throw new NullPointerException("alt forms is null");
        }

        PokemonForm pokemonForm = mArrayAltForms.get(itemNo);
        if (pokemonForm == null) {
            throw new NullPointerException("pkmn form is null");
        }

        TextView tvForm = (TextView) view.findViewById(R.id.item_form_text1);
        if (pokemonForm.isDefault()) {
            tvForm.setText("Normal");
        } else {
            tvForm.setText(pokemonForm.getFormName());
        }

        TextView tvIsCurrent = (TextView) view.findViewById(R.id.item_form_text2);
        if (pokemonForm.getFormId() == mPokemon.getFormId()) {
            tvIsCurrent.setText("(selected)");
        } else {
            tvIsCurrent.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEntryClickListener != null) {
                    mOnEntryClickListener.onEntryClick(v, itemNo);
                }
            }
        });

        return view;
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}