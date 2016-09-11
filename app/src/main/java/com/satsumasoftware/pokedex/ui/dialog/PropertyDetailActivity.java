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
import com.satsumasoftware.pokedex.framework.Color;
import com.satsumasoftware.pokedex.framework.GrowthRate;
import com.satsumasoftware.pokedex.framework.Habitat;
import com.satsumasoftware.pokedex.framework.HatchCounter;
import com.satsumasoftware.pokedex.framework.Height;
import com.satsumasoftware.pokedex.framework.Mass;
import com.satsumasoftware.pokedex.framework.Shape;
import com.satsumasoftware.pokedex.ui.filter.FilterResultsActivity;

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

    private String mProperty;
    private int mValue;

    private String mFilterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_property);

        Bundle extras = getIntent().getExtras();
        mProperty = extras.getString("PROPERTY");
        mValue = extras.getInt("VALUE");

        setupLayouts();
    }

    private void setupLayouts() {
        TextView tvTitle = (TextView) findViewById(R.id.propertyDetail_tvTitle);
        TextView tvSubtitle = (TextView) findViewById(R.id.propertyDetail_tvDetailType);
        TextView tvDescription = (TextView) findViewById(R.id.propertyDetail_tvDescription);
        //Button btnFilter = (Button) findViewById(R.id.propertyDetail_btnFilter);

        Resources res = getResources();

        String propertyName = "", description = "";
        String title = String.valueOf(mValue);  // default title
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
                GrowthRate levellingRate = new GrowthRate(mValue);
                title = levellingRate.getName();
                propertyName = res.getString(R.string.attr_levelling_rate);
                description = res.getString(R.string.description_levelling, levellingRate.getName());
                mFilterName = FilterResultsActivity.FILTER_GROWTH;
                break;
            case PROPERTY_EXP:
                GrowthRate expGrowth = new GrowthRate(mValue);
                int experience = expGrowth.findMaxExperience();
                title = experience + " exp points";
                propertyName = res.getString(R.string.attr_exp_growth);
                description = res.getString(R.string.description_exp, String.valueOf(experience));
                mFilterName = FilterResultsActivity.FILTER_GROWTH;
                break;
            case PROPERTY_GENERATION:
                title = "Gen. " + mValue;
                propertyName = res.getString(R.string.attr_generation);
                description = res.getString(R.string.description_generation, mValue);
                mFilterName = FilterResultsActivity.FILTER_GENERATION;
                break;
            case PROPERTY_MASS:
                Mass mass = new Mass(mValue);
                title = mass.getDisplayedText(this);
                propertyName = res.getString(R.string.attr_mass);
                description = res.getString(R.string.description_mass, mass.getMetricValue());
                mFilterName = FilterResultsActivity.FILTER_MASS;
                break;
            case PROPERTY_HEIGHT:
                Height height = new Height(mValue);
                title = height.getDisplayedText(this);
                propertyName = res.getString(R.string.attr_height);
                description = res.getString(R.string.description_height, height.getMetricValue());
                mFilterName = FilterResultsActivity.FILTER_HEIGHT;
                break;
            case PROPERTY_COLOUR:
                Color color = new Color(mValue);
                title = color.getName();
                propertyName = res.getString(R.string.attr_colour);
                description = res.getString(R.string.description_colour, color.getName());
                mFilterName = FilterResultsActivity.FILTER_COLOUR;
                break;
            case PROPERTY_SHAPE:
                Shape shape = new Shape(mValue);
                title = shape.getSimpleName();
                propertyName = res.getString(R.string.attr_shape);
                description = res.getString(R.string.description_shape, shape.getSimpleName(), shape.getTechnicalName());
                mFilterName = FilterResultsActivity.FILTER_SHAPE;
                break;
            case PROPERTY_HABITAT:
                Habitat habitat = new Habitat(mValue);
                title = habitat.getName();
                propertyName = res.getString(R.string.attr_habitat);
                description = res.getString(R.string.description_habitat, habitat.getName());
                mFilterName = FilterResultsActivity.FILTER_HABITAT;
                break;
            case PROPERTY_EGG_STEPS:
                HatchCounter hatchCounter1 = new HatchCounter(mValue);
                title = hatchCounter1.getEggSteps() + " steps";
                propertyName = res.getString(R.string.attr_base_egg_steps);
                description = res.getString(R.string.description_egg_steps, hatchCounter1.getEggSteps());
                mFilterName = FilterResultsActivity.FILTER_HATCH_COUNTER;
                break;
            case PROPERTY_EGG_CYCLES:
                HatchCounter hatchCounter2 = new HatchCounter(mValue);
                title = hatchCounter2.getEggCycles() + " cycles";
                propertyName = res.getString(R.string.attr_base_egg_cycles);
                description = res.getString(R.string.description_egg_cycles, hatchCounter2.getEggCycles());
                mFilterName = FilterResultsActivity.FILTER_HATCH_COUNTER;
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
                intent.putExtra(mFilterName, String.valueOf(mValue));
                startActivity(intent);
            }
        });
    }
}
