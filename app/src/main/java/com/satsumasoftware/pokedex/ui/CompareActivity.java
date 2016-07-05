package com.satsumasoftware.pokedex.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.ability.MiniAbility;
import com.satsumasoftware.pokedex.entities.detail.DetailInfo;
import com.satsumasoftware.pokedex.entities.detail.PokemonCompareDetail;
import com.satsumasoftware.pokedex.entities.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.entities.pokemon.Pokemon;
import com.satsumasoftware.pokedex.entities.pokemon.PokemonMoves;
import com.satsumasoftware.pokedex.ui.adapter.DetailAdapter;
import com.satsumasoftware.pokedex.ui.adapter.PokemonMovesVgAdapter;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AppConfig;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class CompareActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON_1 = "POKEMON_1";
    public static final String EXTRA_POKEMON_2 = "POKEMON_2";

    private static Pokemon mPokemon1, mPokemon2;
    private static String mPkmnName1, mPkmnName2;
    private String mPkmnFullName1, mPkmnFullName2;

    private static Pokemon[] mPokemonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        MiniPokemon miniPokemon1 = extras.getParcelable(EXTRA_POKEMON_1);
        MiniPokemon miniPokemon2 = extras.getParcelable(EXTRA_POKEMON_2);

        if (miniPokemon1 == null || miniPokemon2 == null) {
            throw new NullPointerException("Parcelable MiniPokemon object(s) through Intent is null");
        }

        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        assert toolbar != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AdUtils.setupAds(this, R.id.adView);

        mPokemon1 = miniPokemon1.toPokemon(this);
        mPokemon2 = miniPokemon2.toPokemon(this);
        mPokemonArray = new Pokemon[] {mPokemon1, mPokemon2};

        mPkmnName1 = mPokemon1.getName();
        mPkmnName2 = mPokemon2.getName();

        mPkmnFullName1 = mPokemon1.getFormAndPokemonName();
        mPkmnFullName2 = mPokemon2.getFormAndPokemonName();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.mdu_text_white_secondary),
                ContextCompat.getColor(this, R.color.mdu_text_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.mdu_white));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        getSupportActionBar().setSubtitle(mPkmnFullName1 + " vs. " + mPkmnFullName2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compare, menu);

        menu.findItem(R.id.action_calculate_experience_pkmn1)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, mPkmnFullName1));
        menu.findItem(R.id.action_calculate_experience_pkmn2)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, mPkmnFullName2));

        menu.findItem(R.id.action_play_cry_pkmn1)
                .setTitle(getResources().getString(R.string.action_play_cry_of, mPkmnFullName1));
        menu.findItem(R.id.action_play_cry_pkmn2)
                .setTitle(getResources().getString(R.string.action_play_cry_of, mPkmnFullName2));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculate_experience_pkmn1:
                Intent expCalcIntent1 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent1.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, mPokemon1.toMiniPokemon());
                startActivity(expCalcIntent1);
                break;
            case R.id.action_calculate_experience_pkmn2:
                Intent expCalcIntent2 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent2.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, mPokemon2.toMiniPokemon());
                startActivity(expCalcIntent2);
                break;
            case R.id.action_play_cry_pkmn1:
                ActionUtils.playPokemonCry(this, mPokemon1);
                break;
            case R.id.action_play_cry_pkmn2:
                ActionUtils.playPokemonCry(this, mPokemon2);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new MovesFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.tab_pkmn_detail_general).toUpperCase(l);
                case 1:
                    return getString(R.string.tab_pkmn_detail_moves).toUpperCase(l);
            }
            return null;
        }
    }


    public static class MainFragment extends Fragment {

        private View mRootView;

        private RecyclerView mRecyclerView;
        private ArrayList<DetailInfo> mDetails;

        private AsyncTask<Void, Integer, Void> mCurrAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_main, container, false);

            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.detail_columns)));

            mDetails = new ArrayList<>();

            Toolbar secondaryToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar_secondary);
            PokemonCompareDetail mainInfo = fetchGeneralData();
            mainInfo.setupCard(getActivity(), secondaryToolbar);

            mCurrAsyncTask = new AsyncTask<Void, Integer, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mDetails.add(fetchAbilityData());
                    mDetails.add(fetchAppearanceData());
                    mDetails.add(fetchGenderData());
                    mDetails.add(fetchStatData());
                    mDetails.add(fetchTrainingData());
                    mDetails.add(fetchPokedexData());
                    mDetails.add(fetchMoreData());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    // stop recycling of views (which causes a bug where views are merged together)
                    mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

                    // setup RecyclerView
                    DetailAdapter adapter = new DetailAdapter(getActivity(), mDetails);
                    mRecyclerView.setAdapter(adapter);
                }
            }.execute();

            return mRootView;
        }

        private PokemonCompareDetail fetchGeneralData() {
            ArrayList<ArrayList<?>> valueArrays = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                ArrayList<Object> valueArray = new ArrayList<>(7);

                valueArray.add(pokemon.toMiniPokemon());
                valueArray.add(Pokemon.isDefault(pokemon.getMiscValues()));
                valueArray.add(pokemon.getGenera().get("en") + " Pok\u00E9mon");
                valueArray.add(Pokemon.isFormMega(pokemon.getFormSpecificValues()));

                SparseIntArray pokemonTypeIds = pokemon.getTypeIds();

                valueArray.add(DataUtils.typeIdToString(pokemonTypeIds.get(1)));
                boolean hasSecondaryType = Pokemon.hasSecondaryType(pokemonTypeIds);
                valueArray.add(hasSecondaryType ? DataUtils.typeIdToString(pokemonTypeIds.get(2)) : null);
                valueArray.add(hasSecondaryType);

                valueArrays.add(valueArray);
            }

            return new PokemonCompareDetail(valueArrays);
        }

        private PokemonCompareDetail fetchAbilityData() {
            ArrayList<ArrayList<String>> propertiesArray = new ArrayList<>(2);
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                final SparseIntArray abilityIds = pokemon.getAbilityIds();
                final SparseArray<String> abilities = MiniAbility.findAbilityNames(getActivity(), abilityIds);

                ArrayList<String> properties = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    if (abilityIds.get(i+1) == 0) {
                        // contrary to DetailActivity, we add null values here as placeholders
                        properties.add(null);
                        values.add(null);
                        listeners.add(null);
                        continue;
                    }
                    int propertyText = 0;
                    switch (i) {
                        case 0:
                            propertyText = (Pokemon.hasSecondaryAbility(abilityIds)) ? R.string.attr_ability_1 : R.string.attr_ability;
                            break;
                        case 1:
                            propertyText = R.string.attr_ability_2;
                            break;
                        case 2:
                            propertyText = R.string.attr_ability_hidden;
                            break;
                    }
                    properties.add(getResources().getString(propertyText));
                    values.add(abilities.get(i));
                    final int j = i;
                    listeners.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), AbilityDetailActivity.class);
                            intent.putExtra(AbilityDetailActivity.EXTRA_ABILITY,
                                    new MiniAbility(abilityIds.get(j+1), abilities.get(j)));
                            startActivity(intent);
                        }
                    });
                }

                propertiesArray.add(properties);
                valuesArray.add(values);
                listenersArray.add(listeners);
            }

            ArrayList<String> pArr1 = propertiesArray.get(0);
            ArrayList<String> pArr2 = propertiesArray.get(1);

            ArrayList<String> finalProperties = new ArrayList<>();

            // now we check to see if two corresponding values are null
            if (pArr1.get(1) == null && pArr2.get(1) == null) {
                // if both 'Ability 2' s are null
                finalProperties.add(getResources().getString(R.string.attr_ability));
                for (ArrayList<?> valArr : valuesArray) valArr.remove(1);
                for (ArrayList<View.OnClickListener> lisArr : listenersArray) lisArr.remove(1);
            } else {
                finalProperties.add(getResources().getString(R.string.attr_ability_1));
                finalProperties.add(getResources().getString(R.string.attr_ability_2));
            }

            if (pArr1.get(2) == null && pArr2.get(2) == null) {
                // if both hidden abilities are null
                for (ArrayList<?> valArr : valuesArray) valArr.remove(2);
                for (ArrayList<View.OnClickListener> lisArr : listenersArray) lisArr.remove(2);
            } else {
                finalProperties.add(getResources().getString(R.string.attr_ability_hidden));
            }

            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_abilities, finalProperties, valuesArray);
            info.addOnClickListeners(listenersArray);
            return info;
        }

        private PokemonCompareDetail fetchAppearanceData() {
            Resources res = getResources();

            ArrayList<String> properties = new ArrayList<>(4);
            properties.add(res.getString(R.string.attr_height));
            properties.add(res.getString(R.string.attr_mass));
            properties.add(res.getString(R.string.attr_colour));
            properties.add(res.getString(R.string.attr_shape));

            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                final ArrayMap<String, Integer> physicalValues = pokemon.getPhysicalAttrs();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                values.add(Pokemon.getHeight(physicalValues) + " m");
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HEIGHT);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, String.valueOf(Pokemon.getHeight(physicalValues)));
                        startActivity(intent);
                    }
                });
                values.add(Pokemon.getWeight(physicalValues) + " kg");
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_MASS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, String.valueOf(Pokemon.getWeight(physicalValues)));
                        startActivity(intent);
                    }
                });
                final String color = DataUtils.colorIdToString(Pokemon.getColorId(physicalValues));
                values.add(color);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_COLOUR);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, color);
                        startActivity(intent);
                    }
                });
                values.add(DataUtils.shapeIdToString(Pokemon.getShapeId(physicalValues), true) +
                        " (" + DataUtils.shapeIdToString(Pokemon.getShapeId(physicalValues), false) + ")");
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_SHAPE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, DataUtils.shapeIdToString(Pokemon.getShapeId(physicalValues), false));
                        startActivity(intent);
                    }
                });

                valuesArray.add(values);
                listenersArray.add(listeners);
            }

            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_physical_attrs, properties, valuesArray);
            info.addOnClickListeners(listenersArray);
            return info;
        }

        private PokemonCompareDetail fetchGenderData() {
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                final ArrayMap<String, Integer> genderValues = pokemon.getGenderValues();
                ArrayList<Integer> values = new ArrayList<>();
                values.add(Pokemon.getGenderRate(genderValues));

                valuesArray.add(values);
            }

            return new PokemonCompareDetail(valuesArray, new String[] {mPkmnName1, mPkmnName2});
        }

        private PokemonCompareDetail fetchStatData() {
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                final ArrayMap<String, Integer> statValues = pokemon.getStats();
                ArrayList<Integer> values = new ArrayList<>();

                String[] keys = new String[] {"hp", "atk", "def", "spa", "spd", "spe"};
                for (String key : keys) {
                    values.add(statValues.get(key));
                }

                valuesArray.add(values);
            }

            return new PokemonCompareDetail(valuesArray, false);
        }

        private PokemonCompareDetail fetchTrainingData() {
            Resources res = getResources();

            ArrayList<String> properties = new ArrayList<>(4);
            properties.add(res.getString(R.string.attr_catch_rate));
            properties.add(res.getString(R.string.attr_base_happiness));
            properties.add(res.getString(R.string.attr_levelling_rate));
            properties.add(res.getString(R.string.attr_exp_growth_abbr));

            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                ArrayMap<String, Integer> trainingValues = pokemon.getTrainingValues();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                final String captureRate = String.valueOf(Pokemon.getCaptureRate(trainingValues));
                values.add(captureRate);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_CATCH_RATE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, captureRate);
                        startActivity(intent);
                    }
                });
                final String happiness = String.valueOf(Pokemon.getBaseHappiness(trainingValues));
                values.add(happiness);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HAPPINESS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, happiness);
                        startActivity(intent);
                    }
                });
                final String levellingRate = InfoUtils.idToGrowth(Pokemon.getGrowthRateId(trainingValues));
                values.add(levellingRate);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_LEVELLING_RATE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, levellingRate);
                        startActivity(intent);
                    }
                });
                final String exp = String.valueOf(DataUtils.maxExpFromGrowthId(Pokemon.getGrowthRateId(trainingValues)));
                values.add(exp);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EXP);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, exp);
                        startActivity(intent);
                    }
                });

                valuesArray.add(values);
                listenersArray.add(listeners);
            }


            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_training, properties, valuesArray);
            info.addOnClickListeners(listenersArray);
                    /*
                    info.addButton(new View.OnClickListener() { // TODO STOPSHIP FIXME
                        @Override
                        public void onClick(View v) {
                            goToExperienceCalculator(getActivity());
                        }
                    });
                    */

            return info;
        }

        private PokemonCompareDetail fetchPokedexData() {
            ArrayList<ArrayList<String>> propertiesArray = new ArrayList<>(2);
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            String[] pokedexKeys = Pokemon.POKEDEX_KEYS;

            for (Pokemon pokemon : mPokemonArray) {
                ArrayMap<String, Integer> pokedexValues = pokemon.getPokedexNumbers();
                ArrayList<String> properties = new ArrayList<>(pokedexValues.size());
                ArrayList<String> values = new ArrayList<>(pokedexValues.size());
                ArrayList<View.OnClickListener> listeners = new ArrayList<>(pokedexValues.size());

                for (int id = 1; id <= pokedexValues.size(); id++) {
                    if (id == 10 || id == 11) {
                        properties.add(null);
                        values.add(null);
                        listeners.add(null);
                        continue;  // id 10 doesn't exist and id 11 is conquest series
                    }

                    String pokedexKey = pokedexKeys[id-1];
                    if (!Pokemon.hasPokedexNumber(pokedexValues, pokedexKey)) {
                        properties.add(null);
                        values.add(null);
                        listeners.add(null);
                        continue;
                    }

                    properties.add(DataUtils.getPokedexNameFromId(id));
                    values.add(InfoUtils.formatPokemonId(pokedexValues.get(pokedexKey)));
                    listeners.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),
                                    "Information about Pokedex numbers will be added soon.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                propertiesArray.add(properties);
                valuesArray.add(values);
                listenersArray.add(listeners);
            }

            ArrayList<String> pArr1 = propertiesArray.get(0);
            ArrayList<String> pArr2 = propertiesArray.get(1);

            ArrayList<String> finalProperties = new ArrayList<>();

            for (int i = 0; i < pokedexKeys.length; i++) {
                if (pArr1.get(i) != null || pArr2.get(i) != null) { // TODO simplify selection here?
                    if (pArr1.get(i) != null && pArr2.get(i) == null) {
                        finalProperties.add(pArr1.get(i));
                        valuesArray.get(1).set(i, null);
                        // listeners array should already be null in the corresponding position
                    } else if (pArr1.get(i) == null && pArr2.get(i) != null) {
                        finalProperties.add(pArr2.get(i));
                        valuesArray.get(0).set(i, null);
                    } else if (pArr1.get(i) != null && pArr2.get(i) != null) {
                        finalProperties.add(pArr1.get(i));
                    }
                }
            }

            // remove null values that are present in both corresponding lists
            int i = 0;
            while (true) {
                if (i == valuesArray.get(0).size()) break;

                if (valuesArray.get(0).get(i) == null && valuesArray.get(1).get(i) == null) {
                    for (ArrayList<?> values : valuesArray) values.remove(i);
                    for (ArrayList<?> listeners : listenersArray) listeners.remove(i);
                } else {
                    i++;
                }
            }

            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_pokedex_numbers, finalProperties, valuesArray);
            info.addOnClickListeners(listenersArray);
            return info;
        }

        private PokemonCompareDetail fetchMoreData() {
            Resources res = getResources();

            ArrayList<String> properties = new ArrayList<>(4);
            properties.add(res.getString(R.string.attr_generation));
            properties.add(res.getString(R.string.attr_base_egg_steps));
            properties.add(res.getString(R.string.attr_base_egg_cycles));
            properties.add(res.getString(R.string.attr_habitat));

            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : mPokemonArray) {
                ArrayMap<String, Integer> moreValues = pokemon.getMoreValues();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                final String generation = InfoUtils.getRomanFromGen(Pokemon.getGenerationId(moreValues));
                values.add(generation);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_GENERATION);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, generation);
                        startActivity(intent);
                    }
                });
                final String eggSteps = String.valueOf(Pokemon.getBaseEggSteps(moreValues));
                values.add(eggSteps);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EGG_STEPS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, eggSteps);
                        startActivity(intent);
                    }
                });
                final String eggCycles = String.valueOf(Pokemon.getBaseEggCycles(moreValues));
                values.add(eggCycles);
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EGG_CYCLES);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, eggCycles);
                        startActivity(intent);
                    }
                });

                if (Pokemon.hasHabitatInfo(moreValues)) {
                    final String habitat = DataUtils.habitatIdToString(Pokemon.getHabitatId(moreValues));
                    values.add(habitat);
                    listeners.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                            intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HABITAT);
                            intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, habitat);
                            startActivity(intent);
                        }
                    });
                } else {
                    values.add(null);
                    listeners.add(null);
                }

                valuesArray.add(values);
                listenersArray.add(listeners);
            }

            if (valuesArray.get(0).get(3) == null && valuesArray.get(1).get(3) == null) {
                // neither pokemon has a habitat
                properties.remove(3);
                for (ArrayList<?> values : valuesArray) values.remove(3);
                for (ArrayList<View.OnClickListener> listeners : listenersArray) listeners.remove(3);
            }

            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_more_information, properties, valuesArray);
            info.addOnClickListeners(listenersArray);
            return info;
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mCurrAsyncTask != null) {
                mCurrAsyncTask.cancel(true);
            }
        }
    }

    public static class MovesFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

        private View mRootView;

        private boolean mSameLearnset;

        private String mLearnMethod, mGameVersion;

        private LinearLayout mContainer1, mContainer2;
        private Button mSubmitButton;
        private LabelledSpinner mSpinnerMethod, mSpinnerGame;

        private ArrayList<String> mArrayMethodTitles = new ArrayList<>();
        private ArrayList<Integer> mArrayMethodTypes = new ArrayList<>();
        private ArrayList<String> mArrayGameTitles;
        private ArrayList<Integer> mArrayGameTypes = new ArrayList<>();

        private AsyncTask<Void, Integer, Void> mAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_compare_learnsets, container, false);

            ArrayMap<String, Integer> formInfos1 = mPokemon1.getFormSpecificValues();
            ArrayMap<String, Integer> formInfos2 = mPokemon2.getFormSpecificValues();

            if (mPkmnName1.equals(mPkmnName2)) {
                if ((Pokemon.isFormDefault(formInfos1) && Pokemon.isFormMega(formInfos2)) ||
                        Pokemon.isFormMega(formInfos1) && Pokemon.isFormDefault(formInfos2)) {
                    mSameLearnset = true;
                }
            } else {
                mSameLearnset = false;
            }

            loadLearnsets();

            return mRootView;
        }

        private void loadLearnsets() {
            Resources res = getActivity().getResources();
            mArrayMethodTitles.add(res.getString(R.string.header_moves_levelup));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_machine));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_tutor));
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_LEVEL_UP);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_MACHINE);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_TUTOR);

            mArrayGameTitles = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.game_versions)));
            mArrayGameTypes.add(AppConfig.GAME_VERSION_RED_BLUE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_YELLOW);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_GOLD_SILVER);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_CRYSTAL);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_RUBY_SAPPHIRE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_EMERALD);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_FIRERED_LEAFGREEN);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_DIAMOND_PEARL);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_PLATINUM);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_HEARTGOLD_SOULSILVER);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_BLACK_WHITE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_BLACK2_WHITE2);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_X_Y); // TODO: add what actually needs to be added

            mSpinnerMethod = (LabelledSpinner) mRootView.findViewById(R.id.compareL_spinnerMethod);
            mSpinnerMethod.setItemsArray(mArrayMethodTitles);
            mSpinnerMethod.setSelection(0);
            mSpinnerMethod.setOnItemChosenListener(this);
            mSpinnerGame = (LabelledSpinner) mRootView.findViewById(R.id.compareL_spinnerGame);
            mSpinnerGame.setItemsArray(R.array.game_versions);
            mSpinnerGame.setSelection(mArrayGameTitles.size() - 1);
            mSpinnerGame.setOnItemChosenListener(this);

            mContainer1 = (LinearLayout) mRootView.findViewById(R.id.compareL_llContainer_1);
            mContainer2 = (LinearLayout) mRootView.findViewById(R.id.compareL_llContainer_2);

            mSubmitButton = (Button) mRootView.findViewById(R.id.compareL_btnGo);

            if (mSameLearnset) {
                mContainer2.setVisibility(View.GONE);
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAsyncTask != null) {
                            mAsyncTask.cancel(true);
                        }
                        loadCard(mContainer1, mPokemon1);
                    }
                });
            } else {
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAsyncTask != null) {
                            mAsyncTask.cancel(true);
                        }
                        loadCard(mContainer1, mPokemon1);
                        loadCard(mContainer2, mPokemon2);
                    }
                });
            }
        }

        private void loadCard(LinearLayout container, Pokemon pokemon) {
            container.removeAllViews();
            container.addView(makeCard(container, pokemon));
        }

        private View makeCard(LinearLayout container, final Pokemon pokemon) {
            View card = getActivity().getLayoutInflater().inflate(R.layout.card_detail_learnset, container, false);

            final TextView title = (TextView) card.findViewById(R.id.card_learnset_titleText);
            final TextView subtitle = (TextView) card.findViewById(R.id.card_learnset_subtitleText);
            final ProgressBar progressBar = (ProgressBar) card.findViewById(R.id.card_learnset_progressBar);
            final LinearLayout itemsContainer = (LinearLayout) card.findViewById(R.id.card_learnset_linearLayout);

            title.setText(mLearnMethod);
            subtitle.setText("Pok"+"\u00E9"+"mon " + mGameVersion);

            int methodNoInList = mArrayMethodTitles.indexOf(mLearnMethod);
            final int learnMethod = mArrayMethodTypes.get(methodNoInList);
            int gameNoInList = mArrayGameTitles.indexOf(mGameVersion);
            final int gameVersion = mArrayGameTypes.get(gameNoInList);

            mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                PokemonMovesVgAdapter adapter;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (progressBar.getVisibility() != View.VISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    PokemonMoves learnset = new PokemonMoves(getActivity(), pokemon, learnMethod);
                    ArrayList<PokemonMoves.PokemonMove> arrayMoves = learnset.getPokemonMoves();
                    Collections.sort(arrayMoves, new Comparator<PokemonMoves.PokemonMove>() {
                        @Override
                        public int compare(PokemonMoves.PokemonMove lhs, PokemonMoves.PokemonMove rhs) {
                            if (learnMethod == AppConfig.LEARN_METHOD_LEVEL_UP) {
                                return lhs.getLevel() - rhs.getLevel();
                            } else {
                                return lhs.getMoveId() - rhs.getMoveId();
                            }
                        }
                    });
                    final ArrayList<PokemonMoves.PokemonMove> arrayMovesFinal = arrayMoves;
                    adapter = new PokemonMovesVgAdapter(getActivity(), itemsContainer, arrayMoves);
                    adapter.setOnEntryClickListener(new PokemonMovesVgAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), MovesDetailActivity.class);
                            intent.putExtra(MovesDetailActivity.EXTRA_MOVE,
                                    arrayMovesFinal.get(position).toMiniMove(getActivity()));
                            startActivity(intent);
                        }
                    });
                    return null;
                }
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    adapter.createListItems();
                    progressBar.setVisibility(View.GONE);
                }
            }.execute();

            return card;
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
        }

        @Override
        public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
            String selected = adapterView.getItemAtPosition(position).toString();
            switch (labelledSpinner.getId()) {
                case R.id.compareL_spinnerMethod:
                    mLearnMethod = selected;
                    break;
                case R.id.compareL_spinnerGame:
                    mGameVersion = selected;
                    break;
            }
        }

        @Override
        public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
    }

}
