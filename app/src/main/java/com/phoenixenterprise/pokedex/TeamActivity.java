package com.phoenixenterprise.pokedex;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.phoenixenterprise.pokedex.adapter.TeamAdapter;
import com.phoenixenterprise.pokedex.util.PrefUtils;


public class TeamActivity extends BaseActivity {

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.main_toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.main_drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_TEAM; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.main_navigationView); }


    // TODO: TeamActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new TeamAdapter(this, PrefUtils.getTeamPkmnList(this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }
}
