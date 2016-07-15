package com.satsumasoftware.pokedex.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.ui.card.DetailCard;
import com.satsumasoftware.pokedex.ui.card.LocationDetail;
import com.satsumasoftware.pokedex.framework.encounter.CompactEncounterDataHolder;
import com.satsumasoftware.pokedex.framework.encounter.Encounter;
import com.satsumasoftware.pokedex.framework.encounter.EncounterDataHolder;
import com.satsumasoftware.pokedex.framework.encounter.EncounterMethodProse;
import com.satsumasoftware.pokedex.framework.encounter.EncounterSlot;
import com.satsumasoftware.pokedex.framework.location.Location;
import com.satsumasoftware.pokedex.framework.location.LocationArea;
import com.satsumasoftware.pokedex.ui.adapter.DetailAdapter;
import com.satsumasoftware.pokedex.util.DataUtils;

import java.util.ArrayList;
import java.util.Locale;

public class LocationDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION = "LOCATION";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LocationAreaPagerAdapter mPagerAdapter;

    private ProgressBar mProgress;
    private FrameLayout mNoPkmnMessage;

    private Location mLocation;

    private ArrayList<LocationArea> mLocationAreas;

    private AsyncTask<Void, Integer, Void> mAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        mLocation = getIntent().getExtras().getParcelable(EXTRA_LOCATION);
        if (mLocation == null) {
            throw new NullPointerException("Parcelable Location object through Intent is null");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.locationDetail_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(mLocation.getName());
        getSupportActionBar().setSubtitle(DataUtils.regionIdToString(mLocation.getRegionId()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.locationDetail_tabs);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager = (ViewPager) findViewById(R.id.locationDetail_viewpager);
        mPagerAdapter = new LocationAreaPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);

        mProgress = (ProgressBar) findViewById(R.id.locationDetail_progress);
        mNoPkmnMessage = (FrameLayout) findViewById(R.id.locationDetail_fl_noPokemon);

        // TODO: Change the database so versions correspond with edited values (i.e. ignoring Conquest series)

        mAsyncTask = new AsyncTask<Void, Integer, Void>() {
            ArrayList<ArrayList<DetailCard>> locationDetailsList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                locationDetailsList = fetchData();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mProgress.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                setupLayouts(locationDetailsList);
            }
        }.execute();
    }

    private ArrayList<ArrayList<DetailCard>> fetchData() {
        mLocationAreas = mLocation.getLocationAreas(getBaseContext());

        ArrayList<ArrayList<DetailCard>> locationDetailsList = new ArrayList<>();

        PokeDB pokeDB = new PokeDB(this);

        for (LocationArea locationArea : mLocationAreas) {

            ArrayList<Integer> versions = getVersionsAtLocationArea(locationArea.getId(), pokeDB);
            int versionId = versions.get(versions.size() - 1);  // by default, the selected version is the latest

            // get all the encounters using locationAreaId and versionId
            ArrayList<Encounter> encounters = locationArea.findAllEncounters(this, versionId);

            // for each encounter, get its corresponding encounter slot - put these into the data holder
            ArrayList<EncounterDataHolder> encounterDataList = new ArrayList<>();
            for (Encounter encounter : encounters) {
                EncounterSlot encounterSlot = encounter.getEncounterSlotObject(this);
                encounterDataList.add(new EncounterDataHolder(encounter, encounterSlot));
            }

            // organise data by encounter method with a sparse array
            SparseArray<ArrayList<EncounterDataHolder>> organisedEncounterData = new SparseArray<>();
            for (EncounterDataHolder encounterDataHolder : encounterDataList) {

                int encounterMethodId = encounterDataHolder.getEncounterSlot().getEncounterMethodId();
                ArrayList<EncounterDataHolder> dataList = organisedEncounterData.get(encounterMethodId);

                if (dataList == null) {
                    ArrayList<EncounterDataHolder> newDataList = new ArrayList<>();
                    newDataList.add(encounterDataHolder);
                    organisedEncounterData.put(encounterMethodId, newDataList);
                } else {
                    organisedEncounterData.remove(encounterMethodId);
                    dataList.add(encounterDataHolder);
                    organisedEncounterData.put(encounterMethodId, dataList);
                }
            }

            // create location detail objects using the organised data
            ArrayList<DetailCard> locationDetails = new ArrayList<>();
            for (int i = 0; i < organisedEncounterData.size(); i++) {
                int encounterMethodId = organisedEncounterData.keyAt(i);
                String name = new EncounterMethodProse(this, encounterMethodId).getName();

                // make encounter data holder lists into shorter 'compact' data list(s) -
                // each compact holder object represents many encounter data holder objects
                // about the same pokemon (same pokemon id)
                ArrayList<EncounterDataHolder> encounterDataHolders =
                        organisedEncounterData.get(encounterMethodId);

                SparseArray<CompactEncounterDataHolder> compactHolderArray = new SparseArray<>();
                for (EncounterDataHolder encounterHolder : encounterDataHolders) {
                    int pokemonId = encounterHolder.getEncounter().getPokemonId();

                    if (compactHolderArray.get(pokemonId) == null) {
                        CompactEncounterDataHolder compactHolder =
                                new CompactEncounterDataHolder(pokemonId);
                        compactHolder.addEncounterDataHolder(encounterHolder);
                        compactHolderArray.put(pokemonId, compactHolder);
                    } else {
                        CompactEncounterDataHolder compactHolder =
                                compactHolderArray.get(pokemonId);
                        compactHolder.addEncounterDataHolder(encounterHolder);
                        compactHolderArray.remove(pokemonId);
                        compactHolderArray.put(pokemonId, compactHolder);
                    }
                }

                locationDetails.add(new LocationDetail(name, compactHolderArray));
            }

            // add to the list
            locationDetailsList.add(locationDetails);
        }

        return locationDetailsList;
    }

    private ArrayList<Integer> getVersionsAtLocationArea(int locationAreaId, PokeDB pokeDB) {
        ArrayList<Integer> versionIds = new ArrayList<>();
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Encounters.TABLE_NAME,
                new String[] {PokeDB.Encounters.COL_VERSION_ID, PokeDB.Encounters.COL_LOCATION_AREA_ID},
                PokeDB.Encounters.COL_LOCATION_AREA_ID + "=?",
                new String[] {String.valueOf(locationAreaId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int versionId = cursor.getInt(cursor.getColumnIndex(PokeDB.Encounters.COL_VERSION_ID));
            if (!versionIds.contains(versionId)) {
                versionIds.add(versionId);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return versionIds;
    }

    private void setupLayouts(ArrayList<ArrayList<DetailCard>> locationDetailsList) {
        if (locationDetailsList.isEmpty()) {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mNoPkmnMessage.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < mLocationAreas.size(); i++) {
            LocationArea locationArea = mLocationAreas.get(i);
            ArrayList<DetailCard> locationDetails = locationDetailsList.get(i);

            View aPage = getLayoutInflater().inflate(R.layout.fragment_detail_location, null);
            RecyclerView recyclerView = (RecyclerView) aPage.findViewById(R.id.recyclerView);

            // stop recycling of views (which causes a bug where views are merged together)
            recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

            // setup the RecyclerView and adapter
            DetailAdapter adapter = new DetailAdapter(this, locationDetails);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            // get the title of the tab
            String title = (locationArea.getName().trim().equals("") && mLocationAreas.size() != 1) ?
                    "General" : locationArea.getName();

            mPagerAdapter.addViewWithTitle(aPage, title);
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.mdu_text_white_secondary),
                ContextCompat.getColor(this, R.color.mdu_text_white));

        if (mPagerAdapter.getCount() == 1) {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
    }


    public class LocationAreaPagerAdapter extends PagerAdapter {

        private ArrayList<View> mViews = new ArrayList<>();
        private ArrayList<String> mTitles = new ArrayList<>();

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public int getItemPosition(Object object) {
            int index = mViews.indexOf(object);
            if (index == -1) {
                return POSITION_NONE;
            } else {
                return index;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return mTitles.get(position).toUpperCase(l);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int addView(View view) {
            return addView(view, mViews.size());
        }

        public int addViewWithTitle(View view, String title) {
            return addViewWithTitle(view, mViews.size(), title);
        }

        public int addView(View view, int position) {
            return addViewWithTitle(view, position, "");
        }

        public int addViewWithTitle(View view, int position, String title) {
            mViews.add(position, view);
            mTitles.add(title);
            notifyDataSetChanged();
            return position;
        }

        public int removeView(ViewPager pager, View view) {
            return removeView(pager, mViews.indexOf(view));
        }

        public int removeView(ViewPager pager, int position) {
            pager.setAdapter(null);
            mViews.remove(position);
            pager.setAdapter(this);
            return position;
        }

        public View getView(int position) {
            return mViews.get(position);
        }
    }

}
