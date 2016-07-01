package com.phoenixenterprise.pokedex;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.phoenixenterprise.pokedex.adapter.FilterListItemVGAdapter;
import com.phoenixenterprise.pokedex.adapter.LocationDexAdapter;
import com.phoenixenterprise.pokedex.db.LocationDBHelper;
import com.phoenixenterprise.pokedex.misc.DividerItemDecoration;
import com.phoenixenterprise.pokedex.object.Location;
import com.phoenixenterprise.pokedex.util.AdUtils;
import com.phoenixenterprise.pokedex.util.AlertUtils;
import com.phoenixenterprise.pokedex.util.Flavours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class LocationsActivity extends BaseActivity implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private static final String LOG_TAG = "LocationActivity.java";

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.main_toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.main_drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_LOCATIONDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.main_navigationView); }


    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private String mFilterSelectionName = "",
            mFilterSelectionRegion = "";

    private LocationDBHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_filterable);
        mRootLayout = findViewById(R.id.main_drawerLayout);

        AdUtils.setupAds(this, R.id.main_adView);

        mToolbar = getSelfToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mDbHelper = new LocationDBHelper(this);
        populateList(mDbHelper.getAllLocations());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        mNoResults = findViewById(R.id.main_frag_noResults);
    }

    private void populateList(ArrayList<Location> items) {
        Collections.sort(items, new Comparator<Location>() {
            @Override
            public int compare(Location location1, Location location2) {
                return location1.getLocation().compareTo(location2.getLocation()); // Ascending
            }
        });
        final ArrayList<Location> itemsFinal = items;
        LocationDexAdapter adapter = new LocationDexAdapter(itemsFinal, true);
        adapter.setOnRowClickListener(new LocationDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(LocationsActivity.this, LocationDetailActivity.class);
                intent.putExtra("LOCATION", itemsFinal.get(position));
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
            item_filter.setIcon(R.drawable.ic_filter_list_grey600_48dp);
        }

        menu.findItem(R.id.action_sort).setVisible(false);

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
        LinearLayout llRegions = (LinearLayout) findViewById(R.id.filterDrawer_llLocRegions_content);

        findViewById(R.id.filterDrawer_llNameList_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llType_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llGrowth_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llGen_group).setVisibility(View.GONE);
        findViewById(R.id.filterDrawer_llAdvanced_group).setVisibility(View.GONE);

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
                    mFilterSelectionName = "(LOWER(" + LocationDBHelper.COL_NAME + ") LIKE \"" +
                            text.toLowerCase() + "%\")";
                }
                updateFilteredList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FilterListItemVGAdapter regionAdapter = new FilterListItemVGAdapter(
                this, llRegions, R.array.list_location_regions);
        regionAdapter.createListItems();
        regionAdapter.setOnFilterItemClickListener(this);
    }

    @Override
    public void onFilterItemClick(View view, int position, String text, boolean isChecked, View itemView) {
        switch (itemView.getId()) {
            case R.id.filterDrawer_llLocRegions_content:
                String regionQuery = "(LOWER(" + LocationDBHelper.COL_REGION + ")=\"" + text.toLowerCase() + "\")";
                mFilterSelectionRegion = reformatFilterSelection(mFilterSelectionRegion);
                if (isChecked) {
                    mFilterSelectionRegion = mFilterSelectionRegion + regionQuery;
                } else {
                    if (mFilterSelectionRegion.contains(" OR " + regionQuery)) {
                        mFilterSelectionRegion = mFilterSelectionRegion.replace(" OR " + regionQuery, "");
                    } else {
                        mFilterSelectionRegion = mFilterSelectionRegion.replace(regionQuery, "");
                    }
                }
                mFilterSelectionRegion = reformatFilterSelectionFinal(mFilterSelectionRegion);
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
        if (mFilterSelectionRegion != null && !mFilterSelectionRegion.trim().equals("")) {
            if (filterCounter > 0) {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("(").append(mFilterSelectionRegion).append(")");
            // filterCounter++; <-- doesn't make a difference
        }

        String selection = stringBuilder.toString();

        Log.d(LOG_TAG, "updateFilteredList | selection [middle]: " + selection);

        if (selection.trim().equals("")) {
            mToolbar.setTitle(getResources().getString(R.string.title_activity_locations));
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
            populateList(mDbHelper.getAllLocations());
            return;
        }

        mToolbar.setTitle(getResources().getString(R.string.title_filtered_locations));

        ArrayList<Location> filteredList = new ArrayList<>();

        mDbHelper = new LocationDBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                LocationDBHelper.TABLE_NAME,
                null,
                selection,
                null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(LocationDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(LocationDBHelper.COL_NAME));
            String region = cursor.getString(cursor.getColumnIndex(LocationDBHelper.COL_REGION));
            filteredList.add(new Location(id, name, region));
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
}
