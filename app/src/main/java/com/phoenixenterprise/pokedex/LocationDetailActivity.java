package com.phoenixenterprise.pokedex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.adapter.EncounterVGAdapter;
import com.phoenixenterprise.pokedex.db.internal.EncounterDBHelper;
import com.phoenixenterprise.pokedex.db.internal.LocationAreaDBHelper;
import com.phoenixenterprise.pokedex.object.EncounterInfo;
import com.phoenixenterprise.pokedex.object.EncounterItem;
import com.phoenixenterprise.pokedex.object.EncounterItemSlot;
import com.phoenixenterprise.pokedex.object.Location;
import com.phoenixenterprise.pokedex.object.MiniPokemon;
import com.phoenixenterprise.pokedex.object.internal.Encounter;
import com.phoenixenterprise.pokedex.object.internal.EncounterSlot;
import com.phoenixenterprise.pokedex.object.internal.LocationArea;
import com.phoenixenterprise.pokedex.object.internal.LocationGameIndex;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class LocationDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LocationDetailActivity";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LocationAreaPagerAdapter mPagerAdapter;

    private ArrayList<ArrayList<EncounterInfo>> mEncounterInfoArrayArray;
    private ArrayList<String> mLocationAreaTitles;

    private ProgressBar mProgress;
    private FrameLayout mNoPkmnMessage;

    private Location mLocation;

    private int mGameVersion;

    private AsyncTask<Void, Integer, Void> mAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.locationDetail_toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;

        mTabLayout = (TabLayout) findViewById(R.id.locationDetail_tabs);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager = (ViewPager) findViewById(R.id.locationDetail_viewpager);
        mPagerAdapter = new LocationAreaPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);

        mProgress = (ProgressBar) findViewById(R.id.locationDetail_progress);
        mNoPkmnMessage = (FrameLayout) findViewById(R.id.locationDetail_fl_noPokemon);

        mLocation = getIntent().getExtras().getParcelable("LOCATION");
        if (mLocation == null) {
            throw new NullPointerException("Parcelable Location object through Intent is null");
        }

        getSupportActionBar().setTitle(mLocation.getLocation());
        getSupportActionBar().setSubtitle(mLocation.getRegion());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEncounterInfoArrayArray = new ArrayList<>();
        mLocationAreaTitles = new ArrayList<>();


        // TODO: Change the database so versions correspond with edited values (i.e. ignoring Conquest series)

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
                loadDatabases();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mProgress.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                setupLayouts();
            }
        }.execute();
    }


    private void loadDatabases() {
        Log.d(LOG_TAG, "----- LOCATION: " + mLocation.getLocation() + " -----\n");

        LocationAreaDBHelper locationAreaDBHelper = new LocationAreaDBHelper(this);
        ArrayList<LocationArea> locationAreas = locationAreaDBHelper.getLocationAreaList(mLocation.getLocationId());

        Log.d(LOG_TAG, "LocationAreas: " + locationAreas.size());

        int count = 0;
        for (LocationArea locationArea : locationAreas) {
            count++;
            Log.d(LOG_TAG, "--- LocationArea: " + locationArea.getIdentifier() + " (loop index:" + count + ") ---");

            mLocationAreaTitles.add(locationArea.getIdentifier());
            ArrayList<EncounterInfo> aEncounterInfoArray = new ArrayList<>();

            EncounterDBHelper encounterDBHelper = new EncounterDBHelper(this);
            ArrayList<Encounter> encounters = encounterDBHelper.getEncountersList(locationArea.getId());

            for (Encounter encounter : encounters) {
                MiniPokemon miniPokemon = new MiniPokemon(this, encounter.getPokemonId(), ""); // TODO: Works for specific forms?
                EncounterSlot encounterSlot = new EncounterSlot(this, encounter.getEncounterSlotId());

                String info = "- Encounter: " + encounter.getId() +
                        " | " + miniPokemon.getPokemon() +
                        " @ Levels: " + encounter.getPokemonMinLvl() +
                        "-" + encounter.getPokemonMaxLvl() +
                        " by: " + InfoUtils.getEncounterMethodFromId(encounterSlot.getEncounterMethodId()) +
                        " [version: " + encounter.getGameVersionId() + "]" +
                        " - (encounter slot id " + encounterSlot.getId() +
                        " (rarity %:" + encounterSlot.getRarity() + "))";
                Log.d(LOG_TAG, info);

                aEncounterInfoArray.add(new EncounterInfo(
                        encounter.getId(),
                        miniPokemon,
                        encounter.getPokemonMinLvl(),
                        encounter.getPokemonMaxLvl(),
                        encounter.getGameVersionId(),
                        encounterSlot.getId(),
                        encounterSlot.getEncounterMethodId(),
                        encounterSlot.getSlot(),
                        encounterSlot.getRarity()
                ));

                // TODO: LocationAreaEncounterRates
            }

            if (!aEncounterInfoArray.isEmpty()) {
                mEncounterInfoArrayArray.add(aEncounterInfoArray);
            }
        }


    }

    private void setupLayouts() {
        if (mEncounterInfoArrayArray.isEmpty()) {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mNoPkmnMessage.setVisibility(View.VISIBLE);
            return;
        }

        for (ArrayList<EncounterInfo> encounterInfoArray : mEncounterInfoArrayArray) {
            View aPage = getLayoutInflater().inflate(R.layout.fragment_location_detail, null);
            LinearLayout container = (LinearLayout) aPage.findViewById(R.id.locationDetail_frag_llContainer);

            ArrayList<Integer> gameVersionIds = new ArrayList<>();
            ArrayList<Integer> encounterMethodIds = new ArrayList<>();

            for (EncounterInfo encounter : encounterInfoArray) {
                int versionId = encounter.getGameVersionId();
                if (!gameVersionIds.contains(versionId)) {
                    gameVersionIds.add(versionId);
                    Log.d(LOG_TAG, "Added game version: " + versionId);
                }

                int methodId = encounter.getEncounterMethodId();
                if (!encounterMethodIds.contains(methodId)) {
                    encounterMethodIds.add(methodId);
                    Log.d(LOG_TAG, "Added encounter method: " + methodId);
                }
            }
            Collections.sort(gameVersionIds);
            Collections.sort(encounterMethodIds);

            mGameVersion = gameVersionIds.get(gameVersionIds.size()-1);

            container.removeAllViews();
            for (int encounterMethodId : encounterMethodIds) {
                container.addView(makeCard(container, encounterInfoArray, encounterMethodId, mGameVersion));
            }

            int index = mEncounterInfoArrayArray.indexOf(encounterInfoArray);
            String title;
            if (mLocationAreaTitles.get(index).trim().equals("") && mLocationAreaTitles.size() != 1) {
                title = "General";
            } else {
                title = InfoUtils.formatStringIdentifier(mLocationAreaTitles.get(index));
            }
            mPagerAdapter.addViewWithTitle(aPage, title);
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.text_white_secondary),
                ContextCompat.getColor(this, R.color.text_white));

        if (mPagerAdapter.getCount() == 1) {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    private View makeCard(LinearLayout container, ArrayList<EncounterInfo> encounterInfoArray, int encounterMethodId, int gameVersion) {
        View card = getLayoutInflater().inflate(R.layout.card_detail_encounter, container, false);

        TextView title = (TextView) card.findViewById(R.id.card_encounter_titleText);
        LinearLayout itemsContainer = (LinearLayout) card.findViewById(R.id.card_encounter_linearLayout);

        title.setText(InfoUtils.getEncounterMethodFromId(encounterMethodId));

        ArrayList<EncounterInfo> encounterInfos = sort(encounterInfoArray, encounterMethodId, gameVersion);

        // Getting a list of Pokemon for this encounter method
        ArrayList<String> pokemonList = new ArrayList<>();
        for (EncounterInfo encounter : encounterInfos) {
            String pkmnString = encounter.getPokemon().toString();
            if (!pokemonList.contains(pkmnString)) {
                pokemonList.add(pkmnString);
            }
        }

        // Populating a formatted encounters list with values for each pokemon
        ArrayList<EncounterItem> formattedEncounters = new ArrayList<>();
        for (String pkmnString : pokemonList) {
            ArrayList<EncounterItemSlot> slots = new ArrayList<>();
            for (EncounterInfo info : encounterInfos) {
                if (info.getPokemon().toString().equals(pkmnString)) {
                    slots.add(new EncounterItemSlot(
                            info.getPokemonMinLvl(),
                            info.getPokemonMaxLvl(),
                            info.getSlot(),
                            info.getRarity()));
                }
            }
            formattedEncounters.add(new EncounterItem(MiniPokemon.stringToPokemon(pkmnString), slots));
        }

        EncounterVGAdapter adapter = new EncounterVGAdapter(this, itemsContainer, formattedEncounters);
        adapter.createListItems();

        return card;
    }

    private ArrayList<EncounterInfo> sort(ArrayList<EncounterInfo> list, int encounterMethodId, int gameVersionId) {
        ArrayList<EncounterInfo> finalList = new ArrayList<>();
        for (EncounterInfo item : list) {
            if (item.getGameVersionId()==gameVersionId && item.getEncounterMethodId()==encounterMethodId) {
                finalList.add(item);
            }
        }
        Log.d(LOG_TAG, "Game version: " + gameVersionId);
        Log.d(LOG_TAG, "Encounter method: " + InfoUtils.getEncounterMethodFromId(encounterMethodId));
        Collections.sort(finalList, new Comparator<EncounterInfo>() {
            @Override
            public int compare(EncounterInfo encounter1, EncounterInfo encounter2) {
                MiniPokemon pokemon1 = encounter1.getPokemon();
                MiniPokemon pokemon2 = encounter2.getPokemon();
                return pokemon1.getNationalId() - pokemon2.getNationalId();
            }
        });
        return finalList;
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
