package com.satsumasoftware.pokedex.ui.filter;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.ui.AboutActivity;
import com.satsumasoftware.pokedex.ui.DetailActivity;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private ArrayList<MiniPokemon> mArrayPokemon = new ArrayList<>();

    private ActionBar mActionBar;
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
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.filteredInfo_progress);
        progressBar.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.filteredInfo_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setVisibility(View.GONE);

        handleSearchIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY).trim();
            mActionBar.setTitle(
                    getResources().getString(R.string.title_activity_search_results) + " \"" + query + "\"");
            doSearch(query);
            populateList(mArrayPokemon);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void doSearch(String searchQuery) {
        PokemonDBHelper helper = new PokemonDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = PokemonDBHelper.COL_ID + " LIKE '%"+InfoUtils.unformatPokemonId(searchQuery)+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_JAPANESE+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_ROMAJI+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_KOREAN+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_CHINESE+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_FRENCH+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_GERMAN+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_SPANISH+") LIKE '%"+searchQuery.toLowerCase()+"%' OR " +
                "LOWER("+ PokemonDBHelper.COL_NAME_ITALIAN+") LIKE '%"+searchQuery.toLowerCase()+"%'";

        Cursor cursor = db.query(
                PokemonDBHelper.TABLE_NAME,
                MiniPokemon.DB_COLUMNS,
                selection,
                null,
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
            String nameAndForm = cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
            int pokedexNumber = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
            if (TextUtils.isDigitsOnly(searchQuery)) {
                String formattedId = InfoUtils.formatPokemonId(pokedexNumber);
                if (formattedId.contains(searchQuery)) {
                    MiniPokemon pokemon = new MiniPokemon(id, speciesId, formId, pkmnName, form,
                            nameAndForm, pokedexNumber);
                    mArrayPokemon.add(pokemon);
                }
            } else {
                MiniPokemon pokemon = new MiniPokemon(id, speciesId, formId, pkmnName, form,
                        nameAndForm, pokedexNumber);
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
                    Intent intent = new Intent(SearchResultsActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POKEMON, pokemonList.get(position));
                    startActivity(intent);
                }
            });
            adapter.setOnEntryLongClickListener(new PokedexAdapter.OnEntryLongClickListener() {
                @Override
                public void onEntryLongClick(View view, int position) {
                    MiniPokemon thisPokemon = pokemonList.get(position);
                    ActionUtils.handleListLongClick(SearchResultsActivity.this, thisPokemon, mRootLayout);
                }
            });
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_buyPro = menu.findItem(R.id.action_buyPro);
        if (Flavours.type == Flavours.Type.PAID) {
            item_buyPro.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_buyPro:
                AlertUtils.buyPro(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
