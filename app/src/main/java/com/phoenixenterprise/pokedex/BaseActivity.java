package com.phoenixenterprise.pokedex;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.phoenixenterprise.pokedex.util.AlertUtils;
import com.phoenixenterprise.pokedex.util.Flavours;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * This method should be overridden in subclasses of BaseActivity to use the Toolbar
     * Return null if there is no Toolbar
     */
    protected Toolbar getSelfToolbar() {
        return null;
    }

    /**
     * This method should be overridden in subclasses of BaseActivity to use the DrawerLayout
     * Return null if there is no DrawerLayout
     */
    protected DrawerLayout getSelfDrawerLayout() {
        return null;
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Returns the NavigationView that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return null to mean that this Activity should not have a Nav Drawer.
     */
    protected NavigationView getSelfNavigationView() {
        return null;
    }

    /*
     * This is a list of all items in the nav drawer and their corresponding menu ids
     */
    protected static final int NAVDRAWER_ITEM_NATIONAL_POKEDEX = R.id.navigation_item_pokedex;
    protected static final int NAVDRAWER_ITEM_ABILITYDEX = R.id.navigation_item_abilities;
    protected static final int NAVDRAWER_ITEM_MOVEDEX = R.id.navigation_item_moves;
    protected static final int NAVDRAWER_ITEM_NATUREDEX = R.id.navigation_item_natures;
    protected static final int NAVDRAWER_ITEM_LOCATIONDEX = R.id.navigation_item_locations;
    protected static final int NAVDRAWER_ITEM_FAVOURITES = R.id.navigation_item_favourites;
    protected static final int NAVDRAWER_ITEM_TEAM = R.id.navigation_item_team;
    protected static final int NAVDRAWER_ITEM_EXPERIENCE = R.id.navigation_item_experience;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;

    /*
     * Variables for BaseActivity
     */
    private static final int NAVDRAWER_LAUNCH_DELAY = 200; // previously 250
    private static final int MAIN_CONTENT_FADEIN_DURATION = 200; // previously 250
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 100; // previously 150

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private View mMainContent;


    /*
     * Methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerLayout = getSelfDrawerLayout();
        if (mDrawerLayout == null) {
            return;
        }

        // Gets the main content view in the DrawerLayout
        mMainContent = mDrawerLayout.getChildAt(0);

        mMainContent.setAlpha(0);
        mMainContent.animate()
                .alpha(1)
                .setDuration(MAIN_CONTENT_FADEIN_DURATION);

        setupLayout();
    }

    private void setupLayout() {
        Toolbar toolbar = getSelfToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mNavigationView = getSelfNavigationView();
        if (mNavigationView == null) {
            return;
        }
        mNavigationView.getMenu().findItem(getSelfNavDrawerItem()).setChecked(true);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                handleNavigationSelection(menuItem);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    private void handleNavigationSelection(final MenuItem menuItem) {
        if (menuItem.getItemId() == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawers();
            return;
        }

        // launch the target Activity after a short delay, to allow the close animation to play
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(menuItem.getItemId());
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        mNavigationView.getMenu().findItem(getSelfNavDrawerItem()).setChecked(false);
        menuItem.setChecked(true);

        mMainContent.animate()
                .alpha(0)
                .setDuration(MAIN_CONTENT_FADEOUT_DURATION);

        mDrawerLayout.closeDrawers();
    }

    private void goToNavDrawerItem(int menuItem) {
        Intent intent;
        switch (menuItem) {
            case NAVDRAWER_ITEM_NATIONAL_POKEDEX:
                intent = new Intent(this, PokedexActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_ABILITYDEX:
                intent = new Intent(this, AbilitiesActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_MOVEDEX:
                intent = new Intent(this, MovesActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_NATUREDEX:
                intent = new Intent(this, NaturesActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_LOCATIONDEX:
                intent = new Intent(this, LocationsActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_FAVOURITES:
                if (Flavours.type == Flavours.Type.FREE) {
                    intent = new Intent(this, PokedexActivity.class);
                    AlertUtils.requiresProToast(this);
                } else {
                    intent = new Intent(this, FavouritesActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_TEAM:
                if (Flavours.type == Flavours.Type.FREE) {
                    intent = new Intent(this, PokedexActivity.class);
                    AlertUtils.requiresProToast(this);
                } else {
                    intent = new Intent(this, TeamActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_EXPERIENCE:
                intent = new Intent(this, ExperienceCalculatorActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Simplified From:
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        else return super.onOptionsItemSelected(item);
        */
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
