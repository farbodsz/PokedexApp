package com.satsumasoftware.pokedex.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.GrowthRate;
import com.satsumasoftware.pokedex.framework.Type;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.ui.adapter.FilterListItemVGAdapter;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.ui.filter.Filter;
import com.satsumasoftware.pokedex.ui.filter.Filters;
import com.satsumasoftware.pokedex.ui.filter.Query;
import com.satsumasoftware.pokedex.ui.filter.SearchResultsActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.CustomIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PokedexActivity extends BaseActivity
        implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private DragScrollBar mScrollBar;

    private Query.Builder mQueryBuilder = new Query.Builder();

    private boolean mSortByName = false;

    private ArrayList<MiniPokemon> mCurrList;

    private PokemonDBHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_filterable);
        mRootLayout = findViewById(R.id.drawerLayout);

        AdUtils.setupAds(this, R.id.adView);

        mToolbar = getSelfToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mScrollBar = new DragScrollBar(this, mRecyclerView, false);

        mDbHelper = PokemonDBHelper.getInstance(this);
        populateList(mDbHelper.getAllPokemon());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        if (!PrefUtils.isWelcomeDone(this)) {
            getSelfDrawerLayout().openDrawer(GravityCompat.START);
            PrefUtils.markWelcomeDone(this);
            PreferenceManager.setDefaultValues(this, R.xml.preferences_fragment, false); // Sets up the Settings page
        }

        mNoResults = findViewById(R.id.fragment_no_results);
    }

    private void populateList(ArrayList<MiniPokemon> pokemonList) {
        mCurrList = pokemonList;
        Collections.sort(pokemonList, new Comparator<MiniPokemon>() {
            @Override
            public int compare(MiniPokemon pokemon1, MiniPokemon pokemon2) {
                if (mSortByName) {
                    return pokemon1.getName().compareTo(pokemon2.getName()); // Ascending
                } else {
                    return pokemon1.getNationalDexNumber() - pokemon2.getNationalDexNumber(); // Ascending
                }
            }
        });
        final ArrayList<MiniPokemon> listFinal = pokemonList;

        PokedexAdapter adapter = new PokedexAdapter(this, listFinal);
        adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(PokedexActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POKEMON, listFinal.get(position));
                startActivity(intent);
            }
        });
        adapter.setOnEntryLongClickListener(new PokedexAdapter.OnEntryLongClickListener() {
            @Override
            public void onEntryLongClick(View view, int position) {
                MiniPokemon thisPokemon = listFinal.get(position);
                ActionUtils.handleListLongClick(PokedexActivity.this, thisPokemon, mRootLayout);
            }
        });
        mRecyclerView.setAdapter(adapter);

        mScrollBar.removeIndicator()
                .addIndicator(mSortByName ?
                        new AlphabetIndicator(this) : new CustomIndicator(this), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pokedex, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        // Note: getSearchableInfo(getComponentName()) will not work on all flavours
        // because of a difference in app IDs

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_filter = menu.findItem(R.id.action_filter);
        MenuItem item_buyPro = menu.findItem(R.id.action_buyPro);

        if (Flavours.type == Flavours.Type.PAID) {
            item_buyPro.setVisible(false);
        } else {
            item_filter.setIcon(R.drawable.ic_filter_list_grey600_24dp);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (Flavours.type == Flavours.Type.PAID) {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_sort_id:
                if (mSortByName) {
                    mSortByName = false;
                    item.setChecked(true);
                    Snackbar
                            .make(mRootLayout, R.string.sort_by_id, Snackbar.LENGTH_SHORT)
                            .show();
                    populateList(mCurrList);
                } else {
                    Snackbar
                            .make(mRootLayout, R.string.sort_by_id_already, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.action_sort_name:
                if (!mSortByName) {
                    mSortByName = true;
                    item.setChecked(true);
                    Snackbar
                            .make(mRootLayout, R.string.sort_by_name, Snackbar.LENGTH_SHORT)
                            .show();
                    populateList(mCurrList);
                } else {
                    Snackbar
                            .make(mRootLayout, R.string.sort_by_name_already, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_buyPro:
                AlertUtils.buyPro(this);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupFilterDrawer() {
        Spinner spinnerName = (Spinner) findViewById(R.id.spinner_name);
        LinearLayout llTypes = (LinearLayout) findViewById(R.id.container_types);
        LinearLayout llGrowth = (LinearLayout) findViewById(R.id.container_growth);
        LinearLayout llGens = (LinearLayout) findViewById(R.id.container_generations);
        Button btnAdvancedFilter = (Button) findViewById(R.id.button_advanced_filter);

        findViewById(R.id.viewGroup_name_list).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_location_region).setVisibility(View.GONE);

        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(this, R.array.filter_name, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.filter_name);
                String text = array[position];
                mQueryBuilder.removePropertyFilters(PokemonDBHelper.COL_NAME);
                if (!text.equalsIgnoreCase("no filter")) {
                    // TODO filtering letters doesn't work - check SQLite statement
                    mQueryBuilder.addFilter(Filters.likeIgnoreCase(PokemonDBHelper.COL_NAME, text));
                }
                updateFilteredList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        FilterListItemVGAdapter typeAdapter = new FilterListItemVGAdapter(
                this, llTypes, R.array.list_types);
        typeAdapter.createListItems();
        typeAdapter.setOnFilterItemClickListener(this);

        FilterListItemVGAdapter growthAdapter = new FilterListItemVGAdapter(
                this, llGrowth, R.array.list_levelling_rates);
        growthAdapter.createListItems();
        growthAdapter.setOnFilterItemClickListener(this);

        FilterListItemVGAdapter gensAdapter = new FilterListItemVGAdapter(
                this, llGens, R.array.list_gens);
        gensAdapter.createListItems();
        gensAdapter.setOnFilterItemClickListener(this);

        btnAdvancedFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PokedexActivity.this, AdvancedFilterActivity.class));
            }
        });
    }

    @Override
    public void onFilterItemClick(View view, int position, String text, boolean isChecked, View itemView) {
        switch (itemView.getId()) {
            case R.id.container_types:
                String typeId = String.valueOf(new Type(text).getId());
                Filter typeFilter = Filters.or(Filters.equal(PokemonDBHelper.COL_TYPE_1_ID, typeId),
                        Filters.equal(PokemonDBHelper.COL_TYPE_2_ID, typeId));

                if (isChecked) {
                    mQueryBuilder.addFilter(typeFilter);
                } else {
                    mQueryBuilder.removeFilter(typeFilter);
                }
                updateFilteredList();
                break;

            case R.id.container_growth:
                String growthId = String.valueOf(new GrowthRate(text).getId());
                Filter growthFilter = Filters.equal(PokemonDBHelper.COL_GROWTH_RATE_ID, growthId);

                if (isChecked) {
                    mQueryBuilder.addFilter(growthFilter);
                } else {
                    mQueryBuilder.removeFilter(growthFilter);
                }
                updateFilteredList();
                break;

            case R.id.container_generations:
                String generation = String.valueOf(DataUtilsKt.romanToGenId(text));
                Filter genFilter = Filters.equal(PokemonDBHelper.COL_GENERATION_ID, generation);

                if (isChecked) {
                    mQueryBuilder.addFilter(genFilter);
                } else {
                    mQueryBuilder.removeFilter(genFilter);
                }
                updateFilteredList();
                break;
        }
    }

    private void updateFilteredList() {
        if (mQueryBuilder.hasNoFilters()) {
            mToolbar.setTitle(getResources().getString(R.string.title_activity_pokedex));
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
            populateList(mDbHelper.getAllPokemon());
            return;
        }

        mToolbar.setTitle(getResources().getString(R.string.title_filtered_pokedex));

        ArrayList<MiniPokemon> filteredList = new ArrayList<>();

        mDbHelper = PokemonDBHelper.getInstance(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sqlSelection = mQueryBuilder.build().getFilter().getSqlStatement();

        String[] columns = new String[] {
                PokemonDBHelper.COL_ID,
                PokemonDBHelper.COL_SPECIES_ID,
                PokemonDBHelper.COL_FORM_ID,
                PokemonDBHelper.COL_POKEMON_ORDER,
                PokemonDBHelper.COL_NAME,
                PokemonDBHelper.COL_FORM_NAME,
                PokemonDBHelper.COL_FORM_POKEMON_NAME,
                PokemonDBHelper.COL_FORM_IS_DEFAULT,
                PokemonDBHelper.COL_TYPE_1_ID,
                PokemonDBHelper.COL_TYPE_2_ID,
                PokemonDBHelper.COL_GROWTH_RATE_ID,
                PokemonDBHelper.COL_GENERATION_ID,
                PokemonDBHelper.COL_POKEDEX_NATIONAL
        };
        Cursor cursor = db.query(
                PokemonDBHelper.TABLE_NAME,
                columns,
                sqlSelection + " AND (" + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=1)",
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MiniPokemon pokemon = new MiniPokemon(cursor);
            filteredList.add(pokemon);
            cursor.moveToNext();
        }
        cursor.close();

        if (filteredList.isEmpty()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mNoResults.setVisibility(View.VISIBLE);
            return;
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoResults.setVisibility(View.GONE);

        populateList(filteredList);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected Toolbar getSelfToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected DrawerLayout getSelfDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_NATIONAL_POKEDEX;
    }

    @Override
    protected NavigationView getSelfNavigationView() {
        return (NavigationView) findViewById(R.id.navigationView);
    }

    @Override
    protected boolean disableRightDrawerSwipe() {
        return true;
    }

}
