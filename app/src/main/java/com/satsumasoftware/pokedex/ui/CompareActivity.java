package com.satsumasoftware.pokedex.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.framework.Color;
import com.satsumasoftware.pokedex.framework.EggGroup;
import com.satsumasoftware.pokedex.framework.GrowthRate;
import com.satsumasoftware.pokedex.framework.Habitat;
import com.satsumasoftware.pokedex.framework.HatchCounter;
import com.satsumasoftware.pokedex.framework.Height;
import com.satsumasoftware.pokedex.framework.Mass;
import com.satsumasoftware.pokedex.framework.MoveMethod;
import com.satsumasoftware.pokedex.framework.Pokedex;
import com.satsumasoftware.pokedex.framework.Shape;
import com.satsumasoftware.pokedex.framework.Type;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonLearnset;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonMove;
import com.satsumasoftware.pokedex.framework.version.VersionGroup;
import com.satsumasoftware.pokedex.ui.adapter.DetailAdapter;
import com.satsumasoftware.pokedex.ui.adapter.PokemonMovesComparisonAdapter;
import com.satsumasoftware.pokedex.ui.card.DetailCard;
import com.satsumasoftware.pokedex.ui.card.PokemonCompareDetail;
import com.satsumasoftware.pokedex.ui.dialog.AbilityDetailActivity;
import com.satsumasoftware.pokedex.ui.dialog.MoveDetailActivity;
import com.satsumasoftware.pokedex.ui.dialog.PropertyDetailActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CompareActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON_1 = "POKEMON_1";
    public static final String EXTRA_POKEMON_2 = "POKEMON_2";

    private static Pokemon sPokemon1, sPokemon2;
    private static Pokemon[] sPokemonArray;


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

        sPokemon1 = miniPokemon1.toPokemon(this);
        sPokemon2 = miniPokemon2.toPokemon(this);
        sPokemonArray = new Pokemon[] {sPokemon1, sPokemon2};

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.mdu_text_white_secondary),
                ContextCompat.getColor(this, R.color.mdu_text_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.mdu_white));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        getSupportActionBar().setSubtitle(sPokemon1.getFormAndPokemonName() + " vs. " + sPokemon2.getFormAndPokemonName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compare, menu);

        String fullName1 = sPokemon1.getFormAndPokemonName();
        String fullName2 = sPokemon2.getFormAndPokemonName();

        menu.findItem(R.id.action_calculate_experience_pkmn1)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, fullName1));
        menu.findItem(R.id.action_calculate_experience_pkmn2)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, fullName2));

        menu.findItem(R.id.action_play_cry_pkmn1)
                .setTitle(getResources().getString(R.string.action_play_cry_of, fullName1));
        menu.findItem(R.id.action_play_cry_pkmn2)
                .setTitle(getResources().getString(R.string.action_play_cry_of, fullName2));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculate_experience_pkmn1:
                Intent expCalcIntent1 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent1.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, sPokemon1.toMiniPokemon());
                startActivity(expCalcIntent1);
                break;
            case R.id.action_calculate_experience_pkmn2:
                Intent expCalcIntent2 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent2.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, sPokemon2.toMiniPokemon());
                startActivity(expCalcIntent2);
                break;
            case R.id.action_play_cry_pkmn1:
                ActionUtils.playPokemonCry(this, sPokemon1);
                break;
            case R.id.action_play_cry_pkmn2:
                ActionUtils.playPokemonCry(this, sPokemon2);
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
        private ArrayList<DetailCard> mDetails;

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

            for (Pokemon pokemon : sPokemonArray) {
                ArrayList<Object> valueArray = new ArrayList<>(7);

                valueArray.add(pokemon.toMiniPokemon());
                valueArray.add(Pokemon.isDefault(pokemon.getMiscValues()));
                valueArray.add(pokemon.getGenera().get("en") + " Pok\u00E9mon");
                valueArray.add(Pokemon.isFormMega(pokemon.getFormSpecificValues()));

                SparseIntArray pokemonTypeIds = pokemon.getTypeIds();

                valueArray.add(new Type(pokemonTypeIds.get(1)).getName());
                boolean hasSecondaryType = Pokemon.hasSecondaryType(pokemonTypeIds);
                valueArray.add(hasSecondaryType ? new Type(pokemonTypeIds.get(2)).getName() : null);
                valueArray.add(hasSecondaryType);

                valueArrays.add(valueArray);
            }

            return new PokemonCompareDetail(valueArrays);
        }

        private PokemonCompareDetail fetchAbilityData() {
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : sPokemonArray) {
                final SparseIntArray abilityIds = pokemon.getAbilityIds();
                SparseArray<MiniAbility> abilities = new SparseArray<>(3);
                for (int i = 1; i < abilityIds.size() + 1; i++) {
                    int id = abilityIds.get(i);
                    MiniAbility miniAbility = (id == DataUtilsKt.NULL_INT) ?
                            null : new MiniAbility(getActivity(), id);
                    abilities.put(i, miniAbility);
                }
                final SparseArray<MiniAbility> finalAbilities = abilities;

                ArrayList<String> values = new ArrayList<>(3);
                ArrayList<View.OnClickListener> listeners = new ArrayList<>(3);

                for (int i = 0; i < 3; i++) {
                    Log.d("DetailActivity", "For loop i = " + i);
                    if (finalAbilities.get(i + 1) == null) {
                        Log.d("DetailActivity", "Continuing to next part of loop");
                        values.add(null);
                        listeners.add(null);
                        continue;
                    }
                    Log.d("DetailActivity", "Ability name: " + finalAbilities.get(i + 1).getName());
                    values.add(finalAbilities.get(i + 1).getName());
                    final int j = i;
                    listeners.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), AbilityDetailActivity.class);
                            intent.putExtra(AbilityDetailActivity.EXTRA_ABILITY, finalAbilities.get(j + 1));
                            startActivity(intent);
                        }
                    });
                }

                valuesArray.add(values);
                listenersArray.add(listeners);
            }

            ArrayList<String> propertiesArray = new ArrayList<>(3);

            boolean bothAbility2sNull = (valuesArray.get(0).get(1) == null && valuesArray.get(1).get(1) == null);
            propertiesArray.add(bothAbility2sNull ?
                    getResources().getString(R.string.attr_ability) :
                    getResources().getString(R.string.attr_ability_1));
            propertiesArray.add(getResources().getString(R.string.attr_ability_2));
            propertiesArray.add(getResources().getString(R.string.attr_ability_hidden));

            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_abilities, propertiesArray, valuesArray);
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

            for (Pokemon pokemon : sPokemonArray) {
                final ArrayMap<String, Integer> physicalValues = pokemon.getPhysicalAttrs();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                final Height height = new Height(Pokemon.getHeightValue(physicalValues));
                values.add(height.getDisplayedText(getActivity()));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HEIGHT);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, height.getDbValue());
                        startActivity(intent);
                    }
                });

                final Mass mass = new Mass(Pokemon.getWeight(physicalValues));
                values.add(mass.getDisplayedText(getActivity()));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_MASS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, mass.getDbValue());
                        startActivity(intent);
                    }
                });
                final Color color = new Color(Pokemon.getColorId(physicalValues));
                values.add(color.getName());
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_COLOUR);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, color.getId());
                        startActivity(intent);
                    }
                });

                final Shape shape = new Shape(Pokemon.getShapeId(physicalValues));
                values.add(shape.getTechnicalName() + " (" + shape.getSimpleName() + ")");
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_SHAPE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, shape.getId());
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

            for (Pokemon pokemon : sPokemonArray) {
                final ArrayMap<String, Integer> genderValues = pokemon.getGenderValues();
                ArrayList<Integer> values = new ArrayList<>();
                values.add(Pokemon.getGenderRate(genderValues));

                valuesArray.add(values);
            }

            return new PokemonCompareDetail(valuesArray, new String[] {sPokemon1.getName(), sPokemon2.getName()});
        }

        private PokemonCompareDetail fetchStatData() {
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);

            for (Pokemon pokemon : sPokemonArray) {
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

            for (Pokemon pokemon : sPokemonArray) {
                ArrayMap<String, Integer> trainingValues = pokemon.getTrainingValues();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                final int captureRate = Pokemon.getCaptureRate(trainingValues);
                values.add(String.valueOf(captureRate));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_CATCH_RATE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, captureRate);
                        startActivity(intent);
                    }
                });
                final int happiness = Pokemon.getBaseHappiness(trainingValues);
                values.add(String.valueOf(happiness));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HAPPINESS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, happiness);
                        startActivity(intent);
                    }
                });

                final GrowthRate growthRate = new GrowthRate(Pokemon.getGrowthRateId(trainingValues));

                values.add(growthRate.getName());
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_LEVELLING_RATE);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, growthRate.getId());
                        startActivity(intent);
                    }
                });
                values.add(String.valueOf(growthRate.findMaxExperience()));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EXP);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, growthRate.getId());
                        startActivity(intent);
                    }
                });

                valuesArray.add(values);
                listenersArray.add(listeners);
            }


            PokemonCompareDetail info = new PokemonCompareDetail(R.string.header_training, properties, valuesArray);
            info.addOnClickListeners(listenersArray);

            return info;
        }

        private PokemonCompareDetail fetchPokedexData() {
            ArrayList<ArrayList<String>> propertiesArray = new ArrayList<>(2);
            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            String[] pokedexKeys = Pokemon.POKEDEX_KEYS;

            for (Pokemon pokemon : sPokemonArray) {
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

                    properties.add(new Pokedex(id).getName());
                    values.add(DataUtilsKt.formatPokemonId(pokedexValues.get(pokedexKey)));
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

            boolean hasTwoEggGroupRows = Pokemon.hasTwoEggGroups(sPokemon1.getEggGroupIds()) ||
                    Pokemon.hasTwoEggGroups(sPokemon2.getEggGroupIds());
            if (hasTwoEggGroupRows) {
                properties.add(res.getString(R.string.attr_egg_group_1));
                properties.add(res.getString(R.string.attr_egg_group_2));
            } else {
                properties.add(res.getString(R.string.attr_egg_group));
            }

            properties.add(res.getString(R.string.attr_habitat));

            ArrayList<ArrayList<?>> valuesArray = new ArrayList<>(2);
            ArrayList<ArrayList<View.OnClickListener>> listenersArray = new ArrayList<>(2);

            for (Pokemon pokemon : sPokemonArray) {
                ArrayMap<String, Integer> moreValues = pokemon.getMoreValues();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<View.OnClickListener> listeners = new ArrayList<>();

                final int generationId = Pokemon.getGenerationId(moreValues);
                values.add(DataUtilsKt.genIdToRoman(generationId));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_GENERATION);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, generationId);
                        startActivity(intent);
                    }
                });

                final HatchCounter hatchCounter = new HatchCounter(Pokemon.getHatchCounter(moreValues));

                values.add(String.valueOf(hatchCounter.getEggSteps()));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EGG_STEPS);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, hatchCounter.getDbValue());
                        startActivity(intent);
                    }
                });

                values.add(String.valueOf(hatchCounter.getEggCycles()));
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                        intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_EGG_CYCLES);
                        intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, hatchCounter.getDbValue());
                        startActivity(intent);
                    }
                });

                SparseIntArray eggGroupIds = pokemon.getEggGroupIds();
                values.add(new EggGroup(eggGroupIds.get(1)).getName());

                View.OnClickListener temporaryToast = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Information will be added soon", Toast.LENGTH_SHORT).show();
                    }
                };

                listeners.add(temporaryToast);

                if (hasTwoEggGroupRows) {
                    if (Pokemon.hasTwoEggGroups(eggGroupIds)) {
                        values.add(new EggGroup(eggGroupIds.get(2)).getName());
                        listeners.add(temporaryToast);
                    } else {
                        values.add("-");
                        listeners.add(null);
                    }
                }

                if (Pokemon.hasHabitatInfo(moreValues)) {
                    final Habitat habitat = new Habitat(Pokemon.getHabitatId(moreValues));
                    values.add(habitat.getName());
                    listeners.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                            intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HABITAT);
                            intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, habitat.getId());
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

        private LinearLayout mContainer;
        private Button mSubmitButton;
        private LabelledSpinner mSpinnerMethod, mSpinnerGame;

        private ArrayList<MoveMethod> mMoveMethods;
        private ArrayList<String> mMoveMethodNames;
        private int mMoveMethodListPos;

        private ArrayList<VersionGroup> mVersionGroups;
        private ArrayList<String> mVersionGroupNames;
        private int mVGroupListPos;

        private AsyncTask<Void, Integer, Void> mAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_compare_learnsets, container, false);

            loadLearnsets();

            return mRootView;
        }

        private void loadLearnsets() {
            PokeDB pokeDB = PokeDB.getInstance(getActivity());

            mMoveMethods = new ArrayList<>();
            mMoveMethodNames = new ArrayList<>();
            Cursor methodCursor = pokeDB.getReadableDatabase().query(
                    PokeDB.PokemonMoveMethodProse.TABLE_NAME,
                    new String[] {PokeDB.PokemonMoveMethodProse.COL_LOCAL_LANGUAGE_ID,
                            PokeDB.PokemonMoveMethodProse.COL_POKEMON_MOVE_METHOD_ID},
                    PokeDB.PokemonMoveMethodProse.COL_LOCAL_LANGUAGE_ID + "= 9 AND " +
                            PokeDB.PokemonMoveMethodProse.COL_POKEMON_MOVE_METHOD_ID + "<= 4",
                    null, null, null, null);
            methodCursor.moveToFirst();
            while (!methodCursor.isAfterLast()) {
                int id = methodCursor.getInt(methodCursor.getColumnIndex(
                        PokeDB.PokemonMoveMethodProse.COL_POKEMON_MOVE_METHOD_ID));
                MoveMethod moveMethod = new MoveMethod(getActivity(), id);
                mMoveMethods.add(moveMethod);
                mMoveMethodNames.add(moveMethod.getName());
                methodCursor.moveToNext();
            }
            methodCursor.close();

            int gen1Id = Pokemon.getGenerationId(sPokemon1.getMoreValues());
            int gen2Id = Pokemon.getGenerationId(sPokemon2.getMoreValues());
            mVersionGroups = new ArrayList<>();
            mVersionGroupNames = new ArrayList<>();
            Cursor cursor = pokeDB.getReadableDatabase().query(
                    PokeDB.VersionGroups.TABLE_NAME,
                    null,
                    PokeDB.VersionGroups.COL_ID + "!=? AND " +
                            PokeDB.VersionGroups.COL_ID + "!=? AND " +
                            "(" + PokeDB.VersionGroups.COL_GENERATION_ID + ">=? OR " +
                            PokeDB.VersionGroups.COL_GENERATION_ID + ">=? )",
                    new String[] {String.valueOf(12), String.valueOf(13),  // exclude XD and Colosseum
                            String.valueOf(gen1Id), String.valueOf(gen2Id)},
                    null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                VersionGroup versionGroup = new VersionGroup(getActivity(), cursor);
                mVersionGroups.add(versionGroup);
                mVersionGroupNames.add(versionGroup.getName());
                cursor.moveToNext();
            }
            cursor.close();


            mSpinnerMethod = (LabelledSpinner) mRootView.findViewById(R.id.spinner_learn_method);
            mSpinnerMethod.setItemsArray(mMoveMethodNames);
            mSpinnerMethod.setSelection(0);
            mSpinnerMethod.setOnItemChosenListener(this);
            mSpinnerGame = (LabelledSpinner) mRootView.findViewById(R.id.spinner_game_version);
            mSpinnerGame.setItemsArray(mVersionGroupNames);
            mSpinnerGame.setSelection(mVersionGroups.size() - 1);
            mSpinnerGame.setOnItemChosenListener(this);

            mContainer = (LinearLayout) mRootView.findViewById(R.id.container);

            mSubmitButton = (Button) mRootView.findViewById(R.id.button_go);
            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAsyncTask != null) {
                        mAsyncTask.cancel(true);
                    }
                    loadCard();
                }
            });
        }

        private void loadCard() {
            mContainer.removeAllViews();
            mContainer.addView(makeCard());
        }

        private View makeCard() {
            View card = getActivity().getLayoutInflater().inflate(R.layout.card_detail_learnset, mContainer, false);

            final TextView title = (TextView) card.findViewById(R.id.title);
            final TextView subtitle = (TextView) card.findViewById(R.id.subtitle);
            final ProgressBar progressBar = (ProgressBar) card.findViewById(R.id.progress_indeterminate);
            final RecyclerView recyclerView = (RecyclerView) card.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity(), DividerItemDecoration.VERTICAL_LIST));

            title.setText(mMoveMethodNames.get(mMoveMethodListPos));
            subtitle.setText("Pok"+"\u00E9"+"mon " + mVersionGroupNames.get(mVGroupListPos));

            final MoveMethod learnMethod = mMoveMethods.get(mMoveMethodListPos);
            final VersionGroup versionGroup = mVersionGroups.get(mVGroupListPos);

            mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                PokemonMovesComparisonAdapter adapter;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (progressBar.getVisibility() != View.VISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    PokemonLearnset learnset1 = new PokemonLearnset(
                            getActivity(), sPokemon1.getId(), learnMethod.getId(), versionGroup.getId());
                    PokemonLearnset learnset2 = new PokemonLearnset(
                            getActivity(), sPokemon2.getId(), learnMethod.getId(), versionGroup.getId());

                    ArrayList<PokemonMove> arrayMoves1 = learnset1.getPokemonMoves();
                    ArrayList<PokemonMove> arrayMoves2 = learnset2.getPokemonMoves();
                    Comparator<PokemonMove> learnsetComparator = new Comparator<PokemonMove>() {
                        @Override
                        public int compare(PokemonMove lhs, PokemonMove rhs) {
                            if (learnMethod.isLevelUpMethod()) {
                                return lhs.getLevel() - rhs.getLevel();
                            } else {
                                return lhs.getMoveId() - rhs.getMoveId();
                            }
                        }
                    };
                    Collections.sort(arrayMoves1, learnsetComparator);
                    Collections.sort(arrayMoves2, learnsetComparator);

                    final List<Pair<Integer, PokemonMove[]>> pokemonMoves =
                            getCombinedMoves(learnMethod, arrayMoves1, arrayMoves2);

                    adapter = new PokemonMovesComparisonAdapter(
                            getActivity(), pokemonMoves, learnMethod);

                    adapter.setOnEntryClickListener(new PokemonMovesComparisonAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int position) {
                            Pair<Integer, PokemonMove[]> pair = pokemonMoves.get(position);
                            final PokemonMove[] moveArray = pair.second;

                            PokemonMove chosenMove;

                            if (moveArray[0] != null && moveArray[1] == null) {
                                chosenMove = moveArray[0];

                            } else if (moveArray[0] == null && moveArray[1] != null) {
                                chosenMove = moveArray[1];

                            } else {
                                // both are not null
                                chosenMove = moveArray[0];
                            }

                            Intent intent = new Intent(getActivity(), MoveDetailActivity.class);
                            intent.putExtra(MoveDetailActivity.EXTRA_MOVE,
                                    chosenMove.toMiniMove(getActivity()));
                            startActivity(intent);
                        }
                    });
                    return null;
                }
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }.execute();

            return card;
        }

        public static List<Pair<Integer, PokemonMove[]>> getCombinedMoves(MoveMethod learnMethod,
                                                                          ArrayList<PokemonMove> sortedMoves1,
                                                                          ArrayList<PokemonMove> sortedMoves2) {
            List<Pair<Integer, PokemonMove[]>> combinedMoves = new ArrayList<>();

            if (!learnMethod.isLevelUpMethod()) {
                while (!sortedMoves1.isEmpty() || !sortedMoves2.isEmpty()) {
                    PokemonMove valuePut1 = null;
                    PokemonMove valuePut2 = null;

                    if (!sortedMoves1.isEmpty()) {
                        valuePut1 = sortedMoves1.get(0);
                        sortedMoves1.remove(0);
                    }

                    if (!sortedMoves2.isEmpty()) {
                        valuePut2 = sortedMoves2.get(0);
                        sortedMoves2.remove(0);
                    }

                    Log.d("COMAPRE", "added item");

                    combinedMoves.add(new Pair<>(
                            0,
                            new PokemonMove[] {valuePut1, valuePut2}
                    ));
                }

                return combinedMoves;
            }

            int i;
            while (!sortedMoves1.isEmpty() || !sortedMoves2.isEmpty()) {

                // list 1 is okay, but 2 is empty
                if (!sortedMoves1.isEmpty() && sortedMoves2.isEmpty()) {
                    PokemonMove move1 = sortedMoves1.get(0);
                    combinedMoves.add(new Pair<>(
                            move1.getLevel(),
                            new PokemonMove[] {move1, null}
                    ));
                    sortedMoves1.remove(0);
                    continue;
                }

                // list 2 is okay, but 1 is empty
                if (sortedMoves1.isEmpty() && !sortedMoves2.isEmpty()) {
                    PokemonMove move2 = sortedMoves2.get(0);
                    combinedMoves.add(new Pair<>(
                            move2.getLevel(),
                            new PokemonMove[] {null, move2}
                    ));
                    sortedMoves2.remove(0);
                    continue;
                }

                // both lists are okay

                PokemonMove move1 = sortedMoves1.get(0);
                PokemonMove move2 = sortedMoves2.get(0);

                if (move1.getLevel() == move2.getLevel()
                        || move1.getLevel() < move2.getLevel()) {
                    i = move1.getLevel();
                } else {
                    i = move2.getLevel();
                }

                // if none of the moves are learnt at level 'i'
                if (move1.getLevel() != i && move2.getLevel() != i) {
                    continue;
                }

                PokemonMove valuePut1 = null;
                PokemonMove valuePut2 = null;

                if (move1.getLevel() == i) {
                    valuePut1 = move1;
                    sortedMoves1.remove(0);
                }
                if (move2.getLevel() == i) {
                    valuePut2 = move2;
                    sortedMoves2.remove(0);
                }

                combinedMoves.add(new Pair<>(
                        i,
                        new PokemonMove[] {valuePut1, valuePut2}
                ));
            }

            return combinedMoves;
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
            switch (labelledSpinner.getId()) {
                case R.id.spinner_learn_method:
                    mMoveMethodListPos = position;
                    break;
                case R.id.spinner_game_version:
                    mVGroupListPos = position;
                    break;
            }
        }

        @Override
        public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
    }

}
