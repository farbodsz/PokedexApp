package com.satsumasoftware.pokedex.ui.dialog;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.GrowthRate;
import com.satsumasoftware.pokedex.ui.filter.FilterResultsActivity;
import com.satsumasoftware.pokedex.util.DataUtilsKt;

public class PropertyDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PROPERTY = "PROPERTY";
    public static final String EXTRA_VALUE = "VALUE";

    public static final String PROPERTY_CATCH_RATE = "catch_rate";
    public static final String PROPERTY_HAPPINESS = "base_happiness";
    public static final String PROPERTY_LEVELLING_RATE = "levelling_rate";
    public static final String PROPERTY_EXP = "exp_to_100";
    public static final String PROPERTY_GENERATION = "generation";
    public static final String PROPERTY_MASS = "mass";
    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_COLOUR = "colour";
    public static final String PROPERTY_SHAPE = "shape";
    public static final String PROPERTY_HABITAT = "habitat";
    public static final String PROPERTY_EGG_STEPS = "base_egg_steps";
    public static final String PROPERTY_EGG_CYCLES = "base_egg_cycles";

    private String mProperty, mValue;

    private String mFilterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_property);

        Bundle extras = getIntent().getExtras();
        mProperty = extras.getString("PROPERTY");
        mValue = extras.getString("VALUE");
        // The above should be converted before putting extra to this intent to a string even if it was an integer

        setupLayouts();
    }

    private void setupLayouts() {
        TextView tvTitle = (TextView) findViewById(R.id.propertyDetail_tvTitle);
        TextView tvSubtitle = (TextView) findViewById(R.id.propertyDetail_tvDetailType);
        TextView tvDescription = (TextView) findViewById(R.id.propertyDetail_tvDescription);
        //Button btnFilter = (Button) findViewById(R.id.propertyDetail_btnFilter);

        Resources res = getResources();

        String propertyName = "", description = "";
        String title = mValue;
        switch (mProperty) {
            case PROPERTY_CATCH_RATE:
                title = "Rate: " + mValue;
                propertyName = res.getString(R.string.attr_catch_rate);
                description = res.getString(R.string.description_catch, mValue);
                mFilterName = FilterResultsActivity.FILTER_CATCH_RATE;
                break;
            case PROPERTY_HAPPINESS:
                title = mValue + " happiness";
                propertyName = res.getString(R.string.attr_base_happiness);
                description = res.getString(R.string.description_happiness, mValue);
                mFilterName = FilterResultsActivity.FILTER_HAPPINESS;
                break;
            case PROPERTY_LEVELLING_RATE:
                // mValue is the growth id
                title = new GrowthRate(Integer.parseInt(mValue)).getName();
                propertyName = res.getString(R.string.attr_levelling_rate);
                description = res.getString(R.string.description_levelling, mValue);
                mFilterName = FilterResultsActivity.FILTER_GROWTH;
                break;
            case PROPERTY_EXP:
                // mValue is the growth id
                title = new GrowthRate(Integer.parseInt(mValue)).findMaxExperience() + " exp points";
                propertyName = res.getString(R.string.attr_exp_growth);
                description = res.getString(R.string.description_exp, mValue);
                mFilterName = FilterResultsActivity.FILTER_GROWTH;
                break;
            case PROPERTY_GENERATION:
                title = "Gen. " + mValue;
                propertyName = res.getString(R.string.attr_generation);
                description = res.getString(R.string.description_generation, mValue);
                mFilterName = FilterResultsActivity.FILTER_GENERATION;
                break;
            case PROPERTY_MASS:
                title = mValue + " kg";
                propertyName = res.getString(R.string.attr_mass);
                description = res.getString(R.string.description_mass, mValue);
                mFilterName = FilterResultsActivity.FILTER_MASS;
                break;
            case PROPERTY_HEIGHT:
                title = mValue + " m";
                propertyName = res.getString(R.string.attr_height);
                description = res.getString(R.string.description_height, mValue);
                mFilterName = FilterResultsActivity.FILTER_HEIGHT;
                break;
            case PROPERTY_COLOUR:
                propertyName = res.getString(R.string.attr_colour);
                description = res.getString(R.string.description_colour, mValue);
                mFilterName = FilterResultsActivity.FILTER_COLOUR;
                break;
            case PROPERTY_SHAPE:
                propertyName = res.getString(R.string.attr_shape);
                description = res.getString(R.string.description_shape, mValue, DataUtilsKt.getTechnicalShapeFromSimple(mValue));
                mFilterName = FilterResultsActivity.FILTER_SHAPE;
                break;
            case PROPERTY_HABITAT:
                propertyName = res.getString(R.string.attr_habitat);
                description = res.getString(R.string.description_habitat, mValue);
                mFilterName = FilterResultsActivity.FILTER_HABITAT;
                break;
            case PROPERTY_EGG_STEPS:
                title = mValue + " steps";
                propertyName = res.getString(R.string.attr_base_egg_steps);
                description = res.getString(R.string.description_egg_steps, mValue);
                mFilterName = FilterResultsActivity.FILTER_EGG_STEPS;
                break;
            case PROPERTY_EGG_CYCLES:
                title = mValue + " cycles";
                propertyName = res.getString(R.string.attr_base_egg_cycles);
                description = res.getString(R.string.description_egg_cycles, mValue);
                mFilterName = FilterResultsActivity.FILTER_EGG_CYCLES;
                break;
        }

        tvTitle.setText(title);
        tvSubtitle.setText(propertyName);
        tvDescription.setText(description);

        Button btnFilter = (Button) findViewById(R.id.propertyDetail_btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterName == null) {
                    Toast.makeText(PropertyDetailActivity.this, "[ERROR] Unable to filter by this attribute!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PropertyDetailActivity.this, FilterResultsActivity.class);
                intent.putExtra(mFilterName, mValue);
                startActivity(intent);
            }
        });
    }
}
