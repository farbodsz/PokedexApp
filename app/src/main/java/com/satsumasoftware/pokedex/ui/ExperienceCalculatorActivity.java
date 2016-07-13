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
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;

public class ExperienceCalculatorActivity extends BaseActivity implements LabelledSpinner.OnItemChosenListener {

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.experience_toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.experience_drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_EXPERIENCE; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.experience_navigationView); }


    private String mGrowth, mLevel;

    private boolean mEnterPokemon;
    private ArrayList<String> mStrPkmnList = new ArrayList<>();

    public static final String EXTRA_POKEMON = "POKEMON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_calculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.experience_toolbar);
        setSupportActionBar(toolbar);

        AdUtils.setupAds(this, R.id.experience_adView);

        Bundle extras = getIntent().getExtras();

        RadioButton selectPokemon = (RadioButton) findViewById(R.id.experience_radioPokemon);
        RadioButton selectLevellingRate = (RadioButton) findViewById(R.id.experience_radioLevellingRate);

        mEnterPokemon = true;

        PokemonDBHelper helper = new PokemonDBHelper(this);
        for (MiniPokemon aPokemon : helper.getAllPokemon()) {
            String name = aPokemon.getName();
            if (!mStrPkmnList.contains(name)) mStrPkmnList.add(name);
        }

        final LabelledSpinner spinnerPokemonOrGrowth = (LabelledSpinner) findViewById(R.id.experience_spinnerPokemonOrGrowth);
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


        LabelledSpinner spinnerLevel = (LabelledSpinner) findViewById(R.id.experience_spinnerLevel);
        ArrayList<Integer> arrayLevels = new ArrayList<>();
        for (int i = 1; i < 101; i++) { arrayLevels.add(i); } // Adding numbers 1 to 100
        spinnerLevel.setItemsArray(arrayLevels);
        spinnerLevel.setOnItemChosenListener(this);

        // Calculate for Pokemon in previous activity
        if (extras != null) {
            MiniPokemon pokemon = extras.getParcelable(EXTRA_POKEMON);
            spinnerPokemonOrGrowth.setSelection(mStrPkmnList.indexOf(pokemon.getName())); // TODO <-- carry on from here
            mGrowth = InfoUtils.idToGrowth(findIntValue(this, pokemon.getName(), PokemonDBHelper.COL_GROWTH_RATE_ID));
            spinnerLevel.setSelection(50-1);
            mLevel = "50";
            calculateExp();
        }


        Button btnCalc = (Button) findViewById(R.id.experience_btnCalc);
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
        String exp = Experience.getTotalExperience(
                getBaseContext(),
                Experience.getGrowthIdFromString(mGrowth),
                mLevel);
        if (exp == null) throw new NullPointerException("exp is null");

        TextView tvAnswer = (TextView) findViewById(R.id.experience_tvAnswer);
        tvAnswer.setText(exp);

        TextView tvDescription = (TextView) findViewById(R.id.experience_tvDescription);
        tvDescription.setText(getResources().getString(R.string.experience_description, mLevel, mGrowth));
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selected = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.experience_spinnerPokemonOrGrowth:
                if (mEnterPokemon) {
                    // mGrowth will be abbreviated here, thus
                    mGrowth = InfoUtils.idToGrowth(findIntValue(
                            getBaseContext(), selected, PokemonDBHelper.COL_GROWTH_RATE_ID));
                } else {
                    mGrowth = selected;
                }
                break;
            case R.id.experience_spinnerLevel:
                mLevel = selected;
                break;
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}


    public static int findIntValue(Context context, String name, String columnName) {
        PokemonDBHelper helper = new PokemonDBHelper(context);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                new String[] {PokemonDBHelper.COL_ID, PokemonDBHelper.COL_IS_DEFAULT, columnName},
                PokemonDBHelper.COL_NAME + "=? AND " + PokemonDBHelper.COL_IS_DEFAULT + "=?",
                new String[] {String.valueOf(name), String.valueOf(1)},
                null, null, null);
        cursor.moveToFirst();
        int value = cursor.getInt(cursor.getColumnIndex(columnName));
        cursor.close();
        return value;
    }
}
