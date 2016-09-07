package com.satsumasoftware.pokedex.ui;

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

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.LocationsDBHelper;
import com.satsumasoftware.pokedex.framework.Region;
import com.satsumasoftware.pokedex.framework.location.Location;
import com.satsumasoftware.pokedex.ui.adapter.FilterListItemVGAdapter;
import com.satsumasoftware.pokedex.ui.adapter.LocationDexAdapter;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LocationsActivity extends BaseActivity implements FilterListItemVGAdapter.OnFilterItemClickListener {

    private static final String LOG_TAG = "LocationActivity.java";

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_LOCATIONDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }
    @Override
    protected boolean disableRightDrawerSwipe() { return true; }


    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private View mRootLayout;
    private View mNoResults;

    private String mFilterSelectionName = "",
            mFilterSelectionRegion = "";

    private LocationsDBHelper mDbHelper;


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

        DragScrollBar scrollBar = new DragScrollBar(this, mRecyclerView, false);
        scrollBar.addIndicator(new AlphabetIndicator(this), true);

        mDbHelper = LocationsDBHelper.getInstance(this);
        populateList(mDbHelper.getAllLocations());

        mDrawerLayout = getSelfDrawerLayout();
        setupFilterDrawer();

        mNoResults = findViewById(R.id.fragment_no_results);
    }

    private void populateList(ArrayList<Location> items) {
        Collections.sort(items, new Comparator<Location>() {
            @Override
            public int compare(Location location1, Location location2) {
                return location1.getName().compareTo(location2.getName()); // Ascending
            }
        });
        final ArrayList<Location> itemsFinal = items;
        LocationDexAdapter adapter = new LocationDexAdapter(this, itemsFinal);
        adapter.setOnEntryClickListener(new LocationDexAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(LocationsActivity.this, LocationDetailActivity.class);
                intent.putExtra(LocationDetailActivity.EXTRA_LOCATION, itemsFinal.get(position));
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
        Spinner spinnerName = (Spinner) findViewById(R.id.spinner_name);
        LinearLayout llRegions = (LinearLayout) findViewById(R.id.container_location_regions);

        findViewById(R.id.viewGroup_name_list).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_type).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_growth).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_generation).setVisibility(View.GONE);
        findViewById(R.id.viewGroup_advanced).setVisibility(View.GONE);

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
                    mFilterSelectionName = "(LOWER(" + LocationsDBHelper.COL_NAME + ") LIKE \"" +
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
            case R.id.container_location_regions:
                String regionQuery = "(" + LocationsDBHelper.COL_REGION_ID + "=\"" + new Region(text).getId() + "\")";
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

        mDbHelper = LocationsDBHelper.getInstance(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                LocationsDBHelper.TABLE_NAME,
                null,
                selection,
                null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(LocationsDBHelper.COL_ID));
            int regionId = cursor.getInt(cursor.getColumnIndex(LocationsDBHelper.COL_REGION_ID));
            String name = cursor.getString(cursor.getColumnIndex(LocationsDBHelper.COL_NAME));
            filteredList.add(new Location(id, regionId, name));
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
