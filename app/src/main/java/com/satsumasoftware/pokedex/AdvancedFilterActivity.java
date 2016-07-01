package com.satsumasoftware.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.farbod.labelledspinner.LabelledSpinner;
import com.satsumasoftware.pokedex.db.AbilityDBHelper;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;

public class AdvancedFilterActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private View mRootLayout;

    private String mFilterName, mFilterSpecies, mFilterType1, mFilterType2,
            mFilterAbility, mFilterGrowth, mFilterGen;
    private ArrayList<String> mArrayAbilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_filter);
        mRootLayout = findViewById(R.id.advancedFilter_rl_rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.advancedFilter_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupLayouts();
    }

    private void setupLayouts() {
        final CardView cvPrompt = (CardView) findViewById(R.id.advancedFilter_cvPrompt);
        if (PrefUtils.isFilterPromptDone(this)) {
            cvPrompt.setVisibility(View.GONE);
        } else {
            Button promptBtn = (Button) findViewById(R.id.advancedFilter_btnPrompt);
            promptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cvPrompt.setVisibility(View.GONE);
                    PrefUtils.markFilterPromptDone(getBaseContext());
                }
            });
        }

        final EditText etName = (EditText) findViewById(R.id.advancedFilter_etName);
        final EditText etSpecies = (EditText) findViewById(R.id.advancedFilter_etSpecies);
        LabelledSpinner spinnerType1 = (LabelledSpinner) findViewById(R.id.advancedFilter_spinnerType1);
        LabelledSpinner spinnerType2 = (LabelledSpinner) findViewById(R.id.advancedFilter_spinnerType2);
        LabelledSpinner spinnerAbility = (LabelledSpinner) findViewById(R.id.advancedFilter_spinnerAbility);
        LabelledSpinner spinnerGrowth = (LabelledSpinner) findViewById(R.id.advancedFilter_spinnerGrowth);
        LabelledSpinner spinnerGen = (LabelledSpinner) findViewById(R.id.advancedFilter_spinnerGeneration);
        Button btnFilter = (Button) findViewById(R.id.advancedFilter_btnGo);

        spinnerType1.setItemsArray(R.array.filter_type);
        spinnerType1.setOnItemChosenListener(this);
        spinnerType2.setItemsArray(R.array.filter_type);
        spinnerType2.setOnItemChosenListener(this);
        mArrayAbilities = getSortedAbilityList();
        spinnerAbility.setItemsArray(mArrayAbilities);
        spinnerAbility.setOnItemChosenListener(this);
        spinnerGrowth.setItemsArray(R.array.filter_levellingRate);
        spinnerGrowth.setOnItemChosenListener(this);
        spinnerGen.setItemsArray(R.array.filter_gen);
        spinnerGen.setOnItemChosenListener(this);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterName = etName.getText().toString().trim();
                mFilterSpecies = etSpecies.getText().toString().trim();
                filterResults();
            }
        });
    }

    private void filterResults() {
        // Check if at least one thing has been specified
        if (mFilterName.isEmpty()
                && mFilterSpecies.isEmpty()
                && (mFilterType1 == null || mFilterType1.equals(getFirstOfList(R.array.filter_type)))
                && (mFilterType2 == null || mFilterType2.equals(getFirstOfList(R.array.filter_type)))
                && (mFilterAbility == null || mFilterAbility.equals(getFirstOfList(mArrayAbilities)))
                && (mFilterGrowth == null || mFilterGrowth.equals(getFirstOfList(R.array.filter_levellingRate)))
                && (mFilterGen == null || mFilterGen.equals(getFirstOfList(R.array.filter_gen)))) {
            Snackbar.make(mRootLayout, R.string.filter_error, Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Start the Filter activity
        Intent intent = new Intent(this, FilterResultsActivity.class);
        if (mFilterName != null && !mFilterName.isEmpty()) {
            intent.putExtra("NAME", mFilterName);
        }
        if (mFilterSpecies != null && !mFilterSpecies.isEmpty()) {
            intent.putExtra("SPECIES", mFilterSpecies);
        }
        if (mFilterType1 != null && !mFilterType1.equals(getFirstOfList(R.array.filter_type))) {
            intent.putExtra("TYPE1", mFilterType1);
        }
        if (mFilterType2 != null && !mFilterType2.equals(getFirstOfList(R.array.filter_type))) {
            intent.putExtra("TYPE2", mFilterType2);
        }
        if (mFilterAbility != null && !mFilterAbility.equals(getFirstOfList(mArrayAbilities))) {
            intent.putExtra("ABILITY", mFilterAbility);
        }
        if (mFilterGrowth != null && !mFilterGrowth.equals(getFirstOfList(R.array.filter_levellingRate))) {
            intent.putExtra("GROWTH", mFilterGrowth);
        }
        if (mFilterGen != null && !mFilterGen.equals(getFirstOfList(R.array.filter_gen))) {
            intent.putExtra("GENERATION", mFilterGen);
        }
        startActivity(intent);
    }

    private ArrayList<String> getSortedAbilityList() {
        AbilityDBHelper helper = new AbilityDBHelper(this);
        ArrayList<String> list = helper.getAllAbilityNames();
        Collections.sort(list);
        return list;
    }

    private String getFirstOfList(ArrayList<String> arrayList) {
        return arrayList.get(0);
    }

    private String getFirstOfList(int arrayResId) {
        String[] items = getResources().getStringArray(arrayResId);
        return items[0];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_advanced_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                // It has already been marked done (which controls whether or not it shows
                // on opening the activity), so we don't need to deal with this here
                final CardView cvPrompt = (CardView) findViewById(R.id.advancedFilter_cvPrompt);
                cvPrompt.setVisibility(View.VISIBLE);
                Button promptBtn = (Button) findViewById(R.id.advancedFilter_btnPrompt);
                promptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cvPrompt.setVisibility(View.GONE);
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selected = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.advancedFilter_spinnerType1:
                mFilterType1 = selected;
                break;
            case R.id.advancedFilter_spinnerType2:
                mFilterType2 = selected;
                break;
            case R.id.advancedFilter_spinnerAbility:
                mFilterAbility = selected;
                break;
            case R.id.advancedFilter_spinnerGrowth:
                mFilterGrowth = selected;
                break;
            case R.id.advancedFilter_spinnerGeneration:
                mFilterGen = selected;
                break;
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> parent) {}
}
