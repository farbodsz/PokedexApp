package com.satsumasoftware.pokedex.ui;

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

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.ui.filter.FilterResultsActivity;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdvancedFilterActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {

    private View mRootLayout;

    private String mFilterName, mFilterSpecies, mFilterType1, mFilterType2, mFilterGrowth, mFilterGen;
    private int mFilterAbility;
    private ArrayList<MiniAbility> mArrayAbilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_filter);
        mRootLayout = findViewById(R.id.rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        final CardView cvPrompt = (CardView) findViewById(R.id.cardView_prompt);
        if (PrefUtils.isFilterPromptDone(this)) {
            cvPrompt.setVisibility(View.GONE);
        } else {
            Button promptBtn = (Button) findViewById(R.id.button_prompt);
            promptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cvPrompt.setVisibility(View.GONE);
                    PrefUtils.markFilterPromptDone(getBaseContext());
                }
            });
        }

        final EditText etName = (EditText) findViewById(R.id.editText_name);
        final EditText etSpecies = (EditText) findViewById(R.id.editText_species);
        LabelledSpinner spinnerType1 = (LabelledSpinner) findViewById(R.id.spinner_type_1);
        LabelledSpinner spinnerType2 = (LabelledSpinner) findViewById(R.id.spinner_type_2);
        LabelledSpinner spinnerAbility = (LabelledSpinner) findViewById(R.id.spinner_ability);
        LabelledSpinner spinnerGrowth = (LabelledSpinner) findViewById(R.id.spinner_growth);
        LabelledSpinner spinnerGen = (LabelledSpinner) findViewById(R.id.spinner_generation);
        Button btnFilter = (Button) findViewById(R.id.button_go);

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
                && (mFilterAbility == 0 || mFilterAbility == getFirstOfList(mArrayAbilities))
                && (mFilterGrowth == null || mFilterGrowth.equals(getFirstOfList(R.array.filter_levellingRate)))
                && (mFilterGen == null || mFilterGen.equals(getFirstOfList(R.array.filter_gen)))) {
            Snackbar.make(mRootLayout, R.string.filter_error, Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Start the Filter activity
        Intent intent = new Intent(this, FilterResultsActivity.class);
        if (mFilterName != null && !mFilterName.isEmpty()) {
            intent.putExtra(FilterResultsActivity.FILTER_NAME, mFilterName);
        }
        if (mFilterSpecies != null && !mFilterSpecies.isEmpty()) {
            intent.putExtra(FilterResultsActivity.FILTER_SPECIES, mFilterSpecies);
        }
        if (mFilterType1 != null && !mFilterType1.equals(getFirstOfList(R.array.filter_type))) {
            intent.putExtra(FilterResultsActivity.FILTER_TYPE_1, mFilterType1);
        }
        if (mFilterType2 != null && !mFilterType2.equals(getFirstOfList(R.array.filter_type))) {
            intent.putExtra(FilterResultsActivity.FILTER_TYPE_2, mFilterType2);
        }
        if (mFilterAbility != 0 && mFilterAbility != getFirstOfList(mArrayAbilities)) {
            intent.putExtra(FilterResultsActivity.FILTER_ABILITY, mFilterAbility);
        }
        if (mFilterGrowth != null && !mFilterGrowth.equals(getFirstOfList(R.array.filter_levellingRate))) {
            intent.putExtra(FilterResultsActivity.FILTER_GROWTH, mFilterGrowth);
        }
        if (mFilterGen != null && !mFilterGen.equals(getFirstOfList(R.array.filter_gen))) {
            intent.putExtra(FilterResultsActivity.FILTER_GENERATION, mFilterGen);
        }
        startActivity(intent);
    }

    private ArrayList<MiniAbility> getSortedAbilityList() {
        AbilitiesDBHelper helper = new AbilitiesDBHelper(this);
        ArrayList<MiniAbility> list = helper.getAllMiniAbilities();
        Collections.sort(list, new Comparator<MiniAbility>() {
            @Override public int compare(MiniAbility ability1, MiniAbility ability2) {
                return ability1.getName().compareTo(ability2.getName()); // Ascending
            }
        });
        MiniAbility placeHolder = new MiniAbility(0, getResources().getString(R.string.no_filter));
        list.add(0, placeHolder);
        return list;
    }

    private int getFirstOfList(ArrayList<MiniAbility> arrayList) {
        return arrayList.get(0).getId();
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
                final CardView cvPrompt = (CardView) findViewById(R.id.cardView_prompt);
                cvPrompt.setVisibility(View.VISIBLE);
                Button promptBtn = (Button) findViewById(R.id.button_prompt);
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
            case R.id.spinner_type_1:
                mFilterType1 = selected;
                break;
            case R.id.spinner_type_2:
                mFilterType2 = selected;
                break;
            case R.id.spinner_ability:
                mFilterAbility = mArrayAbilities.get(position).getId();
                break;
            case R.id.spinner_growth:
                mFilterGrowth = selected;
                break;
            case R.id.spinner_generation:
                mFilterGen = selected;
                break;
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> parent) {}
}
