/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.ui;

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

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.NaturesDBHelper;
import com.satsumasoftware.pokedex.framework.nature.MiniNature;
import com.satsumasoftware.pokedex.ui.adapter.NatureDexAdapter;
import com.satsumasoftware.pokedex.ui.dialog.NatureDetailActivity;
import com.satsumasoftware.pokedex.ui.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NaturesActivity extends BaseActivity {

    @Override
    protected Toolbar getSelfToolbar() { return (Toolbar) findViewById(R.id.toolbar); }
    @Override
    protected DrawerLayout getSelfDrawerLayout() { return (DrawerLayout) findViewById(R.id.drawerLayout); }
    @Override
    protected int getSelfNavDrawerItem() { return NAVDRAWER_ITEM_NATUREDEX; }
    @Override
    protected NavigationView getSelfNavigationView() { return (NavigationView) findViewById(R.id.navigationView); }


    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdUtils.setupAds(this, R.id.adView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        DragScrollBar scrollBar = new DragScrollBar(this, mRecyclerView, false);

        NaturesDBHelper dbHelper = NaturesDBHelper.getInstance(this);
        populateListview(dbHelper.getAllMiniNatures());
    }

    private void populateListview(ArrayList<MiniNature> items) {
        Collections.sort(items, new Comparator<MiniNature>() {
            @Override
            public int compare(MiniNature nature1, MiniNature nature2) {
                return nature1.getName().compareTo(nature2.getName());
            }
        });
        final ArrayList<MiniNature> itemsFinal = items;
        NatureDexAdapter adapter = new NatureDexAdapter(itemsFinal);
        adapter.setOnRowClickListener(new NatureDexAdapter.OnRowClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent intent = new Intent(NaturesActivity.this, NatureDetailActivity.class);
                intent.putExtra(NatureDetailActivity.EXTRA_NATURE, itemsFinal.get(position));
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
