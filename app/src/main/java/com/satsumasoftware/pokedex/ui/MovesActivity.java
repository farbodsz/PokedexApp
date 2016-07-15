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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.MovesDBHelper;
import com.satsumasoftware.pokedex.framework.move.MiniMove;
import com.satsumasoftware.pokedex.ui.adapter.FilterListItemVGAdapter;
import com.satsumasoftware.pokedex.ui.adapter.MoveDexAdapter;
import com.satsumasoftware.pokedex.ui.dialog.MoveDetailActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MovesActivity extends BaseActivity implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private static final String LOG_TAG = "MovesActivity.java";

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_MOVEDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }


    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private String mFilterSelectionName = "",
            mFilterSelectionType = "",
            mFilterSelectionGen = "";
    private boolean mSortByName;

    private ArrayList<MiniMove> mCurrList;

    private MovesDBHelper mDbHelper;


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

        mSortByName = PrefUtils.sortMovesAlphabetically(this);

        mDbHelper = new MovesDBHelper(this);
        populateList(mDbHelper.getAllMiniMoves());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        mNoResults = findViewById(R.id.main_frag_noResults);
    }

    private void populateList(ArrayList<MiniMove> items) {
        mCurrList = items;
        Collections.sort(items, new Comparator<MiniMove>() {
            @Override public int compare(MiniMove move1, MiniMove move2) {
                if (mSortByName) {
                    return move1.getName().compareTo(move2.getName()); // Ascending
                } else {
                    return move1.getId() - move2.getId(); // Ascending
                }
            }

        });
        final ArrayList<MiniMove> itemsFinal = items;
        MoveDexAdapter adapter = new MoveDexAdapter(itemsFinal);
        adapter.setOnRowClickListener(new MoveDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(MovesActivity.this, MoveDetailActivity.class);
                intent.putExtra(MoveDetailActivity.EXTRA_MOVE, itemsFinal.get(position));
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
        Spinner spinnerName = (Spinner) findViewById(R.id.filterDrawer_spinnerName);
        LinearLayout llTypes = (LinearLayout) findViewById(R.id.filterDrawer_llType_content);
        LinearLayout llGens = (LinearLayout) findViewById(R.id.filterDrawer_llGen_content);

        findViewById(R.id.filterDrawer_llNameList_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llGrowth_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llAdvanced_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llLocRegions_group).setVisibility(View.GONE);

        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(this, R.array.filter_name, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.filter_name);
                String text = array[position];
                if (text.equalsIgnoreCase("no filter")) {
                    mFilterSelectionName = "";
                } else {
                    mFilterSelectionName = "(LOWER(" + MovesDBHelper.COL_NAME + ") LIKE \"" +
                            text.toLowerCase() + "%\")";
                }
                updateFilteredList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FilterListItemVGAdapter typeAdapter = new FilterListItemVGAdapter(
                this, llTypes, R.array.list_types);
        typeAdapter.createListItems();
        typeAdapter.setOnFilterItemClickListener(this);

        FilterListItemVGAdapter gensAdapter = new FilterListItemVGAdapter(
                this, llGens, R.array.list_gens);
        gensAdapter.createListItems();
        gensAdapter.setOnFilterItemClickListener(this);
    }

    @Override
    public void onFilterItemClick(View view, int position, String text, boolean isChecked, View itemView) {
        switch (itemView.getId()) {
            case R.id.filterDrawer_llType_content:
                String typeQuery = "(" + MovesDBHelper.COL_TYPE_ID + "=\"" + DataUtils.typeToId(text) + "\")";
                mFilterSelectionType = reformatFilterSelection(mFilterSelectionType);
                if (isChecked) {
                    mFilterSelectionType = mFilterSelectionType + typeQuery;
                } else {
                    if (mFilterSelectionType.contains(" OR " + typeQuery)) {
                        mFilterSelectionType = mFilterSelectionType.replace(" OR " + typeQuery, "");
                    } else {
                        mFilterSelectionType = mFilterSelectionType.replace(typeQuery, "");
                    }
                }
                mFilterSelectionType = reformatFilterSelectionFinal(mFilterSelectionType);
                updateFilteredList();
                break;

            case R.id.filterDrawer_llGen_content:
                String genQuery = "(" + MovesDBHelper.COL_GENERATION_ID + "=\"" +
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
            //selection = "";
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
        if (mFilterSelectionType != null && !mFilterSelectionType.trim().equals("")) {
            if (filterCounter > 0) {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("(").append(mFilterSelectionType).append(")");
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
            mToolbar.setTitle(getResources().getString(R.string.title_activity_moves));
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
            populateList(mDbHelper.getAllMiniMoves());
            return;
        }

        mToolbar.setTitle(getResources().getString(R.string.title_filtered_moves));

        ArrayList<MiniMove> filteredList = new ArrayList<>();

        mDbHelper = new MovesDBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] columns = new String[] {
                MovesDBHelper.COL_ID,
                MovesDBHelper.COL_NAME,
                MovesDBHelper.COL_TYPE_ID,
                MovesDBHelper.COL_GENERATION_ID
        };
        Cursor cursor = db.query(
                MovesDBHelper.TABLE_NAME,
                columns,
                selection,
                null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovesDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(MovesDBHelper.COL_NAME));
            filteredList.add(new MiniMove(id, name));
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
