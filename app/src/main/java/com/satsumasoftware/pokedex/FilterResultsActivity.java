package com.satsumasoftware.pokedex;

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

import com.satsumasoftware.pokedex.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.db.LearnsetDBHelper;
import com.satsumasoftware.pokedex.db.PokedexDBHelper;
import com.satsumasoftware.pokedex.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AppConfig;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;
import java.util.Arrays;


public class FilterResultsActivity extends AppCompatActivity {

    private final static String LOG_TAG = "FilterResultsActivity";

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
        if (mExtras.containsKey("MOVE_ID")) {
            filterLearnsetData();
            return;
        }

        ArrayList<String> selectionsList = new ArrayList<>();
        ArrayList<String> selectionArgsList = new ArrayList<>();

        String name = mExtras.getString("NAME");
        if (name != null) {
            selectionsList.add("LOWER("+PokedexDBHelper.COL_NAME+") LIKE '%'||?||'%'"); // http://stackoverflow.com/a/9438823/4230345
            selectionArgsList.add(name.toLowerCase());
            // Note 'toLowerCase' because we are ignoring case by also doing "LOWER(column_name)"
        }

        String species = mExtras.getString("SPECIES"); // This will not be null (we checked for it in the Filter Activity)
        if (species != null) {
            selectionsList.add("LOWER("+PokedexDBHelper.COL_SPECIES+") LIKE '%'||?||'%'");
            selectionArgsList.add(species.toLowerCase());
        }

        // TODO: Add option for type filtering to be more specific
        // e.g. checkbox that will make sure type 1 is type1 of the pokemon rather than accepting them swapped around
        String type1 = mExtras.getString("TYPE1");
        String type2 = mExtras.getString("TYPE2");
        if ((type1 != null) && (type2 == null)) {
            //conditionType = line[10].equalsIgnoreCase(type1) || line[11].equalsIgnoreCase(type1);
            selectionsList.add("LOWER("+PokedexDBHelper.COL_TYPE_1+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_TYPE_2+") LIKE ?");
            selectionArgsList.add(type1.toLowerCase());
            selectionArgsList.add(type1.toLowerCase());
        } else if ((type1 == null) && (type2 != null)) {
            //conditionType = line[10].equalsIgnoreCase(type2) || line[11].equalsIgnoreCase(type2);
            selectionsList.add("LOWER("+PokedexDBHelper.COL_TYPE_1+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_TYPE_2+") LIKE ?");
            selectionArgsList.add(type2.toLowerCase());
            selectionArgsList.add(type2.toLowerCase());
        } else if ((type1 != null) && (type2 != null)) {
            //conditionType = (line[10].equalsIgnoreCase(type1) || line[11].equalsIgnoreCase(type1)) &&
                    //(line[10].equalsIgnoreCase(type2) || line[11].equalsIgnoreCase(type2));
            selectionsList.add("(LOWER("+PokedexDBHelper.COL_TYPE_1+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_TYPE_2+") LIKE ?) " +
                    "AND (LOWER("+PokedexDBHelper.COL_TYPE_1+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_TYPE_2+") LIKE ?)");
            selectionArgsList.add(type1.toLowerCase());
            selectionArgsList.add(type1.toLowerCase());
            selectionArgsList.add(type2.toLowerCase());
            selectionArgsList.add(type2.toLowerCase());
        }

        String ability = mExtras.getString("ABILITY");
        if (ability != null) {
            //conditionAbility = line[12].equalsIgnoreCase(ability) ||
                    //line[13].equalsIgnoreCase(ability) ||
                    //line[14].equalsIgnoreCase(ability);
            selectionsList.add("LOWER("+PokedexDBHelper.COL_ABILITY_1+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_ABILITY_2+") LIKE ? " +
                    "OR LOWER("+PokedexDBHelper.COL_ABILITY_H+") LIKE ?");
            selectionArgsList.add(ability.toLowerCase());
            selectionArgsList.add(ability.toLowerCase());
            selectionArgsList.add(ability.toLowerCase());
        }

        //TODO: Add filtering for statistics

        String growth = mExtras.getString("GROWTH");
        if (growth != null) {
            //conditionGrowth = line[21].equalsIgnoreCase(InfoUtils.getAbbreviationFromGrowth(growth));
            selectionsList.add("LOWER("+PokedexDBHelper.COL_LEVELLING_RATE+") LIKE ?");
            selectionArgsList.add(InfoUtils.getAbbreviationFromGrowth(growth));
        }

        String generation = mExtras.getString("GENERATION");
        if (generation != null) {
            selectionsList.add(PokedexDBHelper.COL_GENERATION + "=?");
            selectionArgsList.add(String.valueOf(InfoUtils.getGenFromRoman(generation)));
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

        PokedexDBHelper helper = new PokedexDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                PokedexDBHelper.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_ID));
            String pkmnName = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_NAME));
            String form = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_FORM));
            MiniPokemon pokemon = new MiniPokemon(id, pkmnName, form);
            mArrayPokemon.add(pokemon);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void filterLearnsetData() {
        int moveId = mExtras.getInt("MOVE_ID");

        String gameFilter = "(" + LearnsetDBHelper.COL_VERSION_ID + "=" + AppConfig.GAME_VERSION_X_Y + ")";
        String learnFilter = "(" + LearnsetDBHelper.COL_LEARN_METHOD + "=" + AppConfig.LEARN_METHOD_LEVEL_UP +
                " OR " + LearnsetDBHelper.COL_LEARN_METHOD + "=" + AppConfig.LEARN_METHOD_MACHINE +
                " OR " + LearnsetDBHelper.COL_LEARN_METHOD + "=" + AppConfig.LEARN_METHOD_TUTOR + ")";
        String moveFilter = "(" + LearnsetDBHelper.COL_MOVE_ID + "=" + moveId + ")";

        String selection = gameFilter + " AND " + learnFilter + " AND " + moveFilter;

        mArrayPokemon.clear();

        LearnsetDBHelper helper = new LearnsetDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                LearnsetDBHelper.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(LearnsetDBHelper.COL_PKMN_ID));
            String form = cursor.getString(cursor.getColumnIndex(LearnsetDBHelper.COL_PKMN_FORM));
            MiniPokemon pokemon = new MiniPokemon(this, id, form);
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
            viewGroup.addView(getLayoutInflater().inflate(R.layout.fragment_misc_no_results, viewGroup, false)); // TODO: Does not work?
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
