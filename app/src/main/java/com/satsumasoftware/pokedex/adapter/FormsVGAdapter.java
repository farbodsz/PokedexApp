package com.satsumasoftware.pokedex.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;

public class FormsVGAdapter {

    private Activity mActivity;
    private ViewGroup mViewGroup;
    private MiniPokemon mPokemon;
    private ArrayList<String> mArrayAltForms;
    private View[] mItemViews; // Null if not yet created

    public FormsVGAdapter(Activity activity, ViewGroup viewGroup, MiniPokemon pokemon, ArrayList<String> arrayAltForms) {
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

        String aFormName = mArrayAltForms.get(itemNo);
        TextView tvForm = (TextView) view.findViewById(R.id.item_form_text1);
        if (aFormName.equals("")) {
            tvForm.setText("Normal");
        } else {
            tvForm.setText(aFormName);
        }

        TextView tvIsCurrent = (TextView) view.findViewById(R.id.item_form_text2);
        if (aFormName.equals(mPokemon.getForm())) {
            tvIsCurrent.setText("(selected)");
        } else {
            tvIsCurrent.setVisibility(View.GONE);
        }

        TextView tvType1 = (TextView) view.findViewById(R.id.item_form_text_type1);
        TextView tvType2 = (TextView) view.findViewById(R.id.item_form_text_type2);
        Pokemon anAltForm = new Pokemon(mActivity, mPokemon.getPokemon(), aFormName);
        String type1 = anAltForm.getType1();
        tvType1.setText(type1);
        tvType1.setBackgroundColor(mActivity.getResources().getColor(InfoUtils.getTypeBkgdColorRes(type1)));
        if (anAltForm.hasSecondaryType()) {
            tvType2.setVisibility(View.GONE);
        } else {
            String type2 = anAltForm.getType2();
            tvType2.setText(type2);
            tvType2.setBackgroundColor(mActivity.getResources().getColor(InfoUtils.getTypeBkgdColorRes(type2)));
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

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}