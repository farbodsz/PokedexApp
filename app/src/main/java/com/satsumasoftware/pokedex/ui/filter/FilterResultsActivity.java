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
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.db.PokemonMovesDBHelper;
import com.satsumasoftware.pokedex.entities.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.ui.DetailActivity;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AppConfig;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;
import java.util.Arrays;


public class FilterResultsActivity extends AppCompatActivity {

    private final static String LOG_TAG = "FilterResultsActivity";

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
    public static final String FILTER_EXP_TO_100 = "EXP_TO_100";
    public static final String FILTER_MASS = "MASS";
    public static final String FILTER_HEIGHT = "HEIGHT";
    public static final String FILTER_COLOUR = "COLOUR";
    public static final String FILTER_SHAPE = "SHAPE";
    public static final String FILTER_HABITAT = "HABITAT";
    public static final String FILTER_EGG_STEPS = "EGG_STEPS";
    public static final String FILTER_EGG_CYCLES = "EGG_CYCLES";

    private Bundle mExtras;
    private AsyncTask<Void, Integer, Void> mAsyncTask;

    private ArrayList<MiniPokemon> mArrayPokemon = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private View mRootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);
        mRootLayout = findViewById(R.id.filteredInfo_rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filteredInfo_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mExtras = getIntent().getExtras();

        mRecyclerView = (RecyclerView) findViewById(R.id.filteredInfo_rv);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        final ProgressBar progress = (ProgressBar) findViewById(R.id.filteredInfo_progress);
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

        ArrayList<String> selectionsList = new ArrayList<>();
        ArrayList<String> selectionArgsList = new ArrayList<>();

        String name = mExtras.getString(FILTER_NAME);
        if (name != null) {
            selectionsList.add("LOWER("+ PokemonDBHelper.COL_NAME+") LIKE '%'||?||'%'"); // http://stackoverflow.com/a/9438823/4230345
            selectionArgsList.add(name.toLowerCase());
            // Note 'toLowerCase' because we are ignoring case by also doing "LOWER(column_name)"
        }

        String species = mExtras.getString(FILTER_SPECIES); // This will not be null (we checked for it in the Filter Activity)
        if (species != null) {
            selectionsList.add("LOWER("+ PokemonDBHelper.COL_GENUS+") LIKE '%'||?||'%'");
            selectionArgsList.add(species.toLowerCase());
        }

        // TODO: Add option for type filtering to be more specific
        // e.g. checkbox that will make sure type 1 is type1 of the pokemon rather than accepting them swapped around
        String type1 = mExtras.getString(FILTER_TYPE_1);
        String type2 = mExtras.getString(FILTER_TYPE_2);
        if ((type1 != null) && (type2 == null)) {
            //conditionType = line[10].equalsIgnoreCase(type1) || line[11].equalsIgnoreCase(type1);
            selectionsList.add(PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?");
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type1)));
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type1)));
        } else if ((type1 == null) && (type2 != null)) {
            //conditionType = line[10].equalsIgnoreCase(type2) || line[11].equalsIgnoreCase(type2);
            selectionsList.add(PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?");
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type2)));
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type2)));
        } else if ((type1 != null) && (type2 != null)) {
            //conditionType = (line[10].equalsIgnoreCase(type1) || line[11].equalsIgnoreCase(type1)) &&
                    //(line[10].equalsIgnoreCase(type2) || line[11].equalsIgnoreCase(type2));
            selectionsList.add("("+ PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?) " +
                    "AND ("+ PokemonDBHelper.COL_TYPE_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_TYPE_2_ID +" LIKE ?)");
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type1)));
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type1)));
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type2)));
            selectionArgsList.add(String.valueOf(DataUtils.typeToId(type2)));
        }

        int abilityId = mExtras.getInt(FILTER_ABILITY);
        if (abilityId != 0) {
            //conditionAbility = line[12].equalsIgnoreCase(ability) ||
                    //line[13].equalsIgnoreCase(ability) ||
                    //line[14].equalsIgnoreCase(ability);
            selectionsList.add(PokemonDBHelper.COL_ABILITY_1_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_ABILITY_2_ID +" LIKE ? " +
                    "OR "+ PokemonDBHelper.COL_ABILITY_HIDDEN_ID+" LIKE ?");
            // FIXME optimise
            selectionArgsList.add(String.valueOf(abilityId));
            selectionArgsList.add(String.valueOf(abilityId));
            selectionArgsList.add(String.valueOf(abilityId));
        }

        //TODO: Add filtering for statistics

        String growth = mExtras.getString(FILTER_GROWTH);
        if (growth != null) {
            //conditionGrowth = line[21].equalsIgnoreCase(InfoUtils.getAbbreviationFromGrowth(growth));
            selectionsList.add(PokemonDBHelper.COL_GROWTH_RATE_ID+" LIKE ?");
            selectionArgsList.add(String.valueOf(InfoUtils.growthToId(growth)));
        }

        String exp = mExtras.getString(FILTER_EXP_TO_100);
        if (exp != null) {
            selectionsList.add(PokemonDBHelper.COL_GROWTH_RATE_ID + "=?");
            selectionArgsList.add(String.valueOf(DataUtils.growthIdFromMaxExp(Integer.parseInt(exp))));
        }

        String generation = mExtras.getString(FILTER_GENERATION);
        if (generation != null) {
            selectionsList.add(PokemonDBHelper.COL_GENERATION_ID + "=?");
            selectionArgsList.add(String.valueOf(InfoUtils.getGenFromRoman(generation)));
        }

        String catchRate = mExtras.getString(FILTER_CATCH_RATE);
        if (catchRate != null) {
            selectionsList.add(PokemonDBHelper.COL_CAPTURE_RATE + "=?");
            selectionArgsList.add(String.valueOf(catchRate));
        }

        String baseHappiness = mExtras.getString(FILTER_HAPPINESS);
        if (baseHappiness != null) {
            selectionsList.add(PokemonDBHelper.COL_BASE_HAPPINESS + "=?");
            selectionArgsList.add(String.valueOf(baseHappiness));
        }

        String mass = mExtras.getString(FILTER_MASS);
        if (mass != null) {
            selectionsList.add(PokemonDBHelper.COL_WEIGHT + "=?");
            selectionArgsList.add(String.valueOf(mass));
        }

        String height = mExtras.getString(FILTER_HEIGHT);
        if (height != null) {
            selectionsList.add(PokemonDBHelper.COL_HEIGHT + "=?");
            selectionArgsList.add(String.valueOf(height));
        }

        String colour = mExtras.getString(FILTER_COLOUR);
        if (colour != null) {
            selectionsList.add(PokemonDBHelper.COL_COLOR_ID + "=?");
            selectionArgsList.add(String.valueOf(InfoUtils.getIdFromColour(colour)));
        }

        String shape = mExtras.getString(FILTER_SHAPE);
        if (shape != null) {
            selectionsList.add(PokemonDBHelper.COL_SHAPE_ID + "=?");
            selectionArgsList.add(String.valueOf(InfoUtils.getIdFromShape(shape)));
        }

        String habitat = mExtras.getString(FILTER_HABITAT);
        if (habitat != null) {
            selectionsList.add(PokemonDBHelper.COL_HABITAT_ID + "=?");
            selectionArgsList.add(String.valueOf(InfoUtils.getIdFromHabitat(habitat)));
        }

        String eggSteps = mExtras.getString(FILTER_EGG_STEPS);
        if (eggSteps != null) {
            selectionsList.add(PokemonDBHelper.COL_HATCH_COUNTER + "=?");
            selectionArgsList.add(String.valueOf(Integer.parseInt(eggSteps) / AppConfig.EGG_CYCLE_STEPS));
        }

        String eggCycles = mExtras.getString(FILTER_EGG_CYCLES);
        if (eggCycles != null) {
            selectionsList.add(PokemonDBHelper.COL_HATCH_COUNTER + "=?");
            selectionArgsList.add(eggCycles);
        }


        Log.d(LOG_TAG, "selectionsList.size(): " + selectionsList.size());
        Log.d(LOG_TAG, "selectionArgsList.size(): " + selectionArgsList.size());


        if (selectionsList.size() == 0) {
            return;
        }

        String selection = "";
        for (int i = 0; i < selectionsList.size(); i++) {
            selection = selection + "("+selectionsList.get(i)+")";
            if (i != selectionsList.size()-1) { // If not last point
                selection = selection + " AND ";
            }
        }
        String[] selectionArgs = new String[selectionArgsList.size()];
        for (int i = 0; i < selectionArgsList.size(); i++) {
            selectionArgs[i] = selectionArgsList.get(i);
        }
        mArrayPokemon.clear();

        Log.d(LOG_TAG, "selection: " + selection);
        Log.d(LOG_TAG, "selectionArgs: " + Arrays.toString(selectionArgs));

        PokemonDBHelper helper = new PokemonDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                PokemonDBHelper.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ID));
            int speciesId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_ID));
            int formId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID));
            String pkmnName = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME));
            String form = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME));
            String pokemonAndForm = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
            int pokedexNumber = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
            MiniPokemon pokemon = new MiniPokemon(id, speciesId, formId, pkmnName, form, pokemonAndForm, pokedexNumber);
            mArrayPokemon.add(pokemon);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void filterLearnsetData() {
        int moveId = mExtras.getInt(FILTER_MOVE);

        PokemonMovesDBHelper helper = new PokemonMovesDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String gameFilter = "(" + PokemonMovesDBHelper.COL_VERSION_GROUP_ID + "=" + AppConfig.GAME_VERSION_X_Y + ")";
        String learnFilter = "(" + PokemonMovesDBHelper.COL_POKEMON_MOVE_METHOD_ID + "=" + AppConfig.LEARN_METHOD_LEVEL_UP +
                " OR " + PokemonMovesDBHelper.COL_POKEMON_MOVE_METHOD_ID + "=" + AppConfig.LEARN_METHOD_MACHINE +
                " OR " + PokemonMovesDBHelper.COL_POKEMON_MOVE_METHOD_ID + "=" + AppConfig.LEARN_METHOD_TUTOR + ")";
        String moveFilter = "(" + PokemonMovesDBHelper.COL_MOVE_ID + "=" + moveId + ")";
        String selection = gameFilter + " AND " + learnFilter + " AND " + moveFilter;

        Cursor cursor = db.query(
                PokemonMovesDBHelper.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        mArrayPokemon.clear();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokemonMovesDBHelper.COL_POKEMON_ID));
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
            Log.d("LIST IS EMPTY", "MHM");
            ViewGroup viewGroup = (ViewGroup) mRecyclerView.getParent();
            viewGroup.removeView(mRecyclerView);
            viewGroup.addView(getLayoutInflater().inflate(R.layout.fragment_misc_no_results, viewGroup, false));
        } else {
            PokedexAdapter adapter = new PokedexAdapter(this, pokemonList);
            adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
                @Override
                public void onEntryClick(View view, int position) {
                    Intent intent = new Intent(FilterResultsActivity.this, DetailActivity.class);
                    intent.putExtra("POKEMON", pokemonList.get(position));
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
