package com.satsumasoftware.pokedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.satsumasoftware.pokedex.adapter.ChangelogAdapter;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.ChangelogUtils;

import java.util.ArrayList;

public class ChangelogActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.changelog_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdUtils.setupAds(this, R.id.changelog_adView);

        mRecyclerView = (RecyclerView) findViewById(R.id.changelog_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setChangelogInformation();
    }

    private void setChangelogInformation() {
        ArrayList<Integer> arrayVersionCodes = ChangelogUtils.getListOfVersions(this);
        ArrayList<ArrayList<String>> arrayDescriptions = new ArrayList<>();
        for (int i = 0; i < arrayVersionCodes.size(); i++) {
            arrayDescriptions.add(ChangelogUtils.getVersionChanges(this, arrayVersionCodes.get(i)));
        }
        mRecyclerView.setAdapter(new ChangelogAdapter(arrayVersionCodes, arrayDescriptions, true));
    }
}
