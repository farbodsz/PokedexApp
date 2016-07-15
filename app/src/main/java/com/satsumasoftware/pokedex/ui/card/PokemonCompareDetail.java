package com.satsumasoftware.pokedex.ui.card;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.ui.PkmnImageActivity;
import com.satsumasoftware.pokedex.ui.PkmnTypeDetailActivity;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class PokemonCompareDetail implements DetailCard {

    public static final int MODE_NORMAL = 0, MODE_GENERAL_INFO = 1, MODE_GENDER = 2, MODE_STATS = 3;
    @IntDef({MODE_NORMAL, MODE_GENERAL_INFO, MODE_GENDER, MODE_STATS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CardType {}

    private @CardType int mMode;

    private @StringRes int mTitle;
    private ArrayList<String> mProperties;
    private ArrayList<ArrayList<?>> mValuesArrays;
    private String[] mPkmnNames;

    private ArrayList<ArrayList<View.OnClickListener>> mOnClickListenersArray;


    public PokemonCompareDetail(@StringRes int title, ArrayList<String> properties,
                                ArrayList<ArrayList<?>> valuesArray) {
        checkOuterListSize(valuesArray);
        if (properties.size() != valuesArray.get(0).size() ||
                properties.size() != valuesArray.get(1).size()) {
            throw new IllegalArgumentException("the lengths of the lists must match");
        }

        mMode = MODE_NORMAL;

        mTitle = title;
        mProperties = properties;
        mValuesArrays = valuesArray;
    }

    public PokemonCompareDetail(ArrayList<ArrayList<?>> valuesArrays) {
        checkOuterListSize(valuesArrays);
        mMode = MODE_GENERAL_INFO;
        mValuesArrays = valuesArrays;
    }

    public PokemonCompareDetail(ArrayList<ArrayList<?>> genderRateIdArray,
                                String[] pokemonNames) {
        checkOuterListSize(genderRateIdArray);
        if (genderRateIdArray.get(0).size() != 1 || genderRateIdArray.get(0).size() != 1) {
            throw new IllegalArgumentException("this list(s) must only contain one item - " +
                    "the gender rate id");
        }

        mMode = MODE_GENDER;

        mTitle = R.string.header_gender_ratio;
        mValuesArrays = genderRateIdArray;
        mPkmnNames = pokemonNames;
    }

    public PokemonCompareDetail(ArrayList<ArrayList<?>> statInfoArray, boolean evStats) {
        checkOuterListSize(statInfoArray);
        if (statInfoArray.get(0).size() != 6 || statInfoArray.get(1).size() != 6) {
            throw new IllegalArgumentException("the list(s) passed is invalid (must be of size 6)");
        }
        mMode = MODE_STATS;

        mTitle = evStats ? R.string.header_stats_ev : R.string.header_stats;
        mValuesArrays = statInfoArray;
    }

    private static void checkOuterListSize(ArrayList<ArrayList<?>> list) {
        if (list.size() != 2) {
            throw new IllegalArgumentException("the list must contain 2 other lists; this " +
                    "contains " + list.size());
        }
    }

    public void addOnClickListeners(ArrayList<ArrayList<View.OnClickListener>> onClickListenersArray) {
        if (onClickListenersArray.size() != 2) {
            throw new IllegalArgumentException("the list must contain 2 other lists; this " +
                    "contains " + onClickListenersArray.size());
        }
        if (onClickListenersArray.get(0).size() != mProperties.size() ||
                onClickListenersArray.get(1).size() != mProperties.size()) {
            throw new IllegalArgumentException("the length of the list(s) must be the same size " +
                    "as the others");
        }
        mOnClickListenersArray = onClickListenersArray;
    }

    @Override
    public void setupCard(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (mMode != MODE_GENERAL_INFO) {
            TextView infoTitle = (TextView) inflater.inflate(R.layout.card_pokemon_detail_title, container, false);
            infoTitle.setText(context.getResources().getText(mTitle));
            infoTitle.setGravity(Gravity.CENTER);  // as it is a comparison, text is centered
            container.addView(infoTitle);
        }

        switch (mMode) {
            case MODE_NORMAL:
                makeNormalCard(container, inflater);
                break;
            case MODE_GENERAL_INFO:
                makeGeneralInfoCard(context, container, inflater);
                break;
            case MODE_GENDER:
                makeGenderCard(context, container, inflater);
                break;
            case MODE_STATS:
                makeStatCard(context, container, inflater);
                break;
        }
    }


    private void makeNormalCard(ViewGroup container, LayoutInflater inflater) {
        for (int i = 0; i < mProperties.size(); i++) {
            if (mValuesArrays.get(0).get(i) == null && mValuesArrays.get(1).get(i) == null) {
                continue;
            }

            View row = inflater.inflate(R.layout.card_pokemon_detail_compare_row, container, false);

            TextView property = (TextView) row.findViewById(R.id.text_property);
            property.setText(mProperties.get(i));

            TextView[] textViews = new TextView[] {
                    (TextView) row.findViewById(R.id.text_value1),
                    (TextView) row.findViewById(R.id.text_value2)};

            for (int n = 0; n < 2; n++) {
                ArrayList<?> values = mValuesArrays.get(n);
                ArrayList<View.OnClickListener> onClickListeners = mOnClickListenersArray.get(n);

                if (values.get(i) == null) {
                    textViews[n].setText("-");
                } else {
                    String ability = String.valueOf(values.get(i));
                    textViews[n].setText(ability);
                    textViews[n].setOnClickListener(onClickListeners.get(i));
                }
            }

            container.addView(row);
        }
    }

    private void makeGeneralInfoCard(final Context context, ViewGroup container, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.card_pokemon_detail_compare_general, container, false);

        TextView[] dexNumberViews = new TextView[] {
                (TextView) view.findViewById(R.id.pokedexId_1),
                (TextView) view.findViewById(R.id.pokedexId_2)};
        TextView[] pokemonNameViews = new TextView[] {
                (TextView) view.findViewById(R.id.pokemonName_1),
                (TextView) view.findViewById(R.id.pokemonName_2)};
        TextView[] formViews = new TextView[] {
                (TextView) view.findViewById(R.id.form_1),
                (TextView) view.findViewById(R.id.form_2)};
        TextView[] speciesViews = new TextView[] {
                (TextView) view.findViewById(R.id.species_1),
                (TextView) view.findViewById(R.id.species_2)};
        ImageView[] imageViews = new ImageView[] {
                (ImageView) view.findViewById(R.id.imageView_1),
                (ImageView) view.findViewById(R.id.imageView_2)};
        TextView[] type1Views = new TextView[] {
                (TextView) view.findViewById(R.id.type1_1),
                (TextView) view.findViewById(R.id.type1_2)};
        TextView[] type2Views = new TextView[] {
                (TextView) view.findViewById(R.id.type2_1),
                (TextView) view.findViewById(R.id.type2_2)};

        for (int i = 0; i < 2; i++) {
            ArrayList<?> valuesArray = mValuesArrays.get(i);

            final MiniPokemon pokemon = (MiniPokemon) valuesArray.get(0);

            String pokedexNumber = InfoUtils.formatPokemonId(pokemon.getNationalDexNumber());
            dexNumberViews[i].setText("# " + pokedexNumber);

            String name = pokemon.getName();
            pokemonNameViews[i].setText(name);

            String formName = pokemon.getFormName();
            if ((Boolean) valuesArray.get(1)) {  // Pokemon.isDefault(...)
                formViews[i].setVisibility(View.GONE);
            } else {
                formViews[i].setText(formName);
            }

            speciesViews[i].setText((String) valuesArray.get(2));

            if (PrefUtils.showPokemonImages(context)) {
                int id = pokemon.getId();
                boolean isFormMega = (Boolean) valuesArray.get(3);
                ActionUtils.setPokemonImage(id, InfoUtils.formatPokemonId(pokemon.getSpeciesId()),
                        name, isFormMega, imageViews[i]);
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent imgDetailIntent = new Intent(context, PkmnImageActivity.class);
                        imgDetailIntent.putExtra(PkmnImageActivity.EXTRA_POKEMON, pokemon);
                        context.startActivity(imgDetailIntent);
                    }
                });
            } else {
                imageViews[i].setVisibility(View.GONE);
            }

            View.OnClickListener typeClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent typeDetailIntent = new Intent(context, PkmnTypeDetailActivity.class);
                    typeDetailIntent.putExtra(PkmnTypeDetailActivity.EXTRA_POKEMON, pokemon);
                    context.startActivity(typeDetailIntent);
                }
            };

            TextView type1View = type1Views[i];
            TextView type2View = type2Views[i];

            String type1 = (String) valuesArray.get(4);
            type1View.setText(type1);
            type1View.setOnClickListener(typeClickListener);
            type1View.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
            type1View.setTextColor(ContextCompat.getColor(context, R.color.mdu_text_white));

            boolean hasSecondaryType = (Boolean) valuesArray.get(6);
            if (!hasSecondaryType) {
                type2View.setVisibility(View.GONE);
            } else {
                String type2 = (String) valuesArray.get(5);
                type2View.setText(type2);
                type2View.setOnClickListener(typeClickListener);
                type2View.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type2));
                type2View.setTextColor(ContextCompat.getColor(context, R.color.mdu_text_white));
            }
        }

        container.addView(view);
    }

    private void makeGenderCard(Context context, ViewGroup container, LayoutInflater inflater) {
        View row = inflater.inflate(R.layout.card_pokemon_detail_compare_row_gender, container, false);

        ProgressBar[] progressBars = {
                (ProgressBar) row.findViewById(R.id.progressBar_1),
                (ProgressBar) row.findViewById(R.id.progressBar_2)};
        TextView[] maleTexts = {
                (TextView) row.findViewById(R.id.genderMale_1),
                (TextView) row.findViewById(R.id.genderMale_2)};
        TextView[] femaleTexts = {
                (TextView) row.findViewById(R.id.genderFemale_1),
                (TextView) row.findViewById(R.id.genderFemale_2)};

        for (int i = 0; i < 2; i++) {
            progressBars[i].setMax(100);

            int genderRateId = Integer.parseInt(String.valueOf(mValuesArrays.get(i).get(0)));

            if (Pokemon.isGenderless(genderRateId)) {
                femaleTexts[i].setVisibility(View.GONE);
                maleTexts[i].setText(mPkmnNames[i] + " is genderless");
                maleTexts[i].setTextColor(ContextCompat.getColor(context, R.color.mdu_text_black_secondary));
                progressBars[i].setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.progress_gender_neutral));
            } else {
                double genderMale = DataUtils.maleFromGenderRate(genderRateId);
                double genderFemale = 100.0 - genderMale;
                progressBars[i].setProgress((int) genderMale);
                maleTexts[i].setText(Html.fromHtml("<i><b>Male: </b>" + genderMale + "%</i>"));
                femaleTexts[i].setText(Html.fromHtml("<i><b>Female: </b>" + genderFemale + "%</i>"));
                maleTexts[i].setTextColor(ContextCompat.getColor(context, R.color.progress_gender_male));
                femaleTexts[i].setTextColor(ContextCompat.getColor(context, R.color.progress_gender_female));
                progressBars[i].setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.progress_gender));
            }
        }

        container.addView(row);
    }

    private void makeStatCard(Context context, ViewGroup container, LayoutInflater inflater) {
        int[] labels = new int[] {
                R.string.attr_stat_hp, R.string.attr_stat_atk,
                R.string.attr_stat_def, R.string.attr_stat_spA,
                R.string.attr_stat_spD, R.string.attr_stat_spe,
                R.string.attr_stat_total, R.string.attr_stat_average};
        int[] maxes = new int[] {255, 200, 230, 200, 230, 200};
        int maxTotal = 800;
        int maxAvg = 150;

        int[] totalCounts = {0, 0};

        for (int i = 0; i < 8; i++) {
            View row = inflater.inflate(R.layout.card_pokemon_detail_compare_row_stats, container, false);

            TextView propertyText = (TextView) row.findViewById(R.id.propertyText);
            ProgressBar[] progressBars = {
                    (ProgressBar) row.findViewById(R.id.progressBar_1),
                    (ProgressBar) row.findViewById(R.id.progressBar_2)};
            TextView[] valueTexts = {
                    (TextView) row.findViewById(R.id.text_value1),
                    (TextView) row.findViewById(R.id.text_value2)};

            Resources res = context.getResources();
            propertyText.setText(res.getString(labels[i]));

            for (int p = 0; p < 2; p++) {
                if (i == 6) {
                    progressBars[p].setMax(maxTotal);
                    progressBars[p].setProgress(totalCounts[p]);
                    valueTexts[p].setText(String.valueOf(totalCounts[p]));
                } else if (i == 7) {
                    progressBars[p].setMax(maxAvg);
                    progressBars[p].setProgress(totalCounts[p] / 6);
                    valueTexts[p].setText(String.valueOf(totalCounts[p] / 6));
                } else {
                    ArrayList<?> values = mValuesArrays.get(p);

                    String strVal = String.valueOf(values.get(i));
                    progressBars[p].setMax(maxes[i]);
                    progressBars[p].setProgress(Integer.parseInt(strVal));
                    valueTexts[p].setText(String.valueOf(values.get(i)));

                    totalCounts[p] += Integer.parseInt(strVal);
                }
            }

            container.addView(row);
        }
    }
}
