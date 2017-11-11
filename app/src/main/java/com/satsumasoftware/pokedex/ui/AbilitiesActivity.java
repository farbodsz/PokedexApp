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

package com.satsumasoftware.pokedex.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.query.Filter;
import com.satsumasoftware.pokedex.query.Filters;
import com.satsumasoftware.pokedex.query.Query;
import com.satsumasoftware.pokedex.ui.adapter.AbilityDexAdapter;
import com.satsumasoftware.pokedex.ui.adapter.FilterListItemVGAdapter;
import com.satsumasoftware.pokedex.ui.dialog.AbilityDetailActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
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

public class AbilitiesActivity extends BaseActivity
        implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private DragScrollBar mScrollBar;

    private Query.Builder mQueryBuilder = new Query.Builder();
    private boolean mSortByName;

    private ArrayList<MiniAbility> mCurrList;

    private AbilitiesDBHelper mDbHelper;


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

        mSortByName = PrefUtils.sortAbilitiesAlphabetically(this);

        mDbHelper = AbilitiesDBHelper.getInstance(this);
        populateList(mDbHelper.getAllMiniAbilities());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        mNoResults = findViewById(R.id.fragment_no_results);

        updateFilteredList();
        // The above is required specifically in Abilities (filtering) as the filter
        // does not contain a Spinner (for name filtering), so `updateFilteredList()` will not get
        // called when the Activity starts, which means that the "No results found" page would remain
        // visible
    }

    private void populateList(ArrayList<MiniAbility> items) {
        mCurrList = items;
        Collections.sort(items, new Comparator<MiniAbility>() {
            @Override public int compare(MiniAbility ability1, MiniAbility ability2) {
                if (mSortByName) {
                    return ability1.getName().compareTo(ability2.getName()); // Ascending
                } else {
                    return ability1.getId() - ability2.getId(); // Ascending
                }
            }
        });
        final ArrayList<MiniAbility> itemsFinal = items;
        AbilityDexAdapter adapter = new AbilityDexAdapter(this, itemsFinal);
        adapter.setOnRowClickListener(new AbilityDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(AbilitiesActivity.this, AbilityDetailActivity.class);
                intent.putExtra(AbilityDetailActivity.EXTRA_ABILITY, itemsFinal.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);

        mScrollBar.removeIndicator()
                .addIndicator(mSortByName ?
                        new AlphabetIndicator(this) : new CustomIndicator(this), true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (mSortByName) {
            menu.findItem(R.id.action_sort_name).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
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
        LinearLayout llNames = (LinearLayout) findViewById(R.id.container_name);
        LinearLayout llGens = (LinearLayout) findViewById(R.id.container_generations);

        findViewById(R.id.viewGroup_name_spinner).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_type).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_growth).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_advanced).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_location_region).setVisibility(View.GONE);

        FilterListItemVGAdapter nameAdapter = new FilterListItemVGAdapter(
                this, llNames, R.array.list_letters);
        nameAdapter.createListItems();
        nameAdapter.setOnFilterItemClickListener(this);

        FilterListItemVGAdapter gensAdapter = new FilterListItemVGAdapter(
                this, llGens, R.array.list_gens_abilities);
        gensAdapter.createListItems();
        gensAdapter.setOnFilterItemClickListener(this);
    }

    @Override
    public void onFilterItemClick(View view, int position, String text, boolean isChecked,
                                  View itemView) {
        switch (itemView.getId()) {
            case R.id.container_name:
                Filter nameFilter = Filters.startsWith(AbilitiesDBHelper.COL_NAME, text);
                if (isChecked) {
                    mQueryBuilder.addFilter(nameFilter);
                } else {
                    mQueryBuilder.removeFilter(nameFilter);
                }
                updateFilteredList();
                break;

            case R.id.container_generations:
                Filter genFilter = Filters.equal(AbilitiesDBHelper.COL_GENERATION_ID,
                        String.valueOf(DataUtilsKt.romanToGenId(text)));
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
            mToolbar.setTitle(getResources().getString(R.string.title_activity_abilities));
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
            populateList(mDbHelper.getAllMiniAbilities());
            return;
        }

        mToolbar.setTitle(getResources().getString(R.string.title_filtered_abilities));

        ArrayList<MiniAbility> filteredList = new ArrayList<>();

        mDbHelper = AbilitiesDBHelper.getInstance(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] columns = new String[] {
                AbilitiesDBHelper.COL_ID,
                AbilitiesDBHelper.COL_NAME,
                AbilitiesDBHelper.COL_GENERATION_ID
        };

        Cursor cursor = db.query(
                AbilitiesDBHelper.TABLE_NAME,
                columns,
                mQueryBuilder.build().getFilter().getSqlStatement(),
                null, null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(AbilitiesDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(AbilitiesDBHelper.COL_NAME));
            filteredList.add(new MiniAbility(id, name));
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
        return NAVDRAWER_ITEM_ABILITYDEX;
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
