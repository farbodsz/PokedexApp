package com.satsumasoftware.pokedex.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.Experience;
import com.satsumasoftware.pokedex.framework.GrowthRate;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;

public class ExperienceCalculatorActivity extends BaseActivity implements LabelledSpinner.OnItemChosenListener {

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_EXPERIENCE; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }


    private GrowthRate mGrowthRate;
    private int mLevel;

    private boolean mEnterPokemon;
    private ArrayList<String> mStrPkmnList = new ArrayList<>();

    public static final String EXTRA_POKEMON = "POKEMON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_calculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdUtils.setupAds(this, R.id.adView);

        Bundle extras = getIntent().getExtras();

        RadioButton selectPokemon = (RadioButton) findViewById(R.id.radioButton_pokemon);
        RadioButton selectLevellingRate = (RadioButton) findViewById(R.id.radioButton_levelling_rate);

        mEnterPokemon = true;

        PokemonDBHelper helper = new PokemonDBHelper(this);
        for (MiniPokemon aPokemon : helper.getAllPokemon()) {
            String name = aPokemon.getName();
            if (!mStrPkmnList.contains(name)) mStrPkmnList.add(name);
        }

        final LabelledSpinner spinnerPokemonOrGrowth = (LabelledSpinner) findViewById(R.id.spinner_pokemon_or_growth);
        spinnerPokemonOrGrowth.setOnItemChosenListener(this);

        setupPokemonOrGrowthSpinner(spinnerPokemonOrGrowth);

        selectPokemon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEnterPokemon = true;
                    setupPokemonOrGrowthSpinner(spinnerPokemonOrGrowth);
                }
            }
        });
        selectLevellingRate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEnterPokemon = false;
                    setupPokemonOrGrowthSpinner(spinnerPokemonOrGrowth);
                }
            }
        });


        LabelledSpinner spinnerLevel = (LabelledSpinner) findViewById(R.id.spinner_level);
        ArrayList<Integer> arrayLevels = new ArrayList<>();
        for (int i = 1; i < 101; i++) { arrayLevels.add(i); } // Adding numbers 1 to 100
        spinnerLevel.setItemsArray(arrayLevels);
        spinnerLevel.setOnItemChosenListener(this);

        // Calculate for Pokemon in previous activity
        if (extras != null) {
            MiniPokemon pokemon = extras.getParcelable(EXTRA_POKEMON);
            spinnerPokemonOrGrowth.setSelection(mStrPkmnList.indexOf(pokemon.getName()));
            mGrowthRate = new GrowthRate(findGrowthRateId(this, pokemon.getName()));
            spinnerLevel.setSelection(50-1);
            mLevel = 50;
            calculateExp();
        }


        Button btnCalc = (Button) findViewById(R.id.button_calculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateExp();
            }
        });
    }

    private void setupPokemonOrGrowthSpinner(LabelledSpinner spinner) {
        if (mEnterPokemon) {
            spinner.setLabelText(R.string.attr_pokemon);
            spinner.setItemsArray(mStrPkmnList);
        } else {
            spinner.setLabelText(R.string.attr_levelling_rate);
            spinner.setItemsArray(R.array.list_levelling_rates);
        }
    }

    private void calculateExp() {
        int exp = Experience.getTotalExperience(this, mGrowthRate, mLevel);

        TextView tvAnswer = (TextView) findViewById(R.id.text_answer);
        tvAnswer.setText(String.valueOf(exp));

        TextView tvDescription = (TextView) findViewById(R.id.text_description);
        tvDescription.setText(getResources().getString(R.string.experience_description, mLevel, mGrowthRate));
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selected = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_pokemon_or_growth:
                if (mEnterPokemon) {
                    // mGrowthRate will be abbreviated here, thus
                    mGrowthRate = new GrowthRate(findGrowthRateId(getBaseContext(), selected));
                } else {
                    mGrowthRate = new GrowthRate(selected);
                }
                break;
            case R.id.spinner_level:
                mLevel = Integer.parseInt(selected);
                break;
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}


    private static int findGrowthRateId(Context context, String pokemonName) {
        PokemonDBHelper helper = new PokemonDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                new String[] {PokemonDBHelper.COL_ID, PokemonDBHelper.COL_IS_DEFAULT,
                        PokemonDBHelper.COL_GROWTH_RATE_ID},
                PokemonDBHelper.COL_NAME + "=? AND " + PokemonDBHelper.COL_IS_DEFAULT + "=?",
                new String[] {String.valueOf(pokemonName), String.valueOf(1)},
                null, null, null);
        cursor.moveToFirst();
        int value = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_GROWTH_RATE_ID));
        cursor.close();
        return value;
    }
}
