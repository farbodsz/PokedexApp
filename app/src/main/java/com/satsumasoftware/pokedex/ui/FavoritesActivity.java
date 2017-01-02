package com.satsumasoftware.pokedex.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.ui.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.FavoriteUtils;

import java.util.ArrayList;


public class FavoritesActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private boolean isListEmpty = false,
            isRecyclerRemoved = false;

    private View mRootLayout;

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_FAVOURITES; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootLayout = findViewById(R.id.drawerLayout);

        AdUtils.setupAds(this, R.id.adView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupList();
    }

    private void setupList() {
        final ArrayList<MiniPokemon> pokemonList = FavoriteUtils.getFavouritePkmnArrayLists(this);

        if (pokemonList.size() == 0) {
            if (mRecyclerView != null && !isRecyclerRemoved) {
                // If resuming, Recycler may have already been removed so we only do this if it hasn't been removed
                ViewGroup viewGroup = (ViewGroup) mRecyclerView.getParent();
                viewGroup.removeView(mRecyclerView);
                getLayoutInflater().inflate(R.layout.fragment_favourites_empty, viewGroup, true);
                isRecyclerRemoved = true;
                isListEmpty = true;
                invalidateOptionsMenu();
                View favouritesPrompt = findViewById(R.id.favourites_empty_view);
                favouritesPrompt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), PokedexActivity.class));
                        Toast.makeText(getBaseContext(), R.string.add_favourites_description, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
            return;
        }

        PokedexAdapter adapter = new PokedexAdapter(this, pokemonList);
        adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(FavoritesActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POKEMON, pokemonList.get(position));
                startActivity(intent);
            }
        });
        adapter.setOnEntryLongClickListener(new PokedexAdapter.OnEntryLongClickListener() {
            @Override
            public void onEntryLongClick(View view, int position) {
                MiniPokemon thisPokemon = pokemonList.get(position);
                boolean wasFavourite = FavoriteUtils.isAFavouritePkmn(getBaseContext(), thisPokemon);
                ActionUtils.handleListLongClick(FavoritesActivity.this, thisPokemon, mRootLayout);
                boolean isFavourite = FavoriteUtils.isAFavouritePkmn(getBaseContext(), thisPokemon);
                if (wasFavourite != isFavourite) { // TODO: This doesn't work - refresh the list properly
                    refresh();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void refresh() {
        startActivity(new Intent(this, FavoritesActivity.class));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favourites, menu);

        MenuItem itemClearAll = menu.findItem(R.id.action_clearList);
        if (isListEmpty) {
            itemClearAll.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clearList:
                confirmClearList();
                setupList();
                break;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmClearList() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_favorites_title)
                .setMessage(R.string.clear_favorites_description)
                .setPositiveButton(R.string.action_clear_all, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FavoriteUtils.clearFavouritePkmnList(FavoritesActivity.this);
                        setupList();
                        Snackbar.make(mRootLayout, R.string.favourites_cleared, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action_undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FavoriteUtils.restoreBackupFavouritePkmnList(FavoritesActivity.this);
                                        refresh();
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Does nothing
                    }
                })
                .show();
    }
}
