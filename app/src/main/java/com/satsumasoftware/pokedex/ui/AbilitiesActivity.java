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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.ui.adapter.AbilityDexAdapter;
import com.satsumasoftware.pokedex.ui.adapter.FilterListItemVGAdapter;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AbilitiesActivity extends BaseActivity implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private static final String LOG_TAG = "AbilitiesActivity.java";

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_ABILITYDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }


    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private String mFilterSelectionName = "",
            mFilterSelectionGen = "";
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

        mSortByName = PrefUtils.sortAbilitiesAlphabetically(this);

        mDbHelper = new AbilitiesDBHelper(this);
        populateList(mDbHelper.getAllMiniAbilities());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        mNoResults = findViewById(R.id.main_frag_noResults);

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
        AbilityDexAdapter adapter = new AbilityDexAdapter(itemsFinal);
        adapter.setOnRowClickListener(new AbilityDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(AbilitiesActivity.this, AbilityDetailActivity.class);
                intent.putExtra(AbilityDetailActivity.EXTRA_ABILITY, itemsFinal.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
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
        LinearLayout llNames = (LinearLayout) findViewById(R.id.filterDrawer_llName_content);
        LinearLayout llGens = (LinearLayout) findViewById(R.id.filterDrawer_llGen_content);

        findViewById(R.id.filterDrawer_llNameSpinners_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llType_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llGrowth_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llAdvanced_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llLocRegions_group).setVisibility(View.GONE);

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
    public void onFilterItemClick(View view, int position, String text, boolean isChecked, View itemView) {
        switch (itemView.getId()) {
            case R.id.filterDrawer_llName_content:
                String nameQuery = "(LOWER(" + AbilitiesDBHelper.COL_NAME + ") LIKE \"" +
                        text.toLowerCase() + "%\")";
                mFilterSelectionName = reformatFilterSelection(mFilterSelectionName);
                if (isChecked) {
                    mFilterSelectionName = mFilterSelectionName + nameQuery;
                } else {
                    if (mFilterSelectionName.contains(" OR " + nameQuery)) {
                        mFilterSelectionName = mFilterSelectionName.replace(" OR " + nameQuery, "");
                    } else {
                        mFilterSelectionName = mFilterSelectionName.replace(nameQuery, "");
                    }
                }
                mFilterSelectionName = reformatFilterSelectionFinal(mFilterSelectionName);
                Log.d(LOG_TAG, "About to do updateFilteredList");
                updateFilteredList();
                break;

            case R.id.filterDrawer_llGen_content:
                String genQuery = "(" + AbilitiesDBHelper.COL_GENERATION_ID + "=\"" +
                        InfoUtils.getGenFromRoman(text) + "\")";
                mFilterSelectionGen = reformatFilterSelection(mFilterSelectionGen);
                if (isChecked) {
                    mFilterSelectionGen = mFilterSelectionGen + genQuery;
                } else {
                    if (mFilterSelectionGen.contains(" OR " + genQuery)) {
                        mFilterSelectionGen = mFilterSelectionGen.replace(" OR " + genQuery, "");
                    } else {
                        mFilterSelectionGen = mFilterSelectionGen.replace(genQuery, "");
                    }
                }
                mFilterSelectionGen = reformatFilterSelectionFinal(mFilterSelectionGen);
                Log.d(LOG_TAG, "About to do updateFilteredList");
                updateFilteredList();
                break;
        }
    }

    private String reformatFilterSelection(String selection) {
        Log.d(LOG_TAG, "reformatFilterSelection | [start]: " + selection);
        if (!selection.trim().equals("")) {
            selection = selection + " OR ";
        }
        if (selection.trim().startsWith("OR ")) {
            selection = selection.replaceFirst("OR ", "");
        }
        Log.d(LOG_TAG, "reformatFilterSelection | [end]: " + selection);
        return selection;
    }

    private String reformatFilterSelectionFinal(String firstSelection) {
        Log.d(LOG_TAG, "reformatFilterSelectionFinal -- START");
        String selection = reformatFilterSelection(firstSelection);
        Log.d(LOG_TAG, "reformatFilterSelectionFinal | selection: " + selection);
        Log.d(LOG_TAG, "reformatFilterSelectionFinal | sele...endsWith('OR')?: " + selection.trim().endsWith("OR"));
        while (selection.trim().endsWith("OR")) {
            String[] words = selection.split(" ");
            String lastWord = words[words.length-1].trim();
            Log.d(LOG_TAG, "reformatFilterSelectionFinal | lastWord: " + lastWord);
            words[words.length-1] = lastWord.replace("OR", "");
            StringBuilder newSelection = new StringBuilder();
            for (String word : words) {
                newSelection.append(word).append(" ");
            }
            selection = newSelection.toString().trim();
        }
        Log.d(LOG_TAG, "reformatFilterSelectionFinal -- DONE");
        return selection;
    }

    private void updateFilteredList() {
        StringBuilder stringBuilder = new StringBuilder();
        int filterCounter = 0;
        if (mFilterSelectionName != null && !mFilterSelectionName.trim().equals("")) {
            stringBuilder.append("(").append(mFilterSelectionName).append(")");
            filterCounter++;
        }
        if (mFilterSelectionGen != null && !mFilterSelectionGen.trim().equals("")) {
            if (filterCounter > 0) {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("(").append(mFilterSelectionGen).append(")");
            // filterCounter++; <-- doesn't make a difference
        }

        String selection = stringBuilder.toString();

        Log.d(LOG_TAG, "updateFilteredList | selection [middle]: " + selection);

        if (selection.trim().equals("")) {
            mToolbar.setTitle(getResources().getString(R.string.title_activity_abilities));
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
            populateList(mDbHelper.getAllMiniAbilities());
            return;
        }

        mToolbar.setTitle(getResources().getString(R.string.title_filtered_abilities));

        ArrayList<MiniAbility> filteredList = new ArrayList<>();

        mDbHelper = new AbilitiesDBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] columns = new String[] {
                AbilitiesDBHelper.COL_ID,
                AbilitiesDBHelper.COL_NAME,
                AbilitiesDBHelper.COL_GENERATION_ID
        };
        Cursor cursor = db.query(
                AbilitiesDBHelper.TABLE_NAME,
                columns,
                selection,
                null,
                null, null, null
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
}
