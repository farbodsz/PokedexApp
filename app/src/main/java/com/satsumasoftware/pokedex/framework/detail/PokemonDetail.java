package com.satsumasoftware.pokedex.framework.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
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

public class PokemonDetail implements DetailInfo {

    public static final int MODE_NORMAL = 0, MODE_GENERAL_INFO = 1, MODE_GENDER = 2, MODE_STATS = 3;
    @IntDef({MODE_NORMAL, MODE_GENERAL_INFO, MODE_GENDER, MODE_STATS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CardType {}

    private @CardType int mMode;

    private @StringRes int mTitle;
    private ArrayList<String> mProperties;
    private ArrayList<?> mValues;
    private String mPkmnName;

    private ArrayList<View.OnClickListener> mOnClickListeners;


    public PokemonDetail(@StringRes int title, ArrayList<String> properties, ArrayList<String> values) {
        if (properties.size() != values.size()) {
            throw new IllegalArgumentException("the lengths of the two lists must match");
        }
        mMode = MODE_NORMAL;

        mTitle = title;
        mProperties = properties;
        mValues = values;
    }

    public PokemonDetail(ArrayList<Object> values) {
        mMode = MODE_GENERAL_INFO;
        mValues = values;
    }

    public PokemonDetail(ArrayList<Integer> genderRateId, String pkmnName) {
        if (genderRateId.size() != 1) {
            throw new IllegalArgumentException("this list must only contain one item - " +
                    "the gender rate id");
        }
        mMode = MODE_GENDER;

        mTitle = R.string.header_gender_ratio;
        mValues = genderRateId;
        mPkmnName = pkmnName;
    }

    public PokemonDetail(ArrayList<Integer> statInfo, boolean evStats) {
        if (statInfo.size() != 6) {
            throw new IllegalArgumentException("the list passed is invalid (must be of size 6)");
        }
        mMode = MODE_STATS;

        mTitle = evStats ? R.string.header_stats_ev : R.string.header_stats;
        mValues = statInfo;
    }


    public void addOnClickListeners(ArrayList<View.OnClickListener> onClickListeners) {
        if (onClickListeners.size() != mProperties.size()) {
            throw new IllegalArgumentException("the length of the list must be the same size as " +
                    "the others");
        }
        mOnClickListeners = onClickListeners;
    }

    @Override
    public void setupCard(Context context, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (mMode != MODE_GENERAL_INFO) {
            TextView infoTitle = (TextView) inflater.inflate(R.layout.card_pokemon_detail_title, container, false);
            infoTitle.setText(context.getResources().getText(mTitle));
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
            View row = inflater.inflate(R.layout.card_pokemon_detail_row, container, false);

            TextView property = (TextView) row.findViewById(R.id.pokemon_detail_row_property);
            property.setText(mProperties.get(i));

            TextView value = (TextView) row.findViewById(R.id.pokemon_detail_row_value);
            value.setText(String.valueOf(mValues.get(i)));
            if (mOnClickListeners != null) {
                value.setOnClickListener(mOnClickListeners.get(i));
            }

            container.addView(row);
        }
    }

    private void makeGeneralInfoCard(final Context context, ViewGroup container, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.card_pokemon_detail_general, container, false);

        final MiniPokemon pokemon = (MiniPokemon) mValues.get(0);

        String pokedexNumber = InfoUtils.formatPokemonId(pokemon.getNationalDexNumber());
        ((TextView) view.findViewById(R.id.pokedexNumber)).setText("# " + pokedexNumber);

        String name = pokemon.getName();
        ((TextView) view.findViewById(R.id.pokemonName)).setText(name);

        TextView formText = (TextView) view.findViewById(R.id.form);
        String formName = pokemon.getFormName();
        if ((Boolean) mValues.get(1)) {  // Pokemon.isDefault(...)
            formText.setVisibility(View.GONE);
        } else {
            formText.setText(formName);
        }

        ((TextView) view.findViewById(R.id.species)).setText((String) mValues.get(2));

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if (PrefUtils.showPokemonImages(context)) {
            int id = pokemon.getId();
            boolean isFormMega = (Boolean) mValues.get(3);
            ActionUtils.setPokemonImage(id, InfoUtils.formatPokemonId(pokemon.getSpeciesId()),
                    name, isFormMega, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent imgDetailIntent = new Intent(context, PkmnImageActivity.class);
                    imgDetailIntent.putExtra(PkmnImageActivity.EXTRA_POKEMON, pokemon);
                    context.startActivity(imgDetailIntent);
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }


        View.OnClickListener typeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent typeDetailIntent = new Intent(context, PkmnTypeDetailActivity.class);
                typeDetailIntent.putExtra(PkmnTypeDetailActivity.EXTRA_POKEMON, pokemon);
                context.startActivity(typeDetailIntent);
            }
        };

        TextView type1View = (TextView) view.findViewById(R.id.type1);
        TextView type2View = (TextView) view.findViewById(R.id.type2);

        String type1 = (String) mValues.get(4);
        type1View.setText(type1);
        type1View.setOnClickListener(typeClickListener);
        type1View.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
        type1View.setTextColor(ContextCompat.getColor(context, R.color.mdu_text_white));

        boolean hasSecondaryType = (Boolean) mValues.get(6);
        if (!hasSecondaryType) {
            type2View.setVisibility(View.GONE);
        } else {
            String type2 = (String) mValues.get(5);
            type2View.setText(type2);
            type2View.setOnClickListener(typeClickListener);
            type2View.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type2));
            type2View.setTextColor(ContextCompat.getColor(context, R.color.mdu_text_white));
        }

        container.addView(view);
    }

    private void makeGenderCard(Context context, ViewGroup container, LayoutInflater inflater) {
        View row = inflater.inflate(R.layout.card_pokemon_detail_row_gender, container, false);
        ProgressBar progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
        TextView maleText = (TextView) row.findViewById(R.id.genderMale);
        TextView femaleText = (TextView) row.findViewById(R.id.genderFemale);

        progressBar.setMax(100);

        int genderRateId = Integer.parseInt(String.valueOf(mValues.get(0)));

        if (Pokemon.isGenderless(genderRateId)) {
            femaleText.setVisibility(View.GONE);
            maleText.setText(mPkmnName + " is genderless");
            maleText.setTextColor(ContextCompat.getColor(context, R.color.mdu_text_black_secondary));
            progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.progress_gender_neutral));
        } else {
            double genderMale = DataUtils.maleFromGenderRate(genderRateId);
            double genderFemale = 100.0 - genderMale;
            progressBar.setProgress((int) genderMale);
            maleText.setText(Html.fromHtml("<i><b>Male: </b>" + genderMale + "%</i>"));
            femaleText.setText(Html.fromHtml("<i><b>Female: </b>" + genderFemale + "%</i>"));
            maleText.setTextColor(ContextCompat.getColor(context, R.color.progress_gender_male));
            femaleText.setTextColor(ContextCompat.getColor(context, R.color.progress_gender_female));
            progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.progress_gender));
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

        int totalCount = 0;

        for (int i = 0; i < 8; i++) {
            View row = inflater.inflate(R.layout.card_pokemon_detail_row_stats, container, false);

            TextView property = (TextView) row.findViewById(R.id.propertyText);
            Resources res = context.getResources();
            property.setText(res.getString(labels[i]));

            ProgressBar progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            TextView value = (TextView) row.findViewById(R.id.valueText);

            if (i == 6) {
                progressBar.setMax(maxTotal);
                progressBar.setProgress(totalCount);
                value.setText(String.valueOf(totalCount));
                container.addView(row);
                continue;
            } else if (i == 7) {
                progressBar.setMax(maxAvg);
                progressBar.setProgress(totalCount / 6);
                value.setText(String.valueOf(totalCount / 6));
                container.addView(row);
                continue;
            }

            String strVal = String.valueOf(mValues.get(i));
            progressBar.setMax(maxes[i]);
            progressBar.setProgress(Integer.parseInt(strVal));
            value.setText(String.valueOf(mValues.get(i)));
            if (mOnClickListeners != null) {
                value.setOnClickListener(mOnClickListeners.get(i));
            }

            totalCount += Integer.parseInt(strVal);

            container.addView(row);
        }
    }

}
