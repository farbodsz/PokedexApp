/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.ui.filter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.Height;
import com.satsumasoftware.pokedex.framework.Mass;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.query.Filter;
import com.satsumasoftware.pokedex.query.Filters;
import com.satsumasoftware.pokedex.query.Query;
import com.satsumasoftware.pokedex.ui.DetailActivity;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;

import java.util.ArrayList;

public class FilterResultsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FilterResultsActivity";

    public static final String FILTER_MOVE = "MOVE_ID";
    public static final String FILTER_NAME = "NAME";
    public static final String FILTER_SPECIES = "SPECIES";
    public static final String FILTER_TYPE_1 = "TYPE1";
    public static final String FILTER_TYPE_2 = "TYPE2";
    public static final String FILTER_ABILITY = "ABILITY";
    public static final String FILTER_GROWTH = "GROWTH";
    public static final String FILTER_GENERATION = "GENERATION";
    public static final String FILTER_CATCH_RATE = "CATCH_RATE";
    public static final String FILTER_HAPPINESS = "HAPPINESS";
    public static final String FILTER_MASS = "MASS";
    public static final String FILTER_HEIGHT = "HEIGHT";
    public static final String FILTER_COLOUR = "COLOUR";
    public static final String FILTER_SHAPE = "SHAPE";
    public static final String FILTER_HABITAT = "HABITAT";
    public static final String FILTER_HATCH_COUNTER = "HATCH_COUNTER";

    private Bundle mExtras;
    private AsyncTask<Void, Integer, Void> mAsyncTask;

    private ArrayList<MiniPokemon> mArrayPokemon = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private View mRootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);
        mRootLayout = findViewById(R.id.rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mExtras = getIntent().getExtras();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress_indeterminate);
        mAsyncTask = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                loadFilteredData();
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                populateList(mArrayPokemon);
                progress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void loadFilteredData() {
        boolean isMoveQuery = mExtras.containsKey(FILTER_MOVE);

        Query query;
        SQLiteDatabase db;
        String tableName;

        if (isMoveQuery) {
            query = makeMoveQuery();
            db = PokeDB.getInstance(this).getReadableDatabase();
            tableName = PokeDB.PokemonMoves.TABLE_NAME;
        } else {
            query = makePokedexFilterQuery();
            db = PokemonDBHelper.getInstance(this).getReadableDatabase();
            tableName = PokemonDBHelper.TABLE_NAME;
        }

        if (query == null) {
            Log.d(LOG_TAG, "No filters specified.");
            return;
        }

        String sqlSelection = query.getFilter().getSqlStatement();
        Log.d(LOG_TAG, "Querying in '" + tableName + "' with SQL statement:\n" + sqlSelection);

        mArrayPokemon.clear();

        Cursor cursor = db.query(
                tableName,
                null,
                sqlSelection,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MiniPokemon pokemon;

            if (isMoveQuery) {
                int id = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_POKEMON_ID));
                pokemon = MiniPokemon.create(this, id);
                if (!mArrayPokemon.contains(pokemon)) {
                    mArrayPokemon.add(pokemon);
                }
            } else {
                pokemon = new MiniPokemon(cursor);
                mArrayPokemon.add(pokemon);
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private Query makePokedexFilterQuery() {
        Query.Builder builder = new Query.Builder();

        String name = mExtras.getString(FILTER_NAME);
        if (name != null) {
            builder.addFilter(Filters.likeIgnoreCase(PokemonDBHelper.COL_NAME, name.toLowerCase()));
        }

        String species = mExtras.getString(FILTER_SPECIES);
        if (species != null) {
            builder.addFilter(Filters.likeIgnoreCase(
                    PokemonDBHelper.COL_GENUS, species.toLowerCase()));
        }

        // TODO: Add option for type filtering to be more specific
        // e.g. checkbox that will make sure type 1 is type1 of the pokemon rather than accepting
        // them swapped around

        String type1 = mExtras.getString(FILTER_TYPE_1);
        String type2 = mExtras.getString(FILTER_TYPE_2);

        if (type1 != null && type2 == null) {
            builder.addFilter(Filters.or(Filters.like(PokemonDBHelper.COL_TYPE_1_ID, type1),
                    Filters.like(PokemonDBHelper.COL_TYPE_2_ID, type1)));

        } else if (type1 == null && type2 != null) {
            builder.addFilter(Filters.or(Filters.like(PokemonDBHelper.COL_TYPE_1_ID, type2),
                    Filters.like(PokemonDBHelper.COL_TYPE_2_ID, type2)));

        } else if (type1 != null && type2 != null) {
            Filter filter1 = Filters.or(Filters.like(PokemonDBHelper.COL_TYPE_1_ID, type1),
                    Filters.like(PokemonDBHelper.COL_TYPE_2_ID, type1));
            Filter filter2 = Filters.or(Filters.like(PokemonDBHelper.COL_TYPE_1_ID, type2),
                    Filters.like(PokemonDBHelper.COL_TYPE_2_ID, type2));

            builder.addFilter(Filters.and(filter1, filter2));
        }

        int abilityId = mExtras.getInt(FILTER_ABILITY);

        if (abilityId != 0) {
            String abilityIdStr = String.valueOf(abilityId);

            Filter filter = Filters.or(Filters.like(PokemonDBHelper.COL_ABILITY_1_ID, abilityIdStr),
                    Filters.like(PokemonDBHelper.COL_ABILITY_2_ID, abilityIdStr),
                    Filters.like(PokemonDBHelper.COL_ABILITY_HIDDEN_ID, abilityIdStr));
            builder.addFilter(filter);
        }

        String growthId = mExtras.getString(FILTER_GROWTH);
        if (growthId != null) {
            builder.addFilter(Filters.like(PokemonDBHelper.COL_GROWTH_RATE_ID, growthId));
        }

        String generationId = mExtras.getString(FILTER_GENERATION);
        if (generationId != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_GENERATION_ID, generationId));
        }

        String catchRate = mExtras.getString(FILTER_CATCH_RATE);
        if (catchRate != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_CAPTURE_RATE, catchRate));
        }

        String baseHappiness = mExtras.getString(FILTER_HAPPINESS);
        if (baseHappiness != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_BASE_HAPPINESS, baseHappiness));
        }

        String mass = mExtras.getString(FILTER_MASS);
        if (mass != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_WEIGHT,
                    String.valueOf(new Mass(Integer.parseInt(mass)).getDbValue())));
        }

        String height = mExtras.getString(FILTER_HEIGHT);
        if (height != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_HEIGHT,
                    String.valueOf(new Height(Integer.parseInt(height)).getDbValue())));
        }

        String colorId = mExtras.getString(FILTER_COLOUR);
        if (colorId != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_COLOR_ID, colorId));
        }

        String shapeId = mExtras.getString(FILTER_SHAPE);
        if (shapeId != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_SHAPE_ID, shapeId));
        }

        String habitatId = mExtras.getString(FILTER_HABITAT);
        if (habitatId != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_HABITAT_ID, habitatId));
        }

        String hatchCounter = mExtras.getString(FILTER_HATCH_COUNTER);
        if (hatchCounter != null) {
            builder.addFilter(Filters.equal(PokemonDBHelper.COL_HATCH_COUNTER, hatchCounter));
        }

        if (builder.hasNoFilters()) {
            return null;
        }

        return builder.build();
    }

    private Query makeMoveQuery() {
        int moveId = mExtras.getInt(FILTER_MOVE);

        final int versionGroupId = 16;  // TODO: add option to change version group

        return new Query.Builder().addFilter(Filters.and(
                Filters.equal(PokeDB.PokemonMoves.COL_VERSION_GROUP_ID, String.valueOf(versionGroupId)),
                Filters.equal(PokeDB.PokemonMoves.COL_MOVE_ID, String.valueOf(moveId))))
                .build();
    }

    private void populateList(final ArrayList<MiniPokemon> pokemonList) {
        if (pokemonList.isEmpty()) {
            ViewGroup viewGroup = (ViewGroup) mRecyclerView.getParent();
            viewGroup.removeView(mRecyclerView);
            viewGroup.addView(getLayoutInflater().inflate(R.layout.fragment_misc_no_results, viewGroup, false));

        } else {
            PokedexAdapter adapter = new PokedexAdapter(this, pokemonList);
            adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
                @Override
                public void onEntryClick(View view, int position) {
                    Intent intent = new Intent(FilterResultsActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POKEMON, pokemonList.get(position));
                    startActivity(intent);
                }
            });
            adapter.setOnEntryLongClickListener(new PokedexAdapter.OnEntryLongClickListener() {
                @Override
                public void onEntryLongClick(View view, int position) {
                    MiniPokemon thisPokemon = pokemonList.get(position);
                    ActionUtils.handleListLongClick(FilterResultsActivity.this, thisPokemon, mRootLayout);
                }
            });
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
    }

}
