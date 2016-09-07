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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.Height;
import com.satsumasoftware.pokedex.framework.Mass;
import com.satsumasoftware.pokedex.framework.Type;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.ui.DetailActivity;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;

import java.util.ArrayList;
import java.util.Collections;

public class FilterResultsActivity extends AppCompatActivity {

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
        if (mExtras.containsKey(FILTER_MOVE)) {
            filterLearnsetData();
            return;
        }

        Filters filters = new Filters();

        String name = mExtras.getString(FILTER_NAME);
        if (name != null) {
            // see http://stackoverflow.com/a/9438823/4230345 about the stuff after LIKE
            filters.addSelection("LOWER("+PokemonDBHelper.COL_NAME+") LIKE '%'||?||'%'");
            filters.addArguments(name.toLowerCase());
        }

        String species = mExtras.getString(FILTER_SPECIES);
        if (species != null) {
            filters.addSelection("LOWER("+PokemonDBHelper.COL_GENUS+") LIKE '%'||?||'%'");
            filters.addArguments(species.toLowerCase());
        }

        // TODO: Add option for type filtering to be more specific
        // e.g. checkbox that will make sure type 1 is type1 of the pokemon rather than accepting them swapped around

        String type1 = mExtras.getString(FILTER_TYPE_1);
        String type2 = mExtras.getString(FILTER_TYPE_2);

        if (type1 != null && type2 == null) {
            filters.addSelection(
                    PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?");
            filters.addArguments(
                    String.valueOf(new Type(type1).getId()),
                    String.valueOf(new Type(type1).getId()));

        } else if (type1 == null && type2 != null) {
            filters.addSelection(
                    PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?");
            filters.addArguments(
                    String.valueOf(new Type(type2).getId()),
                    String.valueOf(new Type(type2).getId()));

        } else if (type1 != null && type2 != null) {
            filters.addSelection(
                    "("+ PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?) " +
                    "AND ("+ PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?)");
            filters.addArguments(
                    String.valueOf(new Type(type1).getId()),
                    String.valueOf(new Type(type1).getId()),
                    String.valueOf(new Type(type2).getId()),
                    String.valueOf(new Type(type2).getId()));
        }

        int abilityId = mExtras.getInt(FILTER_ABILITY);

        if (abilityId != 0) {
            filters.addSelection(PokemonDBHelper.COL_ABILITY_1_ID + " LIKE ? " +
                    "OR " + PokemonDBHelper.COL_ABILITY_2_ID + " LIKE ? " +
                    "OR " + PokemonDBHelper.COL_ABILITY_HIDDEN_ID + " LIKE ?");
            filters.addArguments(String.valueOf(abilityId),
                    String.valueOf(abilityId),
                    String.valueOf(abilityId));
        }

        String growthId = mExtras.getString(FILTER_GROWTH);
        if (growthId != null) {
            filters.addSelection(PokemonDBHelper.COL_GROWTH_RATE_ID+" LIKE ?");
            filters.addArguments(growthId);
        }

        String generationId = mExtras.getString(FILTER_GENERATION);
        if (generationId != null) {
            filters.addSelection(PokemonDBHelper.COL_GENERATION_ID + "=?");
            filters.addArguments(generationId);
        }

        String catchRate = mExtras.getString(FILTER_CATCH_RATE);
        if (catchRate != null) {
            filters.addSelection(PokemonDBHelper.COL_CAPTURE_RATE + "=?");
            filters.addArguments(catchRate);
        }

        String baseHappiness = mExtras.getString(FILTER_HAPPINESS);
        if (baseHappiness != null) {
            filters.addSelection(PokemonDBHelper.COL_BASE_HAPPINESS + "=?");
            filters.addArguments(baseHappiness);
        }

        String mass = mExtras.getString(FILTER_MASS);
        if (mass != null) {
            filters.addSelection(PokemonDBHelper.COL_WEIGHT + "=?");
            filters.addArguments(String.valueOf(new Mass(Integer.parseInt(mass)).getDbValue()));
        }

        String height = mExtras.getString(FILTER_HEIGHT);
        if (height != null) {
            filters.addSelection(PokemonDBHelper.COL_HEIGHT + "=?");
            filters.addArguments(String.valueOf(new Height(Integer.parseInt(height)).getDbValue()));
        }

        String colorId = mExtras.getString(FILTER_COLOUR);
        if (colorId != null) {
            filters.addSelection(PokemonDBHelper.COL_COLOR_ID + "=?");
            filters.addArguments(colorId);
        }

        String shapeId = mExtras.getString(FILTER_SHAPE);
        if (shapeId != null) {
            filters.addSelection(PokemonDBHelper.COL_SHAPE_ID + "=?");
            filters.addArguments(shapeId);
        }

        String habitatId = mExtras.getString(FILTER_HABITAT);
        if (habitatId != null) {
            filters.addSelection(PokemonDBHelper.COL_HABITAT_ID + "=?");
            filters.addArguments(habitatId);
        }

        String hatchCounter = mExtras.getString(FILTER_HATCH_COUNTER);
        if (hatchCounter != null) {
            filters.addSelection(PokemonDBHelper.COL_HATCH_COUNTER + "=?");
            filters.addArguments(hatchCounter);
        }

        if (filters.size() == 0) {
            return;
        }

        mArrayPokemon.clear();

        PokemonDBHelper helper = PokemonDBHelper.getInstance(this);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                null,
                filters.selectionAsString(),
                filters.selectionArgsAsArray(),
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            mArrayPokemon.add(new MiniPokemon(cursor));
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void filterLearnsetData() {
        int moveId = mExtras.getInt(FILTER_MOVE);

        PokeDB pokeDB = PokeDB.getInstance(this);
        SQLiteDatabase db = pokeDB.getReadableDatabase();

        final int versionGroupId = 16;  // TODO: add option to change version group

        String gameFilter = "(" + PokeDB.PokemonMoves.COL_VERSION_GROUP_ID + "=" + versionGroupId + ")";
        String moveFilter = "(" + PokeDB.PokemonMoves.COL_MOVE_ID + "=" + moveId + ")";
        String selection = gameFilter + " AND " + moveFilter;

        Cursor cursor = db.query(
                PokeDB.PokemonMoves.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        mArrayPokemon.clear();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonMoves.COL_POKEMON_ID));
            MiniPokemon pokemon = new MiniPokemon(this, id);
            if (!mArrayPokemon.contains(pokemon)) {
                mArrayPokemon.add(pokemon);
            }
            cursor.moveToNext();
        }
        cursor.close();
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


    private class Filters {

        ArrayList<String> mSelectionList;
        ArrayList<String> mSelectionArgsList;

        Filters() {
            mSelectionList = new ArrayList<>();
            mSelectionArgsList = new ArrayList<>();
        }

        void addSelection(String selection) {
            mSelectionList.add(selection);
        }

        void addArguments(String... args) {
            Collections.addAll(mSelectionArgsList, args);
        }

        int size() {
            return mSelectionList.size();
        }

        String selectionAsString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mSelectionList.size(); i++) {
                builder.append("(")
                        .append(mSelectionList.get(i))
                        .append(")");

                if (i != mSelectionList.size() - 1) {
                    builder.append(" AND ");
                }
            }
            return builder.toString();
        }

        String[] selectionArgsAsArray() {
            String[] selectionArgs = new String[mSelectionArgsList.size()];
            for (int i = 0; i < mSelectionArgsList.size(); i++) {
                selectionArgs[i] = mSelectionArgsList.get(i);
            }
            return selectionArgs;
        }
    }

}
