package com.phoenixenterprise.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phoenixenterprise.pokedex.adapter.NatureDexAdapter;
import com.phoenixenterprise.pokedex.db.NatureDBHelper;
import com.phoenixenterprise.pokedex.dialog.NatureDetailActivity;
import com.phoenixenterprise.pokedex.misc.DividerItemDecoration;
import com.phoenixenterprise.pokedex.object.Nature;
import com.phoenixenterprise.pokedex.util.AdUtils;
import com.phoenixenterprise.pokedex.util.AlertUtils;
import com.phoenixenterprise.pokedex.util.Flavours;

import java.util.ArrayList;


public class NaturesActivity extends BaseActivity {

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.main_toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.main_drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_NATUREDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.main_navigationView); }


    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdUtils.setupAds(this, R.id.main_adView);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        NatureDBHelper dbHelper = new NatureDBHelper(this);
        populateListview(dbHelper.getAllNatures());
    }

    private void populateListview(ArrayList<Nature> items) {
        final ArrayList<Nature> itemsFinal = items;
        NatureDexAdapter adapter = new NatureDexAdapter(itemsFinal);
        adapter.setOnRowClickListener(new NatureDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(NaturesActivity.this, NatureDetailActivity.class);
                intent.putExtra("NATURE", itemsFinal.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_natures, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Flavours.type == Flavours.Type.PAID) {
            menu.findItem(R.id.action_buyPro).setVisible(false);
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
            case R.id.action_buyPro:
                AlertUtils.buyPro(this);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
