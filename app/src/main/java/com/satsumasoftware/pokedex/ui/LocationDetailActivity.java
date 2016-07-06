package com.satsumasoftware.pokedex.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.detail.DetailInfo;
import com.satsumasoftware.pokedex.framework.detail.LocationDetail;
import com.satsumasoftware.pokedex.framework.encounter.DisplayedEncounter;
import com.satsumasoftware.pokedex.framework.encounter.Encounter;
import com.satsumasoftware.pokedex.framework.encounter.EncounterSlot;
import com.satsumasoftware.pokedex.framework.location.Location;
import com.satsumasoftware.pokedex.framework.location.LocationArea;
import com.satsumasoftware.pokedex.ui.adapter.DetailAdapter;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class LocationDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LocationDetailActivity";

    public static final String EXTRA_LOCATION = "LOCATION";

    private static final int GAME_VERSION_ID = 14;  // TODO: replace with user input or preference

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LocationAreaPagerAdapter mPagerAdapter;

    //private ArrayList<ArrayList<EncounterInfo>> mEncounterDisplayedArrArr;
    //private ArrayList<String> mLocationAreaTitles;

    private ProgressBar mProgress;
    private FrameLayout mNoPkmnMessage;

    private Location mLocation;

    private ArrayList<LocationArea> mLocationAreas;
    private ArrayList<DisplayedEncounter> mDisplayedEncounters;

    private int mGameVersion;

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

        mDisplayedEncounters = new ArrayList<>();

        //mEncounterDisplayedArrArr = new ArrayList<>();
        //mLocationAreaTitles = new ArrayList<>();


        // TODO: Change the database so versions correspond with edited values (i.e. ignoring Conquest series)

        fetchData();
    }

    private void fetchData() {
        mAsyncTask = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.d(LOG_TAG, "========== Location - " + mLocation.getName() + "==========");

                mLocationAreas = mLocation.getLocationAreas(getBaseContext());

                Log.d(LOG_TAG, "Location Areas: " + mLocationAreas.size());

                for (LocationArea locationArea : mLocationAreas) {
                    Log.d(LOG_TAG, "----- Location Area: " + locationArea.getName() + "-----");

                    ArrayList<Integer> versions = locationArea.findEncounterGameVersions(getBaseContext());
                    Collections.sort(versions);
                    int latestVersion = versions.get(versions.size()-1);
                    Log.d(LOG_TAG, "Searching with version: " + latestVersion);

                    ArrayList<Encounter> encounters = locationArea.findAllEncounters(getBaseContext(), latestVersion);
                    Log.d(LOG_TAG, "- Encounters Size: " + encounters.size());

                    for (Encounter encounter : encounters) {
                        mDisplayedEncounters.add(encounter.toDisplayedEncounter(getBaseContext(), locationArea));
                        Log.d(LOG_TAG, "----------");
                        Log.d(LOG_TAG, "Pokemon Id " + encounter.getPokemonId());
                        Log.d(LOG_TAG, "Min level " + encounter.getMinLevel());
                        Log.d(LOG_TAG, "Max level " + encounter.getMaxLevel());

                        EncounterSlot encounterSlot = new EncounterSlot(getBaseContext(), encounter.getEncounterSlotId());
                        Log.d(LOG_TAG, " - EncounterSlot info - ");
                        Log.d(LOG_TAG, "Rarity: " + encounterSlot.getRarity());
                        Log.d(LOG_TAG, "MethodId: " + encounterSlot.getEncounterMethodId());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                displayData();
            }
        }.execute();
    }

    private void displayData() {
        final LayoutInflater inflater = LayoutInflater.from(this);

        for (LocationArea locationArea : mLocationAreas) {
            final String locationAreaName = locationArea.getName();

            mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                View page;
                RecyclerView recyclerView;
                ProgressBar progressBar;
                ArrayList<DetailInfo> locationDetails = new ArrayList<>();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    page = inflater.inflate(R.layout.fragment_detail_main, null);
                    page.findViewById(R.id.appBarLayout_secondary);

                    progressBar = (ProgressBar) page.findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                    recyclerView = (RecyclerView) page.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new GridLayoutManager(
                            LocationDetailActivity.this, getResources().getInteger(R.integer.detail_columns)));
                    recyclerView.setVisibility(View.GONE);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    // TODO optimise
                    ArrayList<Integer> methodIds = new ArrayList<>();
                    ArrayMap<Integer, ArrayList<DisplayedEncounter>> deArrayMap = new ArrayMap<>();
                    for (DisplayedEncounter de : mDisplayedEncounters) {
                        int methodId = de.getEncounterSlot().getEncounterMethodId();
                        if (!methodIds.contains(methodId)) {
                            methodIds.add(de.getEncounterSlot().getEncounterMethodId());
                        }
                        //deArrayArray.get(methodId).add(de);
                        if (deArrayMap.containsKey(methodId)) {
                            ArrayList<DisplayedEncounter> deArray = deArrayMap.get(methodId);
                            deArray.add(de);
                            deArrayMap.remove(methodId);
                            deArrayMap.put(methodId, deArray);
                        } else {
                            ArrayList<DisplayedEncounter> deArray = new ArrayList<>();
                            deArray.add(de);
                            deArrayMap.put(methodId, deArray);
                        }
                    }

                    for (int methodId : methodIds) {
                        ArrayList<DisplayedEncounter> theseEncounters = deArrayMap.get(methodId);
                        String title = InfoUtils.getEncounterMethodFromId(methodId);
                        DetailInfo locationDetail = new LocationDetail(title, theseEncounters);
                        locationDetails.add(locationDetail);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    // stop recycling of views (which causes a bug where views are merged together)
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

                    // setup RecyclerView
                    DetailAdapter adapter = new DetailAdapter(getBaseContext(), locationDetails);
                    recyclerView.setAdapter(adapter);

                    String title;
                    if (locationAreaName.equals("")) {
                        title = "General";
                    } else {
                        title = locationAreaName;
                    }
                    mPagerAdapter.addViewWithTitle(page, title);
                }
            }.execute();
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
