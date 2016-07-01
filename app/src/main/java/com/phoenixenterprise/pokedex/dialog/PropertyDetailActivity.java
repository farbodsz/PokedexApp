package com.phoenixenterprise.pokedex.dialog;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.util.InfoUtils;

public class PropertyDetailActivity extends AppCompatActivity {

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
                propertyName = res.getString(R.string.header_catch_rate);
                description = res.getString(R.string.description_catch, mValue);
                break;
            case PROPERTY_HAPPINESS:
                title = mValue + " happiness";
                propertyName = res.getString(R.string.header_base_happiness);
                description = res.getString(R.string.description_happiness, mValue);
                break;
            case PROPERTY_LEVELLING_RATE:
                title = InfoUtils.getGrowthFromAbbreviation(mValue);
                propertyName = res.getString(R.string.header_levelling_rate);
                description = res.getString(R.string.description_levelling, InfoUtils.getGrowthFromAbbreviation(mValue));
                break;
            case PROPERTY_EXP:
                title = mValue + " exp points";
                propertyName = res.getString(R.string.header_exp_growth);
                description = res.getString(R.string.description_exp, mValue);
                break;
            case PROPERTY_GENERATION:
                title = "Gen. " + mValue;
                propertyName = res.getString(R.string.header_generation);
                description = res.getString(R.string.description_generation, mValue);
                break;
            case PROPERTY_MASS:
                title = mValue + " kg";
                propertyName = res.getString(R.string.header_mass);
                description = res.getString(R.string.description_mass, mValue);
                break;
            case PROPERTY_HEIGHT:
                title = mValue + " m";
                propertyName = res.getString(R.string.header_height);
                description = res.getString(R.string.description_height, mValue);
                break;
            case PROPERTY_COLOUR:
                propertyName = res.getString(R.string.header_colour);
                description = res.getString(R.string.description_colour, mValue);
                break;
            case PROPERTY_SHAPE:
                propertyName = res.getString(R.string.header_shape);
                description = res.getString(R.string.description_shape, mValue, InfoUtils.getTechnicalShapeFromSimple(mValue));
                break;
            case PROPERTY_HABITAT:
                propertyName = res.getString(R.string.header_habitat);
                description = res.getString(R.string.description_habitat, mValue);
                break;
            case PROPERTY_EGG_STEPS:
                title = mValue + " steps";
                propertyName = res.getString(R.string.header_base_egg_steps);
                description = res.getString(R.string.description_egg_steps, mValue);
                break;
            case PROPERTY_EGG_CYCLES:
                title = mValue + " cycles";
                propertyName = res.getString(R.string.header_base_egg_cycles);
                description = res.getString(R.string.description_egg_cycles, mValue);
                break;
        }

        tvTitle.setText(title);
        tvSubtitle.setText(propertyName);
        tvDescription.setText(description);

        // TODO: Add options to filter Pokemon by this attribute
        findViewById(R.id.propertyDetail_llButtonBar).setVisibility(View.GONE);
    }
}
