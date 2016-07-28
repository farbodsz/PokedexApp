package com.satsumasoftware.pokedex.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonForm;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonLearnset;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonMove;
import com.satsumasoftware.pokedex.framework.version.VersionGroup;
import com.satsumasoftware.pokedex.ui.adapter.DetailAdapter;
import com.satsumasoftware.pokedex.ui.adapter.EvolutionsAdapter;
import com.satsumasoftware.pokedex.ui.adapter.FormsTileAdapter;
import com.satsumasoftware.pokedex.ui.adapter.FormsVGAdapter;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.ui.adapter.PokemonMovesAdapter;
import com.satsumasoftware.pokedex.ui.card.DetailCard;
import com.satsumasoftware.pokedex.ui.card.PokemonDetail;
import com.satsumasoftware.pokedex.ui.dialog.AbilityDetailActivity;
import com.satsumasoftware.pokedex.ui.dialog.MoveDetailActivity;
import com.satsumasoftware.pokedex.ui.dialog.PropertyDetailActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.AppConfig;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsumasoftware.pokedex.util.FavoriteUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.satsumasoftware.pokedex.util.ThemeUtilsKt;
import com.satsuware.usefulviews.LabelledSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON = "POKEMON";

    private View mRootLayout;

    private static ViewPager sViewPager;

    private static Pokemon sPokemon;

    private static SparseIntArray sPkmnTypeIds;
    private static ArrayMap<String, Integer> sPkmnPhysicalAttrs;

    private MenuItem mMenuItemFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MiniPokemon miniPokemon = getIntent().getExtras().getParcelable(EXTRA_POKEMON);
        if (miniPokemon == null) {
            throw new NullPointerException("Parcelable BasePokemon object through Intent is null");
        }

        sPokemon = miniPokemon.toPokemon(this);

        if (sPokemon == null) {
            throw new NullPointerException("Pokemon is null");
        }

        sPkmnTypeIds = sPokemon.getTypeIds();
        sPkmnPhysicalAttrs = sPokemon.getPhysicalAttrs();

        switch (PrefUtils.detailColorType(this)) {
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_DEFAULT:
                // Do nothing
                break;
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_TYPE:
                ThemeUtilsKt.colourDetailByType(this, sPkmnTypeIds.get(1));
                break;
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_COLOUR:
                ThemeUtilsKt.colourDetailByColour(this, Pokemon.getColorId(sPkmnPhysicalAttrs));
                break;
        }
        setContentView(R.layout.activity_detail);
        mRootLayout = findViewById(R.id.rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AdUtils.setupAds(this, R.id.adView);

        sViewPager = (ViewPager) findViewById(R.id.viewPager);
        sViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.mdu_text_white_secondary),
                ContextCompat.getColor(this, R.color.mdu_text_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.mdu_white));
        tabLayout.setupWithViewPager(sViewPager);
        sViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(sViewPager);
        sViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if (PrefUtils.playPokemonCryAtStart(this)) {
            ActionUtils.playPokemonCry(this, sPokemon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem previous = menu.findItem(R.id.action_previous);
        MenuItem next = menu.findItem(R.id.action_next);
        MenuItem compare = menu.findItem(R.id.action_compare);
        mMenuItemFavourite = menu.findItem(R.id.action_favourite);

        if (Flavours.type == Flavours.Type.FREE) {
            previous.setIcon(R.drawable.ic_chevron_left_grey600_24dp);
            next.setIcon(R.drawable.ic_chevron_right_grey600_24dp);
            compare.setIcon(R.drawable.ic_compare_grey600_24dp);
            mMenuItemFavourite.setIcon(R.drawable.ic_star_border_grey600_24dp);
            return true;
        }

        switch (sPokemon.getId()) {
            case 1:
                previous.setIcon(R.drawable.ic_chevron_left_grey600_24dp);
                break;
            case AppConfig.MAX_NATIONAL_ID:
                next.setIcon(R.drawable.ic_chevron_right_grey600_24dp);
                break;
        }

        if (FavoriteUtils.isAFavouritePkmn(this, sPokemon.toMiniPokemon())) {
            mMenuItemFavourite.setIcon(R.drawable.ic_star_white_24dp);
            mMenuItemFavourite.setTitle(R.string.action_favourite_remove);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                if (Flavours.type == Flavours.Type.PAID) {
                    if (sPokemon.getId() != 1) {
                        action_navigation(0);
                    } else {
                        Snackbar.make(mRootLayout, R.string.detail_nav_back_error, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_next:
                if (Flavours.type == Flavours.Type.PAID) {
                    if (sPokemon.getId() != AppConfig.MAX_NATIONAL_ID) {
                        action_navigation(1);
                    } else {
                        Snackbar.make(mRootLayout, R.string.detail_nav_forward_error, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_compare:
                if (Flavours.type == Flavours.Type.PAID) {
                    action_compare_chooser();
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_favourite:
                if (Flavours.type == Flavours.Type.PAID) {
                    FavoriteUtils.markAsFavouritePkmn(this, sPokemon.toMiniPokemon(), mRootLayout);
                    if (FavoriteUtils.isAFavouritePkmn(this, sPokemon.toMiniPokemon())) {
                        mMenuItemFavourite.setIcon(R.drawable.ic_star_white_24dp);
                        mMenuItemFavourite.setTitle(R.string.action_favourite_remove);
                    } else {
                        mMenuItemFavourite.setIcon(R.drawable.ic_star_border_white_24dp);
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_calculate_experience:
                goToExperienceCalculator(this);
                break;
            case R.id.action_play_cry:
                ActionUtils.playPokemonCry(this, sPokemon);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void action_navigation(int navigationType) {
        int pos = sPokemon.getNationalDexNumber();

        int newPos;
        if (navigationType == 0) {
            // Previous
            newPos = pos - 1;
        } else {
            // Next
            newPos = pos + 1;
        }

        PokemonDBHelper helper = new PokemonDBHelper(this);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                MiniPokemon.DB_COLUMNS,
                PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=?",
                new String[] {String.valueOf(newPos), String.valueOf(1)},
                null, null, null);
        cursor.moveToFirst();
        MiniPokemon nextPkmn = new MiniPokemon(cursor);
        cursor.close();

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_POKEMON, nextPkmn);
        startActivity(intent);
        finish();
    }

    private void action_compare_chooser() {
        if (Pokemon.hasSwitchableForms(sPokemon.getFormSpecificValues())) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_compare_pokemon_forms)
                    .setMessage(R.string.dialog_msg_compare_pokemon_forms)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            action_compare(true);
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            action_compare(false);
                        }
                    })
                    .setNeutralButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .show();
        } else {
            action_compare(false);
        }
    }

    private void action_compare(boolean compareToForm) {
        String title = getResources().getString(R.string.dialog_title_compare_pokemon);
        LayoutInflater inflater = getLayoutInflater();
        View listPicker = inflater.inflate(R.layout.dialog_list, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(listPicker)
                .setTitle(title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final ArrayList<MiniPokemon> arrayPokemon;
        PokemonDBHelper dbHelper = new PokemonDBHelper(this);
        if (compareToForm) {
            arrayPokemon = new ArrayList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(
                    PokemonDBHelper.TABLE_NAME,
                    MiniPokemon.DB_COLUMNS,
                    PokemonDBHelper.COL_SPECIES_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=? AND " + PokemonDBHelper.COL_FORM_ID + "!=?",
                    new String[] {String.valueOf(sPokemon.getSpeciesId()), String.valueOf(1), String.valueOf(sPokemon.getFormId())},
                    null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MiniPokemon miniPokemon = new MiniPokemon(cursor);
                arrayPokemon.add(miniPokemon);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            arrayPokemon = dbHelper.getAllPokemon();
        }

        RecyclerView pokemonList = (RecyclerView) listPicker.findViewById(R.id.recyclerView);
        pokemonList.setHasFixedSize(true);
        pokemonList.setLayoutManager(new LinearLayoutManager(this));
        pokemonList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        PokedexAdapter adapter = new PokedexAdapter(this, arrayPokemon);
        adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(getBaseContext(), CompareActivity.class);
                intent.putExtra(CompareActivity.EXTRA_POKEMON_1, sPokemon.toMiniPokemon());
                intent.putExtra(CompareActivity.EXTRA_POKEMON_2, arrayPokemon.get(position));
                startActivity(intent);

                alertDialog.dismiss();
            }
        });
        pokemonList.setAdapter(adapter);
        alertDialog.show();
    }

    private static void goToExperienceCalculator(Context context) {
        Intent intent = new Intent(context, ExperienceCalculatorActivity.class);
        intent.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, sPokemon.toMiniPokemon());
        context.startActivity(intent);
    }


    /*
     * VIEW PAGER ADAPTER
     */

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Returns the number of tabs
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Returns a new instance of the fragment
                case 0:
                    return new MainFragment();
                case 1:
                    return new FormsFragment();
                case 2:
                    return new EvolutionsFragment();
                case 3:
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
                    return getString(R.string.tab_pkmn_detail_forms).toUpperCase(l);
                case 2:
                    return getString(R.string.tab_pkmn_detail_evolutions).toUpperCase(l);
                case 3:
                    return getString(R.string.tab_pkmn_detail_moves).toUpperCase(l);
            }
            return null;
        }
    }


    /*
     * FRAGMENTS
     */

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
            PokemonDetail mainInfo = fetchGeneralData();
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
                    DetailAdapter adapter = new DetailAdapter(getActivity(), mDetails, true);
                    mRecyclerView.setAdapter(adapter);
                }
            }.execute();

            return mRootView;
        }

        private PokemonDetail fetchGeneralData() {
            ArrayList<Object> values = new ArrayList<>(7);

            values.add(sPokemon.toMiniPokemon());
            values.add(Pokemon.isDefault(sPokemon.getMiscValues()));
            values.add(sPokemon.getGenera().get("en") + " Pok\u00E9mon");
            values.add(Pokemon.isFormMega(sPokemon.getFormSpecificValues()));

            values.add(DataUtilsKt.typeIdToName(sPkmnTypeIds.get(1)));
            boolean hasSecondaryType = Pokemon.hasSecondaryType(sPkmnTypeIds);
            values.add(hasSecondaryType ? DataUtilsKt.typeIdToName(sPkmnTypeIds.get(2)) : null);
            values.add(hasSecondaryType);

            return new PokemonDetail(values);
        }

        private PokemonDetail fetchAbilityData() {
            final SparseIntArray abilityIds = sPokemon.getAbilityIds();

            SparseArray<MiniAbility> abilities = new SparseArray<>(3);
            for (int i = 1; i < abilityIds.size() + 1; i++) {
                int id = abilityIds.get(i);
                MiniAbility miniAbility = (id == DataUtilsKt.NULL_INT) ?
                        null : new MiniAbility(getActivity(), id);
                abilities.put(i, miniAbility);
            }
            final SparseArray<MiniAbility> finalAbilities = abilities;

            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                if (finalAbilities.get(i) == null) {
                    continue;
                }
                int propertyText = 0;
                switch (i) {
                    case 1:
                        propertyText = Pokemon.hasSecondaryAbility(abilityIds) ?
                                R.string.attr_ability_1 : R.string.attr_ability;
                        break;
                    case 2:
                        propertyText = R.string.attr_ability_2;
                        break;
                    case 3:
                        propertyText = R.string.attr_ability_hidden;
                        break;
                }
                properties.add(getActivity().getResources().getString(propertyText));
                values.add(finalAbilities.get(i).getName());
                final int j = i;
                listeners.add(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AbilityDetailActivity.class);
                        intent.putExtra(AbilityDetailActivity.EXTRA_ABILITY, finalAbilities.get(j));
                        startActivity(intent);
                    }
                });
            }

            PokemonDetail info = new PokemonDetail(R.string.header_abilities, properties, values);
            info.addOnClickListeners(listeners);
            return info;
        }

        private PokemonDetail fetchAppearanceData() {
            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            Resources res = getResources();

            properties.add(res.getString(R.string.attr_height));
            values.add(Pokemon.getHeight(sPkmnPhysicalAttrs) + " m");
            listeners.add(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                    intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_HEIGHT);
                    intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, String.valueOf(Pokemon.getHeight(sPkmnPhysicalAttrs)));
                    startActivity(intent);
                }
            });
            properties.add(res.getString(R.string.attr_mass));
            values.add(Pokemon.getWeight(sPkmnPhysicalAttrs) + " kg");
            listeners.add(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                    intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_MASS);
                    intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, String.valueOf(Pokemon.getWeight(sPkmnPhysicalAttrs)));
                    startActivity(intent);
                }
            });
            properties.add(res.getString(R.string.attr_colour));
            final String color = DataUtilsKt.colorIdToName(Pokemon.getColorId(sPkmnPhysicalAttrs));
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
            properties.add(res.getString(R.string.attr_shape));
            values.add(DataUtilsKt.shapeIdToName(Pokemon.getShapeId(sPkmnPhysicalAttrs), true) +
                    " (" + DataUtilsKt.shapeIdToName(Pokemon.getShapeId(sPkmnPhysicalAttrs), false) + ")");
            listeners.add(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                    intent.putExtra(PropertyDetailActivity.EXTRA_PROPERTY, PropertyDetailActivity.PROPERTY_SHAPE);
                    intent.putExtra(PropertyDetailActivity.EXTRA_VALUE, DataUtilsKt.shapeIdToName(Pokemon.getShapeId(sPkmnPhysicalAttrs), false));
                    startActivity(intent);
                }
            });

            PokemonDetail info = new PokemonDetail(R.string.header_physical_attrs, properties, values);
            info.addOnClickListeners(listeners);
            return info;
        }

        private PokemonDetail fetchGenderData() {
            final ArrayMap<String, Integer> genderValues = sPokemon.getGenderValues();
            ArrayList<Integer> values = new ArrayList<>();
            values.add(Pokemon.getGenderRate(genderValues));

            return new PokemonDetail(values, sPokemon.getName());
        }

        private PokemonDetail fetchStatData() {
            final ArrayMap<String, Integer> statValues = sPokemon.getStats();
            ArrayList<Integer> values = new ArrayList<>();

            String[] keys = new String[] {"hp", "atk", "def", "spa", "spd", "spe"};
            for (String key : keys) {
                values.add(statValues.get(key));
            }

            return new PokemonDetail(values, false);
        }

        private PokemonDetail fetchTrainingData() {
            ArrayMap<String, Integer> trainingValues = sPokemon.getTrainingValues();
            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            Resources res = getResources();

            properties.add(res.getString(R.string.attr_catch_rate));
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
            properties.add(res.getString(R.string.attr_base_happiness));
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
            properties.add(res.getString(R.string.attr_levelling_rate));
            final String levellingRate = DataUtilsKt.growthIdToName(Pokemon.getGrowthRateId(trainingValues));
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
            properties.add(res.getString(R.string.attr_exp_growth_abbr));
            final String exp = String.valueOf(DataUtilsKt.growthIdGetMaxExp(Pokemon.getGrowthRateId(trainingValues)));
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

            PokemonDetail info = new PokemonDetail(R.string.header_training, properties, values);
            info.addOnClickListeners(listeners);
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

        private PokemonDetail fetchPokedexData() {
            ArrayMap<String, Integer> pokedexValues = sPokemon.getPokedexNumbers();
            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();

            String[] pokedexKeys = Pokemon.POKEDEX_KEYS;

            for (int id = 1; id <= pokedexValues.size(); id++) {
                if (id == 10 || id == 11) {
                    continue;  // id 10 doesn't exist and id 11 is conquest series
                }

                String pokedexKey = pokedexKeys[id-1];
                if (!Pokemon.hasPokedexNumber(pokedexValues, pokedexKey)) {
                    continue;
                }

                properties.add(DataUtilsKt.pokedexIdToName(id));
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

            PokemonDetail info = new PokemonDetail(R.string.header_pokedex_numbers, properties, values);
            info.addOnClickListeners(listeners);
            return info;
        }

        private PokemonDetail fetchMoreData() {
            ArrayMap<String, Integer> moreValues = sPokemon.getMoreValues();
            ArrayList<String> properties = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            Resources res = getResources();

            properties.add(res.getString(R.string.attr_generation));
            final String generation = DataUtilsKt.genIdToRoman(Pokemon.getGenerationId(moreValues));
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

            properties.add(res.getString(R.string.attr_base_egg_steps));
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

            properties.add(res.getString(R.string.attr_base_egg_cycles));
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
                properties.add(res.getString(R.string.attr_habitat));
                final String habitat = DataUtilsKt.habitatIdToName(Pokemon.getHabitatId(moreValues));
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
            }

            PokemonDetail info = new PokemonDetail(R.string.header_more_information, properties, values);
            info.addOnClickListeners(listeners);
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

    public static class FormsFragment extends Fragment {

        private View mRootView;

        private CardView mCardView;
        private RecyclerView mRecyclerView;

        private ArrayList<PokemonForm> mAltForms;

        private AsyncTask<Void, Void, Void> mCurrAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_forms, container, false);

            // these have visibility 'gone'
            mCardView = (CardView) mRootView.findViewById(R.id.cardView);
            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);

            if (sPokemon == null) {
                throw new NullPointerException("pokemon is null");
            }

            loadData();

            return mRootView;
        }

        private void loadData() {
            if (PrefUtils.showPokemonImages(getContext())) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                        getResources().getInteger(R.integer.detail_columns_tiles)));
                displayTiles();
            } else {
                mCardView.setVisibility(View.VISIBLE);
                setupFormsList();
            }
        }


        private void displayTiles() {
            mCurrAsyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mAltForms = sPokemon.getAlternateForms();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    FormsTileAdapter adapter = new FormsTileAdapter(getActivity(), mAltForms);
                    adapter.setOnEntryClickListener(new FormsTileAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int position) {
                            if (mAltForms.get(position).getFormId() == sPokemon.getFormId()) {
                                sViewPager.setCurrentItem(0);
                            } else {
                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                intent.putExtra(EXTRA_POKEMON,
                                        mAltForms.get(position).toMiniPokemon(getActivity()));
                                startActivity(intent);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                }
            }.execute();
        }

        private void setupFormsList() {
            mCurrAsyncTask = new AsyncTask<Void, Void, Void>() {
                LinearLayout container;
                ArrayMap<String, Integer> formSpecificVals;
                PokemonForm currForm;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    container = (LinearLayout) mRootView.findViewById(R.id.container);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    mAltForms = sPokemon.getAlternateForms();
                    formSpecificVals = sPokemon.getFormSpecificValues();
                    ArrayMap<String, Integer> miscValues = sPokemon.getMiscValues();
                    currForm = new PokemonForm(
                            sPokemon.getId(), sPokemon.getSpeciesId(), sPokemon.getFormId(),
                            sPokemon.getName(), sPokemon.getFormName(), sPokemon.getFormAndPokemonName(),
                            sPokemon.getNationalDexNumber(), sPkmnTypeIds.get(1),
                            Pokemon.isDefault(miscValues), Pokemon.isFormDefault(formSpecificVals),
                            Pokemon.isFormMega(formSpecificVals));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (mAltForms.isEmpty()) {
                        container.removeAllViews();
                        getActivity().getLayoutInflater().inflate(R.layout.list_item_null, container, true);
                        String listMessage = getResources().getString(R.string.null_alternate_forms, sPokemon.getName());
                        TextView tvListTxt = (TextView) mRootView.findViewById(R.id.item_null_text);
                        tvListTxt.setText(listMessage);
                    } else {
                        FormsVGAdapter adapter = new FormsVGAdapter(getActivity(), container, currForm, mAltForms);
                        adapter.createListItems();
                        adapter.setOnEntryClickListener(new FormsVGAdapter.OnEntryClickListener() {
                            @Override
                            public void onEntryClick(View view, int position) {
                                if (mAltForms.get(position).getFormId() == sPokemon.getFormId()) {
                                    sViewPager.setCurrentItem(0);
                                } else {
                                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                                    intent.putExtra(EXTRA_POKEMON, mAltForms.get(position).toMiniPokemon(getActivity()));
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }.execute();
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mCurrAsyncTask != null) {
                mCurrAsyncTask.cancel(true);
            }
        }
    }

    public static class EvolutionsFragment extends Fragment {

        private View mRootView;

        private AsyncTask<Void, Void, Void> mAsyncTask;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_evolutions, container, false);

            displayEvolutions();

            return mRootView;
        }

        private void displayEvolutions() {
            mAsyncTask = new AsyncTask<Void, Void, Void>() {
                ArrayList<MiniPokemon> evolutions;

                @Override
                protected Void doInBackground(Void... voids) {
                    evolutions = fetchOrderedEvolutions();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    EvolutionsAdapter adapter = new EvolutionsAdapter(
                            getActivity(), evolutions, sPokemon.toMiniPokemon());

                    adapter.setOnEntryClickListener(new EvolutionsAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int position) {
                            MiniPokemon clickedPokemon = evolutions.get(position);
                            if (clickedPokemon.getId() != sPokemon.getId()) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                intent.putExtra(EXTRA_POKEMON, clickedPokemon);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }
            }.execute();
        }

        private ArrayList<MiniPokemon> fetchOrderedEvolutions() {
            int evolutionChainId = Pokemon.getEvolutionChainId(sPokemon.getEvolutionInfo());

            ArrayList<MiniPokemon> evolutions = new ArrayList<>();

            PokemonDBHelper helper = new PokemonDBHelper(getActivity());
            Cursor cursor = helper.getReadableDatabase().query(
                    PokemonDBHelper.TABLE_NAME,
                    concatenateArrays(MiniPokemon.DB_COLUMNS,
                            new String[] {PokemonDBHelper.COL_EVOLVES_FROM_SPECIES_ID,
                                    PokemonDBHelper.COL_EVOLUTION_CHAIN_ID}),
                    PokemonDBHelper.COL_EVOLUTION_CHAIN_ID + "=? AND " +
                            PokemonDBHelper.COL_IS_DEFAULT + "=?",
                    new String[] {String.valueOf(evolutionChainId), "1"},
                    null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                evolutions.add(new MiniPokemon(cursor));
                cursor.moveToNext();
            }
            cursor.close();

            Collections.sort(evolutions, new Comparator<MiniPokemon>() {
                @Override
                public int compare(MiniPokemon p1, MiniPokemon p2) {
                    return Pokemon.getEvolvesFromSpeciesId(p1.toPokemon(getActivity()).getEvolutionInfo())
                            - Pokemon.getEvolvesFromSpeciesId(p2.toPokemon(getActivity()).getEvolutionInfo());
                }
            });

            return evolutions;
        }

        // TODO put in a utils class
        public <T> T[] concatenateArrays (T[] a, T[] b) {
            int aLen = a.length;
            int bLen = b.length;

            @SuppressWarnings("unchecked")
            T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
            System.arraycopy(a, 0, c, 0, aLen);
            System.arraycopy(b, 0, c, aLen, bLen);

            return c;
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
        }
    }

    public static class MovesFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

        private View mRootView;

        private String mLearnMethod;

        private LinearLayout mContainer;
        private Button mSubmitButton;
        private LabelledSpinner mSpinnerMethod, mSpinnerGame;

        private ArrayList<String> mArrayMethodTitles = new ArrayList<>();
        private ArrayList<Integer> mArrayMethodTypes = new ArrayList<>();

        private ArrayList<VersionGroup> mVersionGroups;
        private ArrayList<String> mVersionGroupNames;
        private int mVGroupListPos;

        private AsyncTask<Void, Integer, Void> mAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_learnsets, container, false);

            setupView();

            return mRootView;
        }

        private void setupView() {
            Resources res = getActivity().getResources();
            mArrayMethodTitles.add(res.getString(R.string.header_moves_levelup));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_machine));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_tutor));
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_LEVEL_UP);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_MACHINE);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_TUTOR);

            int genId = Pokemon.getGenerationId(sPokemon.getMoreValues());
            mVersionGroups = new ArrayList<>();
            mVersionGroupNames = new ArrayList<>();
            PokeDB pokeDB = new PokeDB(getActivity());
            Cursor cursor = pokeDB.getReadableDatabase().query(
                    PokeDB.VersionGroups.TABLE_NAME,
                    null,
                    PokeDB.VersionGroups.COL_ID + "!=? AND " +
                            PokeDB.VersionGroups.COL_ID + "!=? AND " +
                            PokeDB.VersionGroups.COL_GENERATION_ID + ">=?",
                    new String[] {String.valueOf(12), String.valueOf(13), String.valueOf(genId)},
                    null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                VersionGroup versionGroup = new VersionGroup(cursor);
                mVersionGroups.add(versionGroup);
                mVersionGroupNames.add(versionGroup.fetchName(getActivity()));
                cursor.moveToNext();
            }
            cursor.close();


            mSpinnerMethod = (LabelledSpinner) mRootView.findViewById(R.id.spinner_learn_method);
            mSpinnerMethod.setItemsArray(mArrayMethodTitles);
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

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && getActivity() != null) {
                // getActivity() can give NPE when using tab buttons instead of swiping
                loadCard();
            }
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

            title.setText(mLearnMethod);
            subtitle.setText("Pok\u00E9mon " + mVersionGroupNames.get(mVGroupListPos));

            int methodNoInList = mArrayMethodTitles.indexOf(mLearnMethod);
            final int learnMethod = mArrayMethodTypes.get(methodNoInList);
            final VersionGroup versionGroup = mVersionGroups.get(mVGroupListPos);

            mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                PokemonMovesAdapter adapter;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (progressBar.getVisibility() != View.VISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                protected Void doInBackground(Void... params) {
                    PokemonLearnset learnset = new PokemonLearnset(
                            getActivity(), sPokemon.getId(), learnMethod, versionGroup.getId());
                    ArrayList<PokemonMove> arrayMoves = learnset.getPokemonMoves();
                    Collections.sort(arrayMoves, new Comparator<PokemonMove>() {
                        @Override
                        public int compare(PokemonMove lhs, PokemonMove rhs) {
                            if (learnMethod == AppConfig.LEARN_METHOD_LEVEL_UP) {
                                return lhs.getLevel() - rhs.getLevel();
                            } else {
                                return lhs.getMoveId() - rhs.getMoveId();
                            }
                        }
                    });
                    final ArrayList<PokemonMove> arrayMovesFinal = arrayMoves;
                    adapter = new PokemonMovesAdapter(getActivity(), arrayMoves);
                    adapter.setOnEntryClickListener(new PokemonMovesAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), MoveDetailActivity.class);
                            intent.putExtra(MoveDetailActivity.EXTRA_MOVE,
                                    arrayMovesFinal.get(position).toMiniMove(getActivity()));
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
                case R.id.spinner_learn_method:
                    mLearnMethod = selected;
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
